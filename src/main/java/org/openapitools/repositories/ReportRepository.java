package org.openapitools.repositories;

import org.openapitools.model.Reporte;
import org.openapitools.model.enums.StatusReporte;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends MongoRepository<Reporte, String> {

    @Query("{ 'idUsuario' : ?0, 'estado' : ?1 }")
    List<Reporte> findByUserIdAndStatus(String idUsuario, StatusReporte estado); //Reportes por categoría y estado
    @Query("{ 'contImportante' : { $gte : ?0 } }")
    List<Reporte> findByRatingsImportantGreaterThanOrderByDateDesc(int contImportante); //Reportes más importantes basado en el númoero de "me gustas"

    List<Reporte> findByUserIdOrderByDateDesc(String userId); //Reportes por usuario específico

    @Query("{ 'contImportancia' : { $gte : ?0, $lte: ?1 } }")
    List<Reporte> findByContImportanciaBetweenImportantBetween(int min, int max); //Reportes con nivel de importancia

    @Query("{ 'fecha' : { $gte : ?0, $lte: ?1 } }")
    long countByDateBetween(LocalDate min, LocalDate max); //Conteo de reportes entre dos fechas
}
