package com.griddynamics.usingutil.actor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.griddynamics.Result;
import com.griddynamics.jdbcutil.DatabaseHandler;
import com.griddynamics.usingutil.BatchIterator;
import com.griddynamics.usingutil.DAO;

@ExtendWith(MockitoExtension.class)
public class ActorDaoTest {
    
    private static final Date dateExample = Date.valueOf("2022-10-19");
    private static final Actor actorExample = new Actor("1", "Name", dateExample);
    private static final String INSERT_QUERY = "INSERT INTO actors(id, name, birthDate) VALUES (?, ?, ?)";
    private static final int BATCH_SIZE = 3;
    private DAO<Actor> actorDAO;

    @Mock
    private DatabaseHandler mockedDB;

    @BeforeEach
    public void setupDAO() throws SQLException {
        actorDAO = new ActorDAO(mockedDB, BATCH_SIZE);
    }

    @Test
    public void findById_OneRecordWithTheSpecifiedID_ShouldReturnOptionalWithTheRecord() throws SQLException {
        when(mockedDB.findOne("SELECT * FROM actors WHERE id = ?", ActorDAO.ACTOR_MAPPER, actorExample.id()))
            .thenReturn(Optional.of(actorExample));
        var result = actorDAO.findById(actorExample.id());
        assertTrue(result.isPresent());
        assertEquals(actorExample, result.get());
    }

    @Test
    public void findById_NoRecordWithTheSpecifiedID_ShouldReturnEmptyOptional() throws SQLException {
        when(mockedDB.findOne(eq("SELECT * FROM actors WHERE id = ?"), eq(ActorDAO.ACTOR_MAPPER), any()))
            .thenReturn(Optional.empty());
        var result = actorDAO.findById("0");
        assertFalse(result.isPresent());
    }

    @Test
    public void save_SaveWasUnsuccessful_ReturnErr() throws SQLException {
        SQLException sqlException = new SQLException("Save failed");
        doThrow(sqlException).when(mockedDB).execute(eq(INSERT_QUERY), any(), any(), any());
        var result = actorDAO.save(actorExample);
        assertEquals(Result.err(sqlException), result);
    }

    @Test
    public void save_SaveWasSuccessful_ReturnOK() throws SQLException {
        doNothing().when(mockedDB).execute(eq(INSERT_QUERY), any(), any(), any());
        var result = actorDAO.save(actorExample);
        assertEquals(Result.ok(actorExample), result);
    }

    @Test
    public void findAll_MoreThanZeroRecordsFound_ReturnListWithAllOfThem() throws SQLException {
        var actors = List.of(
            new Actor("1", "Name1", dateExample),
            new Actor("2", "Name2", dateExample),
            new Actor("3", "Name3", dateExample)
        );
        when(mockedDB.findMany("SELECT * FROM actors", ActorDAO.ACTOR_MAPPER))
            .thenReturn(actors);
        var result = actorDAO.findAll();
        assertEquals(actors, result);
    }

    @Test
    public void findAll_ZeroRecordsFound_ReturnEmptyList() throws SQLException {
        when(mockedDB.findMany("SELECT * FROM actors", ActorDAO.ACTOR_MAPPER))
            .thenReturn(Collections.emptyList());
        var result = actorDAO.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    public void scan_DBIsEmpty_ShouldNotHaveNext() throws SQLException {
        when(mockedDB.findMany(any(), any(), any(), any()))
            .thenReturn(Collections.emptyList());
        var iter = actorDAO.scan();
        assertFalse(iter.hasNext());
    }

    @Test
    public void scan_DBHasLessRecordsThanBatchSize_GetAllInOneBatch() throws SQLException {
        var actor = new Actor("1", "Name1", dateExample);
        when(mockedDB.<Actor>findMany(any(), any(), any(), any()))
            .thenReturn(List.of(actor));
        var iter = actorDAO.scan();
        assertTrue(iter.hasNext());
        assertEquals(actor, iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void scan_DBHasMoreRecordsThanBatchSize_GetAllRecordsInMultipleBatches()
    throws SQLException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        var actor1 = new Actor("1", "Name1", dateExample);
        var actor2 = new Actor("2", "Name2", dateExample);
        var actor3 = new Actor("3", "Name3", dateExample);
        var actor4 = new Actor("4", "Name4", dateExample);
        when(mockedDB.<Actor>findMany(any(), any(), any(), any()))
            .thenReturn(
                List.of(actor1, actor2, actor3),
                List.of(actor4)
            );
        var iter = actorDAO.scan();
        assertTrue(iter.hasNext());
        assertEquals(BATCH_SIZE, checkCurrentBatchSize(iter));
        assertEquals(actor1, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(actor2, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(actor3, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(1, checkCurrentBatchSize(iter));
        assertEquals(actor4, iter.next());
        assertFalse(iter.hasNext());
    }

    @SuppressWarnings("unchecked")
    private int checkCurrentBatchSize(Iterator<Actor> iter)
    throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        BatchIterator<Actor> batchIter = (BatchIterator<Actor>) iter;
        var field = batchIter.getClass().getDeclaredField("currentBatch");
        field.setAccessible(true);
        var currentBatch = (List<Actor>) field.get(batchIter);
        return currentBatch.size();
    }

}
