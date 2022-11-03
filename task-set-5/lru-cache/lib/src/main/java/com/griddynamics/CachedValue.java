package com.griddynamics;

import java.util.Date;

public class CachedValue<V> {
    
    private final V value;
    private final Date creationDate;
    private final ValueAcquisitionStatus status;

    /**
     * This class is used to store cache values or provide information about
     * the status of operations associated with obtaining those values.
     * Additionaly, each object stores {@code Date} representing time of
     * obtaining the value. This may have some functionality for particular
     * {@code EvictionStrategy} implementations.
     * @param value to be stored in the cache
     */
    public CachedValue(V value) {
        this.value = value;
        creationDate = new Date();
        status = ValueAcquisitionStatus.OK;
    }

    private CachedValue(ValueAcquisitionStatus failureStatus) {
        value = null;
        creationDate = new Date();
        status = failureStatus;
    }

    /**
     * Factory method used to mark an entry in the cache with
     * an attempt to obtain a value which failed due to a computation error.
     * @return {@code CachedValue} instance with no value and the status of
     * {@code FAILED_TO_COMPUTE}
     */
    public static CachedValue<Object> failedToCompute() {
        return new CachedValue<>(ValueAcquisitionStatus.FAILED_TO_COMPUTE);
    }

    /**
     * Allows obtaining raw value if the value acquisition status allows for it.
     * Otherwise an exception is thrown.
     * @return value of type {@code V} stored in this {@code CachedValue}
     */
    public V unwrap() {
        if (!status.equals(ValueAcquisitionStatus.OK)) {
            throw new IllegalStateException("Tried to unwrap value whose status is " + status.name());
        }
        return value;
    }

    /**
     * Allows for checking for the date when the value was obtained or failed. 
     * @return {@code Date} object representing date of this object's creation
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Provides information on the status of obtaining the value.
     * @return status information of type {@code ValueStatus}
     */
    public ValueAcquisitionStatus getValueStatus() {
        return status;
    }

}
