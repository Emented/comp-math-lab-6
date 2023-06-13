package com.emented.backend.equation;

import java.util.function.BiFunction;

public abstract class Equation {

    public abstract String getTextRepresentstion();

    public abstract long getId();

    public abstract BiFunction<Double, Double, Double> getExactFunction();

    public abstract BiFunction<Double, Double, Double> getInitialFunction();

    public abstract BiFunction<Double, Double, Double> getFunctionForC();

}
