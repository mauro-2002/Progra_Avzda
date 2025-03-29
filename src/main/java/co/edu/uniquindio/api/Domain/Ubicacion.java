package co.edu.uniquindio.api.Domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Ubicacion {
    private double latittud;
    private double longitud;
}
