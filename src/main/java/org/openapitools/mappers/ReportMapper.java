package org.openapitools.mappers;


import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.openapitools.dto.ReporteDTO.ReporteRequestDto;
import org.openapitools.dto.ReporteDTO.ReporteResponseDto;
import org.openapitools.dto.UbicacionDTO.UbicacionDto;
import org.openapitools.model.Reporte;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReportMapper {

    @Mapping(source = "idUsuario", target = "idUsuario", qualifiedByName = "stringToObjectId")
    @Mapping(source = "ubicacion", target = "ubicacion", qualifiedByName = "ubicacionDtoToGeoJson")
    Reporte toEntity(ReporteRequestDto reporteRequestDto);

    @Mapping(source = "idUsuario", target = "idUsuario", qualifiedByName = "objectIdToString")
    @Mapping(source = "ubicacion", target = "ubicacion", qualifiedByName = "geoJsonToUbicacionDto")
    ReporteResponseDto toDto(Reporte reporte);

    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        return id != null ? new ObjectId(id) : null;
    }

    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        return id != null ? id.toString() : null;
    }

    // ✅ Conversión de GeoJsonPoint a UbicacionDto (Corregido)
    @Named("geoJsonToUbicacionDto")
    default UbicacionDto geoJsonToUbicacionDto(GeoJsonPoint geoJsonPoint) {
        if (geoJsonPoint != null && geoJsonPoint.getCoordinates() != null) {
            return new UbicacionDto(
                    geoJsonPoint.getY(), // Latitud
                    geoJsonPoint.getX()  // Longitud
            );
        }
        return null;
    }

    // ✅ Conversión de UbicacionDto a GeoJsonPoint (Corregido)
    @Named("ubicacionDtoToGeoJson")
    default GeoJsonPoint ubicacionDtoToGeoJson(UbicacionDto ubicacionDto) {
        if (ubicacionDto != null) {
            return new GeoJsonPoint(ubicacionDto.getLongitud(), ubicacionDto.getLatitud());
        }
        return null;
    }
}
