package com.emented.backend.math;

import java.util.ArrayList;
import java.util.List;

import com.emented.backend.dto.InputParamsDto;
import com.emented.backend.dto.PointDto;
import com.emented.backend.equation.Equation;
import org.springframework.stereotype.Component;

@Component
public class ExactValuesCounter {

    private final RangeCounter rangeCounter;

    public ExactValuesCounter(RangeCounter rangeCounter) {
        this.rangeCounter = rangeCounter;
    }

    public List<PointDto> countExactPoints(InputParamsDto input, Equation equation) {

        double c = equation.getFunctionForC().apply(input.getX0(), input.getY0());

        List<PointDto> exactPoints = new ArrayList<>();

        for (Double x : rangeCounter.countRanges(input.getX0(), input.getXn(), input.getH())) {
            double y = equation.getExactFunction().apply(x, c);

            exactPoints.add(new PointDto(x, y));
        }

        return exactPoints;
    }

}
