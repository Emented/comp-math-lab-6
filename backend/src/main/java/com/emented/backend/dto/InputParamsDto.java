package com.emented.backend.dto;

import lombok.Data;

@Data
public class InputParamsDto {

    private long functionId;
    private double x0;
    private double y0;
    private double xn;
    private double h;
    private double e;
}
