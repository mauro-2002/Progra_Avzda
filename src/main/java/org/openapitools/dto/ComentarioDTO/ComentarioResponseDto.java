package org.openapitools.dto.ComentarioDTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ComentarioResponseDto {

    private String id;
    private String descripcion;
    private LocalDateTime fechacreacion;
    private String idUsuario;
}
