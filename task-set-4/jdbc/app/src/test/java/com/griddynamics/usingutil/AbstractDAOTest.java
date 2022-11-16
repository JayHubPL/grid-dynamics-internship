package com.griddynamics.usingutil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.griddynamics.Result;
import com.griddynamics.jdbcutil.DatabaseHandler;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractDAOTest<T, ID> {
    
    private static final int BATCH_SIZE = 3;
    private final T dataExample;
    private final DatabaseHandler mockedDB;
    private final String SELECT_WHERE_ID_QUERY;
    private final String SELECT_ALL_QUERY;
    private final Function<ResultSet, T> mapper;
    private final DAO<T, ID> dao;
    private final Supplier<ID> idSupplier;
    private DataSupplier<T> dataSupplier;
    

    protected AbstractDAOTest(Class<? extends DAO<T, ID>> daoClazz, Class<? extends DataSupplier<T>> dataSupplierClazz, String tableName, Function<ResultSet, T> mapper, Supplier<ID> idSupplier)
    throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        mockedDB = mock(DatabaseHandler.class);
        dao = daoClazz.getConstructor(DatabaseHandler.class, int.class).newInstance(mockedDB, BATCH_SIZE);
        this.idSupplier = idSupplier;
        dataSupplier = dataSupplierClazz.getConstructor().newInstance();
        dataExample = dataSupplier.next();
        this.mapper = mapper;
        SELECT_WHERE_ID_QUERY = String.format("SELECT * FROM %s WHERE id = ?", tableName);
        SELECT_ALL_QUERY = String.format("SELECT * FROM %s", tableName);
    }

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void resetDataSupplier()
    throws InstantiationException, IllegalAccessException, IllegalArgumentException,
    InvocationTargetException, NoSuchMethodException, SecurityException {
        dataSupplier = dataSupplier.getClass().getConstructor().newInstance();
    }

    @Test
    public void findById_OneRecordWithTheSpecifiedID_ShouldReturnOptionalWithTheRecord() throws SQLException {
        when(mockedDB.findOne(eq(SELECT_WHERE_ID_QUERY), eq(mapper), any()))
            .thenReturn(Optional.of(dataExample));
        var result = dao.findById(idSupplier.get());
        assertTrue(result.isPresent());
        assertEquals(dataExample, result.get());
    }

    @Test
    public void findById_NoRecordWithTheSpecifiedID_ShouldReturnEmptyOptional() throws SQLException {
        when(mockedDB.findOne(eq(SELECT_WHERE_ID_QUERY), eq(mapper), any()))
            .thenReturn(Optional.empty());
        var result = dao.findById(idSupplier.get());
        assertFalse(result.isPresent());
    }

    @Test
    public void save_SaveWasUnsuccessful_ReturnErr() throws SQLException {
        SQLException sqlException = new SQLException("Save failed");
        doThrow(sqlException).when(mockedDB).execute(anyString(), any(), any(), any());
        var result = dao.save(dataExample);
        assertEquals(Result.err(sqlException), result);
    }

    @Test
    public void save_SaveWasSuccessful_ReturnOK() throws SQLException {
        doNothing().when(mockedDB).execute(anyString(), any(), any(), any());
        var result = dao.save(dataExample);
        assertEquals(Result.ok(dataExample), result);
    }

    @Test
    public void findAll_MoreThanZeroRecordsFound_ReturnListWithAllOfThem() throws SQLException {
        var actors = dataSupplier.getDataList(3);
        when(mockedDB.findMany(SELECT_ALL_QUERY, mapper))
            .thenReturn(actors);
        var result = dao.findAll();
        assertEquals(actors, result);
    }

    @Test
    public void findAll_ZeroRecordsFound_ReturnEmptyList() throws SQLException {
        when(mockedDB.findMany(SELECT_ALL_QUERY, mapper))
            .thenReturn(Collections.emptyList());
        var result = dao.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    public void scan_DBIsEmpty_ShouldNotHaveNext() throws SQLException {
        when(mockedDB.findMany(any(), any(), any(), any()))
            .thenReturn(Collections.emptyList());
        var iter = dao.scan();
        assertFalse(iter.hasNext());
    }

    @Test
    public void scan_DBHasLessRecordsThanBatchSize_GetAllInOneBatch() throws SQLException {
        when(mockedDB.<T>findMany(any(), any(), any(), any()))
            .thenReturn(List.of(dataExample));
        var iter = dao.scan();
        assertTrue(iter.hasNext());
        assertEquals(dataExample, iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void scan_DBHasMoreRecordsThanBatchSize_GetAllRecordsInMultipleBatches()
    throws SQLException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        var batch1 = dataSupplier.getDataList(BATCH_SIZE);
        var batch2 = dataSupplier.getDataList(1);
        when(mockedDB.<T>findMany(any(), any(), any(), any()))
            .thenReturn(batch1, batch2);
        var iter = dao.scan();
        assertTrue(iter.hasNext());
        assertEquals(BATCH_SIZE, checkCurrentBatchSize(iter));
        assertEquals(batch1.get(0), iter.next());
        assertTrue(iter.hasNext());
        assertEquals(batch1.get(1), iter.next());
        assertTrue(iter.hasNext());
        assertEquals(batch1.get(2), iter.next());
        assertTrue(iter.hasNext());
        assertEquals(1, checkCurrentBatchSize(iter));
        assertEquals(batch2.get(0), iter.next());
        assertFalse(iter.hasNext());
    }

    @SuppressWarnings("unchecked")
    private int checkCurrentBatchSize(Iterator<T> iter)
    throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        BatchIterator<T> batchIter = (BatchIterator<T>) iter;
        var field = batchIter.getClass().getDeclaredField("currentBatch");
        field.setAccessible(true);
        var currentBatch = (List<T>) field.get(batchIter);
        return currentBatch.size();
    }

}
