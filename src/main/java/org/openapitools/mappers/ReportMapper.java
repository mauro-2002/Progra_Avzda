package org.openapitools.mappers;


import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.openapitools.dto.ReporteDTO.ReporteRequestDto;
import org.openapitools.dto.ReporteDTO.ReporteResponseDto;
import org.openapitools.model.Reporte;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReportMapper {

    @Mapping(source = "idUsuario", target = "idUsuario", qualifiedByName = "stringToObjectId")
    Reporte toEntity(ReporteRequestDto reporteRequestDto);

    @Mapping(source = "idUsuario", target = "idUsuario", qualifiedByName = "objectIdToString")
    ReporteResponseDto toDto(Reporte reporte);

    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        return id != null ? new ObjectId(id) : null;
    }

    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        return id != null ? id.toString() : null;
    }
}
