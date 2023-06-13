package com.emented.backend.equation;

import java.util.function.BiFunction;

import org.springframework.stereotype.Component;

@Component
public class SecondEquation extends Equation {

    @Override
    public String getTextRepresentstion() {
        return "y' = x^2 + y";
    }

    @Override
    public long getId() {
        return 2;
    }

    @Override
    public BiFunction<Double, Double, Double> getExactFunction() {
        return (x, c) -> ((-1) * x * x - 2 * x - 2 + c * Math.exp(x));
    }

    @Override
    public BiFunction<Double, Double, Double> getInitialFunction() {
        return (x, y) -> (x * x + y);
    }

    @Override
    public BiFunction<Double, Double, Double> getFunctionForC() {
        return (x, y) -> (y + x * x + 2 * x + 2) / Math.exp(x);
    }
}
