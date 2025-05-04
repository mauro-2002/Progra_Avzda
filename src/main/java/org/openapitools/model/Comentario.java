package org.openapitools.model;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comentario {

    @Id
    @EqualsAndHashCode.Include
    private String id;
    @Field("ID_USUARIO")
    private String idUsuario;
    @Field("DESCRIPCION")
    private String descripcion;
    @Field("FECHA")
    private LocalDateTime fechaCreacion;

}
