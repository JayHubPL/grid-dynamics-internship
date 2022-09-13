package com.griddynamics;

import java.util.Iterator;
import java.util.function.BiFunction;

public class ZippingIterator<A, B, C> implements Iterator<C> {
 
    private final Iterator<A> iter1;
    private final Iterator<B> iter2;
    private final BiFunction<A, B, C> zippingFunction;

    public ZippingIterator(Iterator<A> iter1, Iterator<B> iter2, BiFunction<A, B, C> zippingFunction) {
        if (iter1 == null) {
            throw new IllegalArgumentException("iter1 cannot be null");
        }
        if (iter2 == null) {
            throw new IllegalArgumentException("iter2 cannot be null");
        }
        if (zippingFunction == null) {
            throw new IllegalArgumentException("zippingFunction cannot be null");
        }
        this.iter1 = iter1;
        this.iter2 = iter2;
        this.zippingFunction = zippingFunction;
    }

    @Override
    public boolean hasNext() {
        return iter1.hasNext() && iter2.hasNext();
    }

    @Override
    public C next() {
        return zippingFunction.apply(iter1.next(), iter2.next());
    }

}
