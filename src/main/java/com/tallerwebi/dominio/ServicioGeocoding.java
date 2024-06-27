package com.tallerwebi.dominio;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ServicioGeocoding {
    private final String apiKey = "AAIzaSyCDpFxYmSO5R8H2llYsC_G97wfILowgNBo";

    public Coordenadas obtenerCoordenadas(String direccion) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + direccion + "&key=" + apiKey;
        try {
            ResponseEntity<GeocodingResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, GeocodingResponse.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                GeocodingResponse response = responseEntity.getBody();
                if (response != null && response.getResults().size() > 0) {
                    GeocodingResult result = response.getResults().get(0);
                    double latitud = result.getGeometry().getLocation().getLat();
                    double longitud = result.getGeometry().getLocation().getLng();
                    return new Coordenadas(latitud, longitud);
                }
            } else {
                throw new RuntimeException("Error al obtener las coordenadas: " + responseEntity.getStatusCode());
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Error al llamar a la API de geocodificación: " + e.getMessage());
        }
        return null;
    }

    public static class Coordenadas {
        private double latitud;
        private double longitud;

        public Coordenadas(double latitud, double longitud) {
            this.latitud = latitud;
            this.longitud = longitud;
        }

        public double getLatitud() {
            return latitud;
        }

        public double getLongitud() {
            return longitud;
        }
    }
}

class GeocodingResponse {
    private List<GeocodingResult> results;

    public List<GeocodingResult> getResults() {
        return results;
    }
}

class GeocodingResult {
    private Geometry geometry;

    public Geometry getGeometry() {
        return geometry;
    }
}

class Geometry {
    private Location location;

    public Location getLocation() {
        return location;
    }
}

class Location {
    private double lat;
    private double lng;

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}

