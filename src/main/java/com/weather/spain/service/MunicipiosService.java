package com.weather.spain.service;

import com.weather.spain.service.dto.MunicipioDTO;

import java.io.IOException;
import java.util.List;

public interface MunicipiosService {

    List<MunicipioDTO> getAllMunicipios(String token) throws IOException, InterruptedException;

    List<MunicipioDTO> getAllMunicipiosPopulationSort(String jwt);
}
