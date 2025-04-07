package org.openapitools.dto.ReporteDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.openapitools.dto.UbicacionDTO.UbicacionDto;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private LocalDateTime fechaCreacion;

    @NotNull
    private UbicacionDto ubicacion;

    @NotNull
    @Size(min = 1)
    private List<@NotBlank @Pattern(regexp = "^(https?|ftp)://\\S+$") String> imagenes;

    @NotNull
    private StatusReporte estado = StatusReporte.PENDIENTE;

    private int contImportancia = 0;
}

