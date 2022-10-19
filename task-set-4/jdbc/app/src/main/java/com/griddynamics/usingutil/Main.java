package com.griddynamics.usingutil;

import java.net.InetAddress;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Iterator;

import com.griddynamics.jdbcutil.ConnectionAttributes;
import com.griddynamics.jdbcutil.DatabaseConnectionProvider;
import com.griddynamics.jdbcutil.DatabaseHandler;
import com.griddynamics.jdbcutil.PGDataSource;
import com.griddynamics.usingutil.actor.Actor;
import com.griddynamics.usingutil.actor.ActorDAO;

public class Main {
    
    public static final String DATABASE_NAME = "users";
    public static final String USER = "postgres";
    public static final String PASSWORD = "docker";
    public static final int PORT = 5432;
    public static final InetAddress IP = InetAddress.getLoopbackAddress();

    public static void main(String[] args) throws SQLException {
        ConnectionAttributes connectionAttributes = new ConnectionAttributes(IP, PORT, DATABASE_NAME, USER, PASSWORD);
        DatabaseConnectionProvider connectionProvider = new PGDataSource(connectionAttributes);
        try (DatabaseHandler db = new DatabaseHandler(connectionProvider)) {
            db.execute("""
                    DROP TABLE IF EXISTS actors
                    """);
            db.execute("""
                    CREATE TABLE IF NOT EXISTS actors
                        ( id varchar(50) UNIQUE NOT NULL
                        , name varchar(50) NOT NULL
                        , birthDate date NOT NULL )
                    """);
            ActorDAO actorDAO = new ActorDAO(db, 3);
            for (int i = 0; i < 100; i++) {
                actorDAO.save(new Actor(Integer.toString(i), "A", Date.valueOf(LocalDate.now())));
            }
            Iterator<Actor> iter = actorDAO.scan();
            while (iter.hasNext()) {
                System.out.println(iter.next());
            }
        }
    }

}
