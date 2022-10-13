package com.griddynamics.usingutil;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.griddynamics.Result;

public interface DAO<T> {

    Optional<T> findById(String id) throws SQLException;
    Result<T, SQLException> save(T elem);
    List<T> findAll() throws SQLException;
    Iterator<T> scan();

}
