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

            while (inputParamsDto.getXn() - currentX >= - 0.0001) {
                xValues.add(currentX);
                yValues.add(currentY);

                double newY = countWithH(equation, currentX, currentY, h);
                double halfHY = countWithH(equation, currentX, currentY, h / 2);

                currentY = newY;

                currentAccuracy = rungeApproximationCounter.calculateApproximation(currentY, halfHY, p);

                currentX = currentX + h;

                if (Double.isNaN(currentY) || !Double.isFinite(currentY)) {
                    throw new RuntimeException("The function suffers a break, change interval");
                }
            }

            h /= 2;
            if (h < 0.000005) {
                throw new RuntimeException("Too small h");
            }

            countOfIteration++;
            if (countOfIteration > 500_000) {
                throw new RuntimeException("Can't solve by 500000 iterations");
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
