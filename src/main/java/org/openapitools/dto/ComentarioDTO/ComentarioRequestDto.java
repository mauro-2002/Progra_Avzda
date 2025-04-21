package org.openapitools.dto.ComentarioDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class ComentarioRequestDto {

    @NotBlank
    private String idUsuario;

    @NotBlank
    private String descripcion;

}
