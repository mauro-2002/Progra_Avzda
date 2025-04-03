package org.openapitools.dto.ReporteDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.bson.types.ObjectId;
import org.openapitools.dto.UbicacionDto;
import org.openapitools.model.enums.StatusReporte;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

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

    private String idUsuario;
    private LocalDateTime fecha;
    private UbicacionDto ubicacion;
    private List<String> imageUrl;

    @NotNull
    private StatusReporte estado;
    private int contImportancia;

}
