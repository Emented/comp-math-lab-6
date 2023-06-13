package com.emented.backend.methods;

import java.util.ArrayList;
import java.util.List;

import com.emented.backend.dto.InputParamsDto;
import com.emented.backend.dto.PointDto;
import com.emented.backend.equation.Equation;
import com.emented.backend.math.RangeCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MilnMethod {

    private final RangeCounter rangeCounter;

    @Autowired
    public MilnMethod(RangeCounter rangeCounter) {
        this.rangeCounter = rangeCounter;
    }

    public List<PointDto> solve(Equation equation, InputParamsDto inputParamsDto, List<PointDto> rungePoints) {
        List<Double> xList = rangeCounter.countRanges(inputParamsDto.getX0(),
                inputParamsDto.getXn(),
                inputParamsDto.getH());

        if (xList.size() < 4) {
            throw new RuntimeException("For Milan differentiation num of intervals has to be at least 4");
        }

        if (rungePoints == null || rungePoints.size() < 4) {
            throw new RuntimeException("For Milan differentiation Runge-Kutt method should contain at least 4 points");
        }

        List<Double> yList = new ArrayList<>();
        List<PointDto> answer = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            answer.add(rungePoints.get(i));
            yList.add(rungePoints.get(i).getY());
        }

        for (int i = 4; i < xList.size(); i++) {
            double predict = getPrediction(inputParamsDto.getH(), equation, xList, yList, i);
            double correction = getCorrection(inputParamsDto.getH(), equation, xList, yList, i, predict);

            while (Math.abs(correction - predict) > inputParamsDto.getE()) {
                predict = correction;
                correction = getCorrection(inputParamsDto.getH(), equation, xList, yList, i, predict);
            }

            yList.add(correction);
            answer.add(new PointDto(xList.get(i), correction));
        }

        return answer;
    }

    private double getPrediction(double step,
                                 Equation equation,
                                 List<Double> xList,
                                 List<Double> yList,
                                 int i) {
        double tmp = 2 * equation.getInitialFunction().apply(xList.get(i - 3),
                yList.get(i - 3)) - equation.getInitialFunction().apply(xList.get(i - 2),
                yList.get(i - 2)) + 2 * equation.getInitialFunction().apply(xList.get(i - 1),
                yList.get(i - 1));

        return yList.get(i - 4) + 4 * step * tmp / 3;
    }

    private double getCorrection(double step,
                                 Equation equation,
                                 List<Double> xList,
                                 List<Double> yList,
                                 int i,
                                 double prediction) {
        double tmp = equation.getInitialFunction().apply(xList.get(i - 2),
                yList.get(i - 2)) + 4 * equation.getInitialFunction().apply(xList.get(i - 1),
                yList.get(i - 1)) + equation.getInitialFunction().apply(xList.get(i),
                prediction);

        return yList.get(i - 2) + step * tmp / 3;
    }
}
