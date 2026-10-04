package com.weather.spain.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

public interface MunicipiosService {

    String getAllMunicipios() throws IOException, InterruptedException;

}
