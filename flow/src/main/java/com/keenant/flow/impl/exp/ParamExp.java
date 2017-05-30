package com.keenant.flow.impl.exp;

/**
 * An expression that is a single parameter/object.
 */
public class ParamExp extends PlainExp {
    public ParamExp(Object param) {
        super("?", param);
    }
}
