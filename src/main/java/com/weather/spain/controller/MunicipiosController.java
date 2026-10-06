package com.weather.spain.controller;


import com.weather.spain.service.MunicipiosService;
import com.weather.spain.service.dto.MunicipioDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class MunicipiosController {


    @Autowired
    MunicipiosService municipiosService;

    @GetMapping(value = "/municipios")
    public List<MunicipioDTO> findAllMunicipios(HttpServletRequest request) throws IOException, InterruptedException {
        String jwt = request.getHeader("Authorization").substring(7);
        return municipiosService.getAllMunicipios(jwt);
    }



}
