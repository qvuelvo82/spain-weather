package com.weather.spain.controller;


import com.weather.spain.service.MunicipiosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class Controller {


    @Autowired
    MunicipiosService municipiosService;

    @GetMapping(value = "/municipios")
    public String findAllMunicipios() throws IOException, InterruptedException {
        return municipiosService.getAllMunicipios();
    }



}
