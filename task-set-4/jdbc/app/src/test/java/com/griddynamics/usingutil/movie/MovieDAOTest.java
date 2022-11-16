package com.griddynamics.usingutil.movie;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import com.griddynamics.usingutil.AbstractDAOTest;
import com.griddynamics.usingutil.DataSupplier;

public class MovieDAOTest extends AbstractDAOTest<Movie, String> {

    private static Supplier<String> idSupplier = new Supplier<String>() {

        @Override
        public String get() {
            return "ID";
        }
        
    };

    protected MovieDAOTest()
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        super(MovieDAO.class, MovieSupplier.class, "movies", MovieDAO.MOVIE_MAPPER, idSupplier);
    }

    public static class MovieSupplier implements DataSupplier<Movie> {

        private int id = 0;
    
        @Override
        public Movie next() {
            id++;
            return new Movie(Integer.toString(id), "Title" + id, DataSupplier.DATE_EXAMPLE);
        }
        
    }
    
}
