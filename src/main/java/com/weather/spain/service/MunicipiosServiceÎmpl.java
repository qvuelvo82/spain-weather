package com.weather.spain.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

@Service
public class MunicipiosServiceÎmpl implements MunicipiosService{

    Logger logger = Logger.getLogger("MunicipiosServiceÎmpl");

    @Value( "${token.AEMET}" )
    private String bearerToken;

    @Override
    public String getAllMunicipios() throws IOException, InterruptedException {

        HttpRequest requestLink = HttpRequest.newBuilder()
                .uri(URI.create("https://opendata.aemet.es/opendata/api/maestro/municipios"))
                .header("api_key", bearerToken)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        logger.info("Inicio llamada AEMET...");
        HttpResponse<String> response = HttpClient.newHttpClient().send(requestLink, HttpResponse.BodyHandlers.ofString());
        logger.info("Fin llamada AEMET con body: " + response.body());

        ObjectMapper mapper = new ObjectMapper();
        MunicipiosDTO municipiosDTO = mapper.readValue(response.body(), MunicipiosDTO.class);

        logger.info("Inicio llamada link municipios AEMET: " +  municipiosDTO.datos());
        HttpRequest requestMunicipios = HttpRequest.newBuilder()
                .uri(URI.create(municipiosDTO.datos()))
                .header("api_key", bearerToken)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        response = HttpClient.newHttpClient().send(requestMunicipios,HttpResponse.BodyHandlers.ofString());
        logger.info("FIN llamada link municipios AEMET");



        return response.body();
    }
}
