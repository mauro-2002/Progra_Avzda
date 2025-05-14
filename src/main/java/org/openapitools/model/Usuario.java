package org.openapitools.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;
import org.openapitools.model.enums.Rol;
import org.openapitools.model.enums.StatusUsuario;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class Usuario {

    @Id
    private String id;
    @Field ("NOMBRE")
    private String nombre;
    @Field ("EMAIL")
    private String email;
    private String ciudad;
    private String direccion;
    @Field("TELEFONO")
    private String telefono;
    private String password;
    @Field ("TIPO")
    private Rol rol;

    @Field("ESTADO_USUARIO")
    private StatusUsuario status;

    @DBRef
    private List<Reporte> reportes;
    @DBRef
    private List<Notificacion> notificaciones;

}
