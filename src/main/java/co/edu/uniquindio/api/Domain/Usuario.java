package co.edu.uniquindio.api.Domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class Usuario {
    private String nombre;
    private String correo;
    private String ciudad;
    private String direccion;
    private String password;
    private Rol rol;

    private StatusUsuario estado;
    private Map<String, Reporte> reportes;

    private List<Notificacion> notificaciones;
    private List<Categoria> preferencias;
}
