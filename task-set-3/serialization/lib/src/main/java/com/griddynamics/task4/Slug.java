package com.griddynamics.task4;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

@DefaultSerializer(TaggedFieldSerializer.class)
/*
 * Default serializer for the class can be specified using annotation instead
 * of providing it while registering class.
 */
public class Slug {
    /*
     * No interface is mandatory. There is KryoSerializable which provides
     * simmilar functionality to Externalizable.
     */
    
    @Tag(1)
    /*
     * Tags give every tagged field a uniqe identifier which allows
     * for change in the name of the field since the field is no longer
     * identified by it but rather by it's tag number.
     */
    private final String name;
    /*
     * No getters or setters are required for the (de)serializer.
     * Final fields are no problem either.
     */
    @Tag(2)
    private Shell homeShell;

    public Slug(String name) {
        this.name = name;
    }

    public void setHomeShell(Shell homeShell) {
        this.homeShell = homeShell;
    }

    @Override
    public String toString() {
        return String.format("I am %s living in this %s shell.", name, homeShell.color());
    }

}
