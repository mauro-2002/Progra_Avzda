package org.openapitools.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.openapitools.model.enums.StatusReporte;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
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
    private String id;
    private String titulo;
    private String descripcion;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint ubicacion;

    private LocalDateTime fecha;
    private StatusReporte estado;
    private int contImportancia;
    private ObjectId idUsuario;
    private List<String> imageUrl;

    @DBRef
    private List<Comentario> comentarios;

    @DBRef
    private List<Categoria> categorias;

}
