package com.emented.backend.equation;

import java.util.function.BiFunction;

import org.springframework.stereotype.Component;

@Component
public class ThirdEquation extends Equation {

    @Override
    public String getTextRepresentstion() {
        return "y' = y + (1 + x) * y^2";
    }

    @Override
    public long getId() {
        return 3;
    }

    @Override
    public BiFunction<Double, Double, Double> getExactFunction() {
        return (x, c) -> (-(Math.exp(x)) / (c + Math.exp(x) * x));
    }

    @Override
    public BiFunction<Double, Double, Double> getInitialFunction() {
        return (x, y) -> (y + (1 + x) * y * y);
    }

    @Override
    public BiFunction<Double, Double, Double> getFunctionForC() {
        return (x, y) -> (-Math.exp(x) - y * Math.exp(x) * x) / y;
    }
}
