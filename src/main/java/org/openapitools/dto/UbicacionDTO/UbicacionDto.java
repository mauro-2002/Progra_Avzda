package org.openapitools.dto.UbicacionDTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UbicacionDto {

    @NotNull
    private Double latitud;

    @NotNull
    private Double longitud;
}
