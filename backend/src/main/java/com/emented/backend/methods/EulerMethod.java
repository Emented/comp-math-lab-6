package com.emented.backend.methods;

import com.emented.backend.equation.Equation;
import com.emented.backend.math.RungeApproximationCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EulerMethod extends OneStepMethod {

    @Autowired
    public EulerMethod(RungeApproximationCounter rungeApproximationCounter) {
        super(1, rungeApproximationCounter);
    }

    @Override
    protected double countWithH(Equation equation, double currentX, double currentY, double h) {
        double tmp = equation.getInitialFunction().apply(currentX, currentY);

        return currentY + h * tmp;
    }
}
