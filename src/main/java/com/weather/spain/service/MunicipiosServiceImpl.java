package com.weather.spain.service;

import com.weather.spain.service.dto.MunicipioDTO;
import com.weather.spain.service.dto.MunicipiosUrlDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;
import static java.net.http.HttpClient.newHttpClient;

@Service
public class MunicipiosServiceImpl implements MunicipiosService{

    private final Logger logger = Logger.getLogger("MunicipiosServiceÎmpl");
    private final ObjectMapper mapper = new ObjectMapper();

    @Value( "${url.municipiosMaestro}" )
    private String urlMaestroMunicipios;

    @Override
    @CircuitBreaker(name="municipiosApi",fallbackMethod = "fallbackMunicipios")
    public List<MunicipioDTO> getAllMunicipios(String token) throws IOException, InterruptedException {
        String urlDatos = getURLDatosMunicipiosAEMET(token);
        return getAllMunicipiosAEMET(token, urlDatos);
    }

    @Override
    public List<MunicipioDTO> getAllMunicipiosPopulationSort(String jwt) {
        String urlDatos = getURLDatosMunicipiosAEMET(jwt);
        return getAllMunicipiosAEMET(jwt, urlDatos).stream()
                .sorted((o1, o2) -> {
                    if (parseInt(o1.getNum_hab()) > parseInt(o2.getNum_hab())) {
                        return -1;
                    } else if (parseInt(o1.getNum_hab()) < parseInt(o2.getNum_hab())) {
                        return 1;
                    }
                    return 0;
                }).toList();
    }

    private List<MunicipioDTO> getAllMunicipiosAEMET(String token,String urlDatos) {
        logger.info("Inicio llamada link municipios AEMET: " +  urlDatos);
        HttpResponse<?> response = invokeAEMETURL(token,urlDatos);
        if (!response.body().toString().contains("503 Service Temporarily Unavailable")) {
            logger.info("FIN llamada link municipios AEMET");
            return List.of(mapper.readValue(response.body().toString(), MunicipioDTO[].class));
        } else {
            throw new RuntimeException("503 Service Temporarily Unavailable");
        }
    }

    private String getURLDatosMunicipiosAEMET(String token){
        HttpResponse<?> response = invokeAEMETURL(token,urlMaestroMunicipios);
        logger.info("Fin llamada AEMET con body: " + response.body());
        MunicipiosUrlDTO municipiosURLDTO = mapper.readValue(response.body().toString(), MunicipiosUrlDTO.class);
        return municipiosURLDTO.datos();
    }

    private HttpResponse<?> invokeAEMETURL(String token,String url){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("api_key", token)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    private String fallbackMunicipios(){
        return "503 Service Temporarily Unavailable";
    }


}
