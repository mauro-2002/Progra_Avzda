package org.openapitools.repositories;

import org.openapitools.model.Reporte;
import org.openapitools.model.enums.StatusReporte;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.time.LocalDateTime;
import java.util.List;


public interface ReportRepository extends MongoRepository<Reporte, String> {

    @Query("{'idUsuario' : ?0}")
    List<Reporte> findByIdUsuario(String idUsuario); //Reporte por usuario basado en su Id

    @Query("{ 'idUsuario' : ?0, 'estado' : ?1 }")
    List<Reporte> findByIdUsuarioAndEstado(String idUsuario, StatusReporte estado); //Reportes por usuario y estado

    @Query("{ 'contImportancia' : { $gte : ?0 } }")
    List<Reporte> findByContImportanciaGreaterThanOrderByFechaDesc(int contImportancia); //Reportes más importantes basado en el númoero de "me gustas"

    List<Reporte> findByIdUsuarioOrderByFechaDesc(String idUsuario); //Reportes por usuario específico

    List<Reporte> findByCategorias_Id(String categoriaId); //Reportes por categoría

    List<Reporte> findByUbicacionNear(Point location, Distance distance);//Reportes por localización

    @Query("{ 'fecha' : { $gte : ?0, $lt : ?1 } }")
    List<Reporte> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);//Reportes de una fecha en especifico

    @Query("{ 'contImportancia' : { $gte : ?0, $lte: ?1 } }")
    List<Reporte> findByContImportanciaBetween(int min, int max); //Reportes con nivel de importancia

    @Query("{ 'fecha' : { $gte : ?0, $lte: ?1 } }")
    long countByFechaBetween(LocalDateTime min, LocalDateTime max); //Conteo de reportes entre dos fechas

    @Query("{ 'fecha' : { $gte : ?0 } }")
    List<Reporte> findByFechaAfter(LocalDateTime fecha); //Reportes después de una fecha en específico
}
