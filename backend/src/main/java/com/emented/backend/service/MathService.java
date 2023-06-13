package com.emented.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.emented.backend.dto.AnswerDto;
import com.emented.backend.dto.EnhancedPointDto;
import com.emented.backend.dto.InputParamsDto;
import com.emented.backend.dto.PointDto;
import com.emented.backend.equation.Equation;
import com.emented.backend.math.ExactValuesCounter;
import com.emented.backend.methods.EulerMethod;
import com.emented.backend.methods.MilnMethod;
import com.emented.backend.methods.ModifiedEulerMethod;
import com.emented.backend.methods.RungeKuttMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MathService {

    private static final double E = 0.0001;

    private final Map<Long, Equation> equationMap;
    private final EulerMethod eulerMethod;
    private final ModifiedEulerMethod modifiedEulerMethod;
    private final RungeKuttMethod rungeKuttMethod;
    private final MilnMethod milnMethod;
    private final ExactValuesCounter exactValuesCounter;

    @Autowired
    public MathService(List<Equation> equationList,
                       EulerMethod eulerMethod,
                       ModifiedEulerMethod modifiedEulerMethod,
                       RungeKuttMethod rungeKuttMethod,
                       MilnMethod milnMethod, ExactValuesCounter exactValuesCounter) {
        this.equationMap = equationList.stream().collect(Collectors.toMap(Equation::getId, Function.identity()));
        this.eulerMethod = eulerMethod;
        this.modifiedEulerMethod = modifiedEulerMethod;
        this.rungeKuttMethod = rungeKuttMethod;
        this.milnMethod = milnMethod;
        this.exactValuesCounter = exactValuesCounter;
    }

    public AnswerDto solve(InputParamsDto inputParamsDto) {

        Equation equation = equationMap.get(inputParamsDto.getFunctionId());

        List<PointDto> exactPoints = exactValuesCounter.countExactPoints(inputParamsDto, equation);

        List<PointDto> eulerPoints = null;
        String eulerErrorMessage = null;

        try {
            List<PointDto> sourceEulerPoints = eulerMethod.solve(equation, inputParamsDto);
            eulerPoints = new ArrayList<>();

            if (sourceEulerPoints.size() == exactPoints.size()) {
                eulerPoints = sourceEulerPoints;
            } else {
                for (PointDto exactPoint : exactPoints) {
                    var newPoint = sourceEulerPoints
                            .stream()
                            .dropWhile((point) -> Math.abs(exactPoint.getX() - point.getX()) > E)
                            .findFirst();

                    if (newPoint.isPresent()) {
                        eulerPoints.add(newPoint.get());
                    }
                }

                if (eulerPoints.size() < exactPoints.size()) {
                    eulerPoints.add(sourceEulerPoints.get(sourceEulerPoints.size() - 1));
                }
//                int diff = (int) Math.ceil((double) sourceEulerPoints.size() / ((double) exactPoints.size() - 1));
//
//                eulerPoints.add(sourceEulerPoints.get(0));
//
//                for (int i = diff; i < sourceEulerPoints.size(); i += diff) {
//                    eulerPoints.add(sourceEulerPoints.get(i));
//                }
//
//                eulerPoints.add(sourceEulerPoints.get(sourceEulerPoints.size() - 1));
            }
        } catch (Exception e) {
            eulerErrorMessage = e.getMessage();
        }

        List<PointDto> modifiedEulerPoints = null;
        String modifiedEulerErrorMessage = null;

        try {
            List<PointDto> sourceModifiedEulerPoints = modifiedEulerMethod.solve(equation, inputParamsDto);
            modifiedEulerPoints = new ArrayList<>();

            if (sourceModifiedEulerPoints.size() == exactPoints.size()) {
                modifiedEulerPoints = sourceModifiedEulerPoints;
            } else {
                for (PointDto exactPoint : exactPoints) {
                    var newPoint = sourceModifiedEulerPoints
                            .stream()
                            .dropWhile((point) -> Math.abs(exactPoint.getX() - point.getX()) > E)
                            .findFirst();

                    if (newPoint.isPresent()) {
                        modifiedEulerPoints.add(newPoint.get());
                    }
                }

                if (modifiedEulerPoints.size() < exactPoints.size()) {
                    modifiedEulerPoints.add(sourceModifiedEulerPoints.get(sourceModifiedEulerPoints.size() - 1));
                }
//                int diff = (int) Math.ceil((double) sourceModifiedEulerPoints.size() / ((double) exactPoints.size() - 1));
//
//                modifiedEulerPoints.add(sourceModifiedEulerPoints.get(0));
//
//                for (int i = diff; i < sourceModifiedEulerPoints.size(); i += diff) {
//                    modifiedEulerPoints.add(sourceModifiedEulerPoints.get(i));
//                }
//
//                modifiedEulerPoints.add(sourceModifiedEulerPoints.get(sourceModifiedEulerPoints.size() - 1));
            }
        } catch (Exception e) {
            modifiedEulerErrorMessage = e.getMessage();
        }

        List<PointDto> rungeKuttPoints = null;
        String rungeKuttErrorMessage = null;

        try {
            List<PointDto> sourceRungeKuttPoints = rungeKuttMethod.solve(equation, inputParamsDto);
            rungeKuttPoints = new ArrayList<>();

            if (sourceRungeKuttPoints.size() == exactPoints.size()) {
                rungeKuttPoints = sourceRungeKuttPoints;
            } else {
                for (PointDto exactPoint : exactPoints) {
                    var newPoint = sourceRungeKuttPoints
                            .stream()
                            .dropWhile((point) -> Math.abs(exactPoint.getX() - point.getX()) > E)
                            .findFirst();

                    if (newPoint.isPresent()) {
                        rungeKuttPoints.add(newPoint.get());
                    }
                }

                if (rungeKuttPoints.size() < exactPoints.size()) {
                    rungeKuttPoints.add(sourceRungeKuttPoints.get(sourceRungeKuttPoints.size() - 1));
                }
//                int diff = (int) Math.ceil((double) sourceRungeKuttPoints.size() / ((double) exactPoints.size() - 1));
//
//                rungeKuttPoints.add(sourceRungeKuttPoints.get(0));
//
//                for (int i = diff; i < sourceRungeKuttPoints.size(); i += diff) {
//                    rungeKuttPoints.add(sourceRungeKuttPoints.get(i));
//                }
//
//                rungeKuttPoints.add(sourceRungeKuttPoints.get(sourceRungeKuttPoints.size() - 1));
            }

        } catch (Exception e) {
            rungeKuttErrorMessage = e.getMessage();
        }

        List<EnhancedPointDto> enhancedMilnPoints = null;
        String milnErrorMessage = null;
        try {
            List<PointDto> milnPoints = milnMethod.solve(equation, inputParamsDto, rungeKuttPoints);
            enhancedMilnPoints = new ArrayList<>(milnPoints.size());

            for (int i = 0; i < milnPoints.size(); i++) {
                PointDto milnPointDto = milnPoints.get(i);
                PointDto exactPointDto = exactPoints.get(i);

                enhancedMilnPoints.add(new EnhancedPointDto(milnPointDto.getX(),
                        milnPointDto.getY(),
                        Math.abs(milnPointDto.getY() - exactPointDto.getY())));
            }
        } catch (Exception e) {
            milnErrorMessage = e.getMessage();
        }

        double xMin = Double.MAX_VALUE;
        double xMax = Double.MIN_VALUE;

        double yMin = Double.MAX_VALUE;
        double yMax = Double.MIN_VALUE;

        for (PointDto pointDto : exactPoints) {
            if (Double.isFinite(pointDto.getX())) {
                xMin = Math.min(xMin, pointDto.getX());
                xMax = Math.max(xMax, pointDto.getX());
            }

            if (Double.isFinite(pointDto.getY())) {
                yMin = Math.min(yMin, pointDto.getY());
                yMax = Math.max(yMax, pointDto.getY());
            }
        }


        return AnswerDto.builder()
                .exactPoints(exactPoints)
                .eulerPoints(eulerPoints)
                .eulerErrorMessage(eulerErrorMessage)
                .modifiedEulerPoints(modifiedEulerPoints)
                .modifiedEulerErrorMessage(modifiedEulerErrorMessage)
                .rungeKuttPoints(rungeKuttPoints)
                .rungeKuttErrorMessage(rungeKuttErrorMessage)
                .milnPoints(enhancedMilnPoints)
                .milnErrorMessage(milnErrorMessage)
                .xMin(xMin)
                .xMax(xMax)
                .yMin(yMin)
                .yMax(yMax)
                .build();
    }
}
