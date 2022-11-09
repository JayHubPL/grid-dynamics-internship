package com.griddynamics.usingutil;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.griddynamics.Result;

public interface DAO<T, ID> {

    Optional<T> findById(ID id) throws SQLException;
    Result<T, SQLException> save(T elem);
    List<T> findAll() throws SQLException;
    Iterator<T> scan();

}
