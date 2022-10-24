package com.griddynamics.usingutil.movie;

import java.lang.reflect.InvocationTargetException;

import com.griddynamics.usingutil.AbstractDAOTest;
import com.griddynamics.usingutil.DataSupplier;

public class MovieDAOTest extends AbstractDAOTest<Movie> {

    protected MovieDAOTest()
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        super(MovieDAO.class, MovieSupplier.class, "movies", MovieDAO.MOVIE_MAPPER);
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
