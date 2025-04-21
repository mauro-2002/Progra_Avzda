package org.openapitools.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.openapitools.model.enums.StatusReporte;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;



@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document("reportes")
public class Reporte {

    @EqualsAndHashCode.Include
    @Id
    private String idReporte;
    private String titulo;
    private String descripcion;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint ubicacion;

    @Field("fecha_reporte")
    private LocalDateTime fechaCreacion;
    private StatusReporte estado;
    private int contImportancia;

    @DBRef
    private ObjectId idUsuario;
    private List<String> imagesUrl;

    @DBRef
    private List<Comentario> comentarios;

    @DBRef
    private List<Categoria> categorias;

}
