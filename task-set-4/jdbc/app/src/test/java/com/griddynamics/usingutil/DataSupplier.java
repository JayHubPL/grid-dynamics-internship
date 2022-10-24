package com.griddynamics.usingutil;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface DataSupplier<T> extends Iterator<T> {

    public static final Date DATE_EXAMPLE = Date.valueOf("2022-10-19");

    @Override
    default public boolean hasNext() {
        return true;
    }

    @Override
    public T next();

    default public List<T> getDataList(int n) {
        List<T> l = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            l.add(next());
        }
        return l;
    }
    
}
