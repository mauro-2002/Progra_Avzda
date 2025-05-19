package org.openapitools.data;

import org.openapitools.model.UsuarioStandar;
import org.openapitools.model.enums.Rol;
import org.openapitools.model.enums.StatusUsuario;
import org.openapitools.repositories.UserRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class TestDataLoader {

    public static Map<String, UsuarioStandar> loadTestData(UserRepository userRespository, MongoTemplate mongoTemplate) {
        var encoder = new BCryptPasswordEncoder();
        return loadTestData(
                List.of(
                        UsuarioStandar.builder().id(UUID.randomUUID().toString()).email("juan@mail.com").nombre("juan").password(encoder.encode("123456789")).rol(Rol.STANDAR).status(StatusUsuario.ACTIVO).build(),
                        UsuarioStandar.builder().id(UUID.randomUUID().toString()).email("maria@mail.com").nombre("maria").password(encoder.encode("987654321")).rol(Rol.STANDAR).status(StatusUsuario.ACTIVO).build()
                ),
                userRespository,
                mongoTemplate
        );
    }

    public static Map<String, UsuarioStandar> loadTestData(Collection<UsuarioStandar> newUsers, UserRepository userRespository, MongoTemplate mongoTemplate) {
        // Borrar datos existentes para asegurar la repetibilidad de las pruebas.
        mongoTemplate.getDb().listCollectionNames()
                .forEach(mongoTemplate::dropCollection);
        return userRespository.saveAll(newUsers).stream().collect(Collectors.toMap(UsuarioStandar::getId, usuario -> usuario));
    }
}
