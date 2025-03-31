package org.openapitools.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class Reporte {
    private String titulo;
    private String descripcion;

    private LocalDate fecha;
    private StatusReporte estado;
    private int contImportante;

    private String idUsuario;

    private List<Comentario> comentarios;
    private List<Categoria> categorias;
}
