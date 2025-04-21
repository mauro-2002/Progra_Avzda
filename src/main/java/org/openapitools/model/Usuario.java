package org.openapitools.model;

import lombok.*;
import jakarta.persistence.*;
import org.openapitools.model.enums.Rol;
import org.openapitools.model.enums.StatusUsuario;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Map;



@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("usuarios")
@Data
public class Usuario {

    @Id
    private String id;
    private String nombre;
    private String email;
    private String ciudad;
    private String direccion;
    private String telefono;
    private String password;
    private Rol rol;

    private StatusUsuario status;

    @DBRef
    private List<Reporte> reportes;

    @DBRef
    private List<Notificacion> notificaciones;

    @DBRef
    private List<Categoria> preferencias;
}
