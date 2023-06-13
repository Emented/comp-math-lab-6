package com.emented.backend.methods;

import com.emented.backend.equation.Equation;
import com.emented.backend.math.RungeApproximationCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModifiedEulerMethod extends OneStepMethod {

    @Autowired
    public ModifiedEulerMethod(RungeApproximationCounter rungeApproximationCounter) {
        super(2, rungeApproximationCounter);
    }

    @Override
    protected double countWithH(Equation equation, double currentX, double currentY, double h) {

        double tmp = equation.getInitialFunction().apply(currentX,
                currentY) + equation.getInitialFunction().apply(currentX + h,
                currentY + h * equation.getInitialFunction().apply(currentX,
                        currentY));

        return currentY + (h / 2) * tmp;

    }
}
