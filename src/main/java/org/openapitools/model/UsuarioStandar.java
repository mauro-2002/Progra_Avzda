package org.openapitools.model;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@Table
@Document("usuarios")
public class UsuarioStandar extends Usuario {

    private String codigoActivacion;
    private LocalDateTime expiracionCodigo;
    @DBRef
    private List<Categoria> preferencias;
}
