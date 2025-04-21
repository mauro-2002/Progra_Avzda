package org.openapitools.dto.ComentarioDTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class ComentarioResponseDto {

    private String id;
    private String idUsuario;
    private String descripcion;
    private LocalDateTime fechacreacion;

}
