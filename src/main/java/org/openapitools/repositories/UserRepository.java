package org.openapitools.repositories;

import org.openapitools.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserRepository extends MongoRepository<Usuario, String> {

    Optional<Usuario> findUserByEmail(String email);

    @Query(value = "{ 'status': { $ne: 'DELETED' }, 'email': ?0 }")
    Optional<Usuario> findExistingUserByEmail(String email);

    @Query(value = "{ 'status': { $ne: 'DELETED' }, " +
            "  'fullName': { $regex: ?0, $options: 'i' }, " +
            "  'email': { $regex: ?1, $options: 'i' }, " +
            "  ?#{ [2] != null ? 'dateBirth' : '_ignore' } : ?2 }",
            sort = "{ 'fullName': 1 }")
    Page<Usuario> findExistingUsersByFilters(String fullName, String email, LocalDateTime dateBirth, Pageable pageable);

    List<UserResponse> findByStatusNot(UserStatus status);
}
