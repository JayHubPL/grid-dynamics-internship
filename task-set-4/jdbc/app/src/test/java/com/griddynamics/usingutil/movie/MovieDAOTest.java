package com.griddynamics.usingutil.movie;

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
public class MovieDAOTest {

    private static final Date dateExample = Date.valueOf("2022-10-19");
    private static final Movie movieExample = new Movie("1", "Title", dateExample);
    private static final String INSERT_QUERY = "INSERT INTO movies(id, title, releaseDate) VALUES (?, ?, ?)";
    private static final int BATCH_SIZE = 3;
    private DAO<Movie> movieDAO;

    @Mock
    private DatabaseHandler mockedDB;

    @BeforeEach
    public void setupDAO() throws SQLException {
        movieDAO = new MovieDAO(mockedDB, BATCH_SIZE);
    }

    @Test
    public void findById_OneRecordWithTheSpecifiedID_ShouldReturnOptionalWithTheRecord() throws SQLException {
        when(mockedDB.findOne("SELECT * FROM movies WHERE id = ?", MovieDAO.MOVIE_MAPPER, movieExample.id()))
            .thenReturn(Optional.of(movieExample));
        var result = movieDAO.findById(movieExample.id());
        assertTrue(result.isPresent());
        assertEquals(movieExample, result.get());
    }

    @Test
    public void findById_NoRecordWithTheSpecifiedID_ShouldReturnEmptyOptional() throws SQLException {
        when(mockedDB.findOne(eq("SELECT * FROM movies WHERE id = ?"), eq(MovieDAO.MOVIE_MAPPER), any()))
            .thenReturn(Optional.empty());
        var result = movieDAO.findById("0");
        assertFalse(result.isPresent());
    }

    @Test
    public void save_SaveWasUnsuccessful_ReturnErr() throws SQLException {
        SQLException sqlException = new SQLException("Save failed");
        doThrow(sqlException).when(mockedDB).execute(eq(INSERT_QUERY), any(), any(), any());
        var result = movieDAO.save(movieExample);
        assertEquals(Result.err(sqlException), result);
    }

    @Test
    public void save_SaveWasSuccessful_ReturnOK() throws SQLException {
        doNothing().when(mockedDB).execute(eq(INSERT_QUERY), any(), any(), any());
        var result = movieDAO.save(movieExample);
        assertEquals(Result.ok(movieExample), result);
    }

    @Test
    public void findAll_MoreThanZeroRecordsFound_ReturnListWithAllOfThem() throws SQLException {
        var movies = List.of(
            new Movie("1", "Title1", dateExample),
            new Movie("2", "Title2", dateExample),
            new Movie("3", "Title3", dateExample)
        );
        when(mockedDB.findMany("SELECT * FROM movies", MovieDAO.MOVIE_MAPPER))
            .thenReturn(movies);
        var result = movieDAO.findAll();
        assertEquals(movies, result);
    }

    @Test
    public void findAll_ZeroRecordsFound_ReturnEmptyList() throws SQLException {
        when(mockedDB.findMany("SELECT * FROM movies", MovieDAO.MOVIE_MAPPER))
            .thenReturn(Collections.emptyList());
        var result = movieDAO.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    public void scan_DBIsEmpty_ShouldNotHaveNext() throws SQLException {
        when(mockedDB.findMany(any(), any(), any(), any()))
            .thenReturn(Collections.emptyList());
        var iter = movieDAO.scan();
        assertFalse(iter.hasNext());
    }

    @Test
    public void scan_DBHasLessRecordsThanBatchSize_GetAllInOneBatch() throws SQLException {
        var movie = new Movie("1", "Title1", dateExample);
        when(mockedDB.<Movie>findMany(any(), any(), any(), any()))
            .thenReturn(List.of(movie));
        var iter = movieDAO.scan();
        assertTrue(iter.hasNext());
        assertEquals(movie, iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void scan_DBHasMoreRecordsThanBatchSize_GetAllRecordsInMultipleBatches()
    throws SQLException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        var movie1 = new Movie("1", "Title1", dateExample);
        var movie2 = new Movie("2", "Title2", dateExample);
        var movie3 = new Movie("3", "Title3", dateExample);
        var movie4 = new Movie("4", "Title4", dateExample);
        when(mockedDB.<Movie>findMany(any(), any(), any(), any()))
            .thenReturn(
                List.of(movie1, movie2, movie3),
                List.of(movie4)
            );
        var iter = movieDAO.scan();
        assertTrue(iter.hasNext());
        assertEquals(BATCH_SIZE, checkCurrentBatchSize(iter));
        assertEquals(movie1, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(movie2, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(movie3, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(1, checkCurrentBatchSize(iter));
        assertEquals(movie4, iter.next());
        assertFalse(iter.hasNext());
    }

    @SuppressWarnings("unchecked")
    private int checkCurrentBatchSize(Iterator<Movie> iter)
    throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        BatchIterator<Movie> batchIter = (BatchIterator<Movie>) iter;
        var field = batchIter.getClass().getDeclaredField("currentBatch");
        field.setAccessible(true);
        var currentBatch = (List<Movie>) field.get(batchIter);
        return currentBatch.size();
    }

}
