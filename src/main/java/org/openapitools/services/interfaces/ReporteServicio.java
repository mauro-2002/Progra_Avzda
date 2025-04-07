package org.openapitools.services.interfaces;


import com.mongodb.client.model.geojson.Point;
import org.openapitools.model.Reporte;
import org.openapitools.model.enums.StatusReporte;
import org.springframework.data.geo.Distance;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface ReporteServicio {

    Reporte guardarReporte(Reporte reporte) throws Exception;

    void eliminarReporte(String id) throws Exception;

    Reporte obtenerReporte(String id) throws Exception;

    List<Reporte> obtenerReportesPorUsuario(String idUsuario) throws Exception;

    List<Reporte> obtenerReportesPorUsuarioYEstado(String idUsuario, StatusReporte estado) throws Exception;

    List<Reporte> obtenerReportesImportantes(int contMinimo) throws Exception;

    List<Reporte> obtenerReportesPorCategoria(String categoriaId) throws Exception;

    List<Reporte> obtenerReportesCercanos(Point ubicacion, Distance distancia) throws Exception;

    List<Reporte> obtenerReportesEntreFechas(LocalDateTime inicio, LocalDateTime fin) throws Exception;

    List<Reporte> obtenerReportesDespuesDe(LocalDateTime fecha) throws Exception;

    List<Reporte> obtenerReportesPorRangoDeImportancia(int min, int max) throws Exception;

    long contarReportesEntreFechas(LocalDateTime inicio, LocalDateTime fin) throws Exception;
}



