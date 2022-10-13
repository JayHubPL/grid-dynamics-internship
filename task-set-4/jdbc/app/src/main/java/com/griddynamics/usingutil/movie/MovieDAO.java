package com.griddynamics.usingutil.movie;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.griddynamics.Result;
import com.griddynamics.jdbcutil.DatabaseHandler;
import com.griddynamics.usingutil.BatchIterator;
import com.griddynamics.usingutil.DAO;

public class MovieDAO implements DAO<Movie> {

    private static final String tableName = "movies";
    private static final Function<ResultSet, Movie> movieMapper = rs -> {
        try {
            return new Movie(
                rs.getString("id"),
                rs.getString("title"),
                rs.getDate("releaseDate"));
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    };
    private final DatabaseHandler db;
    private final int batchSize;

    public MovieDAO(DatabaseHandler db,  int batchSize) {
        this.db = db;
        this.batchSize = batchSize;
    }

    @Override
    public Optional<Movie> findById(String id) throws SQLException {
        final String query = "SELECT * FROM actors WHERE id = ?";
        return db.findOne(query, movieMapper, id);
    }

    @Override
    public Result<Movie, SQLException> save(Movie elem) {
        final String query = "INSERT INTO movies(id, title, releaseDate) VALUES (?, ?, ?)";
        try {
            db.execute(query, elem.id(), elem.title(), elem.releaseDate());
        } catch (SQLException sqlException) {
            return Result.err(sqlException);
        }
        return Result.ok(elem);
    }

    @Override
    public List<Movie> findAll() throws SQLException {
        final String query = "SELECT * FROM movies";
        return db.findMany(query, movieMapper);
    }

    @Override
    public Iterator<Movie> scan() {
        return new BatchIterator<>(db, movieMapper, batchSize, tableName);
    }
    
}
