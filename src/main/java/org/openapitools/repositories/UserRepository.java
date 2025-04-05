package org.openapitools.repositories;

import org.hibernate.query.Page;
import org.openapitools.dto.UsuarioResponse;
import org.openapitools.model.Usuario;
import org.openapitools.model.enums.StatusUsuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<Usuario, String> {

    Optional<Usuario> findUserByEmail(String email);

    @Query(value = "{ 'estado': { $ne: 'ELIMINADO' }, 'email': ?0 }")
    Optional<Usuario> findExistingUserByEmail(String email);

    //@Query(value = "{ 'estado': { $ne: 'ELIMINADO' }, " +
    //"  'nombre': { $regex: ?0, $options: 'i' }, " +
    //"  'correo': { $regex: ?1, $options: 'i' } }",
    //sort = "{ 'nombre': 1 }")
    //Page<Usuario> findExistingUsersByFilters(String nombre, String correo, Pageable pageable);

    List<UsuarioResponse> findByStatusNot(StatusUsuario status);
}
