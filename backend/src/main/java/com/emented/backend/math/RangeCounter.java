package com.emented.backend.math;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class RangeCounter {

    public List<Double> countRanges(double x0, double xn, double step) {
        List<Double> xs = new ArrayList<>();

        int n = countN(x0, xn, step);

        for (int i = 0; i < n; i++) {
            xs.add(x0);
            x0 += step;
        }

        return xs;
    }

    public int countN(double x0, double xn, double step) {
        return (int) Math.floor((xn - x0) / step) + 1;
    }

}
