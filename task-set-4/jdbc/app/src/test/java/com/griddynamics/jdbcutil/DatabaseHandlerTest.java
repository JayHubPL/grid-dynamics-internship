package com.griddynamics.jdbcutil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.griddynamics.deletesomeusers.User;

public class DatabaseHandlerTest {

    private static final Path RESOURCE_DIR = Paths.get("src", "test", "resources");
    private static final Function<ResultSet, User> USER_MAPPER = rs -> {
        try {
            return new User(
                rs.getString("id"),
                rs.getString("username")
            );
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    };

    private DatabaseHandler db;

    @BeforeEach
    public void setupDB() throws SQLException, IOException {
        db = new DatabaseHandler(new H2DBConnectionProvider());
        initializeDBContents(db.conn, "users");
    }

    @AfterEach
    public void closeDB() throws SQLException {
        db.close();
    }

    @Test
    public void execute_QueryYeldsNoResultSet_ShouldNotThrow() throws SQLException {
        db.execute("CREATE TEMP TABLE tmp (id int PRIMARY KEY)");
    }

    @Test
    public void execute_QueryYeldsAResultSet_ShouldThrow() {
        assertThrows(SQLException.class, () -> {
            db.execute("SELECT * FROM users");
        });
    }

    @Test
    public void findOne_QueryYeldsZeroResults_ShouldReturnEmptyOptional() throws SQLException {
        var result = db.findOne("SELECT * FROM users WHERE 1 <> 1", USER_MAPPER);
        assertFalse(result.isPresent());
    }

    @Test
    public void findOne_QueryYeldsOneResult_ShouldReturnOptionalWithTheUser() throws SQLException {
        var expectedUserID = "1";
        var result = db.findOne("SELECT * FROM users WHERE id = ? LIMIT 1", USER_MAPPER, expectedUserID);
        assertTrue(result.isPresent());
        var user = result.get();
        assertEquals(expectedUserID, user.id());
    }

    @Test
    public void findOne_QueryYeldsMoreThanOneResult_ShouldThrow() throws SQLException {
        assertThrows(SQLException.class, () -> {
            db.findOne("SELECT * FROM users", USER_MAPPER);
        });
    }

    @Test
    public void findMany_QueryYeldsZeroResults_ShouldReturnEmptyList() throws SQLException {
        var result = db.findMany("SELECT * FROM users WHERE 1 <> 1", USER_MAPPER);
        assertTrue(result.isEmpty());
    }

    @Test
    public void findMany_QueryYeldsOneResult_ShouldReturnListWithOneElem() throws SQLException {
        var expectedUserID = "1";
        var result = db.findMany("SELECT * FROM users WHERE id = ? LIMIT 1", USER_MAPPER, expectedUserID);
        assertEquals(1, result.size());
        var user = result.get(0);
        assertEquals(expectedUserID, user.id());
    }

    @Test
    public void findMany_QueryYeldsMoreThanOneResult_ShouldReturnListWithAllElems() throws SQLException {
        var noOfUsers = 3;
        var result = db.findMany("SELECT * FROM users LIMIT ?", USER_MAPPER, noOfUsers);
        assertEquals(noOfUsers, result.size());
    }

    private void initializeDBContents(Connection conn, String... tables) throws SQLException, IOException {
        System.out.println(RESOURCE_DIR.toAbsolutePath().toString());
        for (var table : tables) {
            for (var sqlFile : List.of(table + "_schema.sql", table + "_data.sql")) {
                try (BufferedReader reader = new BufferedReader(new FileReader(RESOURCE_DIR.resolve(sqlFile).toFile()))) {
                    ScriptRunner sr = new ScriptRunner(conn);
                    sr.runScript(reader);
                }
            }
        }
    }

}
