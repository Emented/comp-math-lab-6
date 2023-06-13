package com.emented.backend.controller;


import com.emented.backend.dto.AnswerDto;
import com.emented.backend.dto.InputParamsDto;
import com.emented.backend.service.MathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@CrossOrigin
public class MathController {

    private final MathService service;

    @Autowired
    public MathController(MathService service) {
        this.service = service;
    }

    @PostMapping("/submit")
    public AnswerDto solve(@RequestBody InputParamsDto inputParamsDto) {
        return service.solve(inputParamsDto);
    }
}
