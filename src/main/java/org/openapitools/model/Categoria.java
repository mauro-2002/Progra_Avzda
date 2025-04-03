package org.openapitools.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;



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
    private String nombre;

    private String descripcion;
}
