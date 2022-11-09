package com.griddynamics.usingutil.actor;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import com.griddynamics.usingutil.AbstractDAOTest;
import com.griddynamics.usingutil.DataSupplier;

public class ActorDAOTest extends AbstractDAOTest<Actor, String> {

    private static Supplier<String> idSupplier = new Supplier<String>() {

        @Override
        public String get() {
            return "ID";
        }
        
    };

    protected ActorDAOTest()
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        super(ActorDAO.class, ActorSupplier.class, "actors", ActorDAO.ACTOR_MAPPER, idSupplier);
    }

    public static class ActorSupplier implements DataSupplier<Actor> {

        private int id = 0;
    
        @Override
        public Actor next() {
            id++;
            return new Actor(Integer.toString(id), "Name" + id, DataSupplier.DATE_EXAMPLE);
        }
        
    }

}
