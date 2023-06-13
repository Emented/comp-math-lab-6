package com.emented.backend.methods;

import com.emented.backend.equation.Equation;
import com.emented.backend.math.RungeApproximationCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RungeKuttMethod extends OneStepMethod {

    @Autowired
    public RungeKuttMethod(RungeApproximationCounter rungeApproximationCounter) {
        super(4, rungeApproximationCounter);
    }

    @Override
    protected double countWithH(Equation equation, double currentX, double currentY, double h) {

            double k1 = h * equation.getInitialFunction().apply(currentX, currentY);
            double k2 = h * equation.getInitialFunction().apply(currentX + h / 2, currentY + k1 / 2);
            double k3 = h * equation.getInitialFunction().apply(currentX + h / 2, currentY + k2 / 2);
            double k4 = h * equation.getInitialFunction().apply(currentX + h, currentY + k3);

            return currentY + (k1 + 2 * k2 + 2 * k3 + k4) / 6;

    }
}
