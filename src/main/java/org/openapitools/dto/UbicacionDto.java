package org.openapitools.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UbicacionDto {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}
