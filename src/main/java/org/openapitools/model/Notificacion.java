package org.openapitools.model;

import jakarta.persistence.Id;
import lombok.*;
import org.openapitools.model.enums.TipoNotificacion;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document("notificaciones")
public class Notificacion {

    @Id
    private String id;
    private String idUsuario;
    private String idReporte;
    private TipoNotificacion tipo;
    private String contenido;
    private LocalDateTime fecha;
    private boolean leida;
}
