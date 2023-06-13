package com.emented.backend.equation;

import java.util.function.BiFunction;

import org.springframework.stereotype.Component;

@Component
public class FirstEquation extends Equation {

    @Override
    public String getTextRepresentstion() {
        return "y' = x + y";
    }

    @Override
    public long getId() {
        return 1;
    }

    @Override
    public BiFunction<Double, Double, Double> getExactFunction() {
        return (x, c) -> ((-1) * x - 1 + c * Math.exp(x));
    }

    @Override
    public BiFunction<Double, Double, Double> getInitialFunction() {
        return Double::sum;
    }

    @Override
    public BiFunction<Double, Double, Double> getFunctionForC() {
        return (x, y) -> (y + x + 1) * Math.exp(-x);
    }
}
