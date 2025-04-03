package org.openapitools.dto.ReporteDTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.openapitools.dto.UbicacionDto;
import org.openapitools.model.enums.StatusReporte;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReporteRequestDto {
    @NotBlank
    @Size(max = 255)
    private String titulo;

    @NotBlank
    private String descripcion;

    @NotNull
    private LocalDateTime fechaCreacion;

    @NotNull
    private UbicacionDto ubicacion;

    @NotNull
    private List<@NotBlank @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$") String> imagenes;

    @NotNull
    private StatusReporte estado = StatusReporte.PENDIENTE;

    private int contImportante = 0;
}

