package com.emented.backend.methods;

import java.util.ArrayList;
import java.util.List;

import com.emented.backend.dto.InputParamsDto;
import com.emented.backend.dto.PointDto;
import com.emented.backend.equation.Equation;
import com.emented.backend.math.RungeApproximationCounter;

public abstract class OneStepMethod {

    private final int p;
    private final RungeApproximationCounter rungeApproximationCounter;

    protected OneStepMethod(int p, RungeApproximationCounter rungeApproximationCounter) {
        this.p = p;
        this.rungeApproximationCounter = rungeApproximationCounter;
    }

    public List<PointDto> solve(Equation equation, InputParamsDto inputParamsDto) {
        double currentAccuracy = 1.0;
        double h = inputParamsDto.getH();
        List<Double> xValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();
        long countOfIteration = 0;

        while (currentAccuracy - inputParamsDto.getE() > 0) {
            xValues = new ArrayList<>();
            yValues = new ArrayList<>();
            double currentX = inputParamsDto.getX0();
            double currentY = inputParamsDto.getY0();

            while (currentX <= inputParamsDto.getXn()) {
                xValues.add(currentX);
                yValues.add(currentY);

                currentY = countWithH(equation, currentX, currentY, h);
                double halfHY = countWithH(equation, currentX, currentY, h / 2);

                currentAccuracy = rungeApproximationCounter.calculateApproximation(currentY, halfHY, p);

                currentX = currentX + h;
            }

            h /= 2;
            if (h < 0.0001) {
                throw new RuntimeException("Too small h");
            }

            countOfIteration++;
            if (countOfIteration > 10000) {
                throw new RuntimeException("Can't solve by 2 seconds");
            }
        }

        List<PointDto> answer = new ArrayList<>(xValues.size());
        for (int i = 0; i < xValues.size(); i++) {
            answer.add(new PointDto(xValues.get(i), yValues.get(i)));
        }

        return answer;
    }
    protected abstract double countWithH(Equation equation, double currentX, double currentY, double h);

}
