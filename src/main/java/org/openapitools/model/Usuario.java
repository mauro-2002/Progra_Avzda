package org.openapitools.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import org.openapitools.model.enums.Rol;
import org.openapitools.model.enums.StatusUsuario;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Table
@Document("usuarios")
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
    private Map<String, Reporte> reportes;

    private List<Notificacion> notificaciones;
    private List<Categoria> preferencias;
}
