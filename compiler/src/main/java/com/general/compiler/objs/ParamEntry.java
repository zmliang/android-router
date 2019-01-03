package com.general.compiler.objs;

import com.squareup.javapoet.TypeName;

/**
 * Author: zml
 * Date  : 2019/1/3
 **/
public class ParamEntry {
    private String name;
    private TypeName type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeName getType() {
        return type;
    }

    public void setType(TypeName type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ParamEntry{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
