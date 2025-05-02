package org.openapitools.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "categorias")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Builder
public class Categoria {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Indexed(unique = true)
    @EqualsAndHashCode.Include
    @Field("NOMBRE_CATEGORIA")
    private String nombre;

    @Field("DESCRIPCION")
    private String descripcion;
}
