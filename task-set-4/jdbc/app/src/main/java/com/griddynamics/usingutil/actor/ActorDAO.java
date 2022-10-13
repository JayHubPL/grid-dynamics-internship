package com.griddynamics.usingutil.actor;

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

public class ActorDAO implements DAO<Actor> {

    private static final String tableName = "actors";
    private static final Function<ResultSet, Actor> actorMapper = rs -> {
        try {
            return new Actor(
                rs.getString("id"),
                rs.getString("name"),
                rs.getDate("birthDate"));
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    };
    private final DatabaseHandler db;
    private final int batchSize;

    public ActorDAO(DatabaseHandler db, int batchSie) {
        this.db = db;
        this.batchSize = batchSie;
    }

    @Override
    public Optional<Actor> findById(String id) throws SQLException {
        final String query = "SELECT * FROM actors WHERE id = ?";
        return db.findOne(query, actorMapper, id);
    }

    @Override
    public Result<Actor, SQLException> save(Actor elem) {
        final String query = "INSERT INTO actors(id, name, birthDate) VALUES (?, ?, ?)";
        try {
            db.execute(query, elem.id(), elem.name(), elem.birthDate());
        } catch (SQLException sqlException) {
            return Result.err(sqlException);
        }
        return Result.ok(elem);
    }

    @Override
    public List<Actor> findAll() throws SQLException {
        final String query = "SELECT * FROM actors";
        return db.findMany(query, actorMapper);
    }

    @Override
    public Iterator<Actor> scan() {
        return new BatchIterator<>(db, actorMapper, batchSize, tableName);
    }
    
}
