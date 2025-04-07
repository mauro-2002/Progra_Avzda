package org.openapitools.dto.ReporteDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.openapitools.dto.UbicacionDTO.UbicacionDto;
import org.openapitools.model.enums.StatusReporte;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReporteResponseDto {

    @NotBlank
    private String id;

    @NotBlank
    @Size(min = 5, max = 50)
    private String titulo;

    @NotBlank
    private String descripcion;

    @NotNull
    @NotBlank
    private String idUsuario;

    @NotNull
    private LocalDateTime fechaCreacion;

    @NotNull
    private UbicacionDto ubicacion;
    private List<@Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$") String> imagenes;

    @NotNull
    private StatusReporte estado;
    private int contImportancia;

}
