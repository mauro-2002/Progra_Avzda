package org.openapitools.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class Notificacion {
    private String mensaje;
    private LocalDate fecha;

}
