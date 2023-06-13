package com.emented.backend.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerDto {
    private List<PointDto> exactPoints;
    private List<PointDto> eulerPoints;

    private String eulerErrorMessage;
    private List<PointDto> modifiedEulerPoints;
    private String modifiedEulerErrorMessage;
    private List<EnhancedPointDto> milnPoints;
    private String milnErrorMessage;
    private List<PointDto> rungeKuttPoints;
    private String rungeKuttErrorMessage;

    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;
}
