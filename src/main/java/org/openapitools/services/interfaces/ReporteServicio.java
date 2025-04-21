package org.openapitools.services.interfaces;

import org.openapitools.dto.ComentarioDTO.ComentarioRequestDto;
import org.openapitools.dto.ReporteDTO.ReportePatchRequestDto;
import org.openapitools.model.Comentario;
import org.springframework.data.geo.Point;
import org.openapitools.model.Reporte;
import org.openapitools.model.enums.StatusReporte;
import org.springframework.data.geo.Distance;
import java.time.LocalDateTime;
import java.util.List;

public interface ReporteServicio {

    Reporte guardarReporte(Reporte reporte) throws Exception;
    Reporte obtenerReporte(String id) throws Exception;
    Reporte actualizarReporte(String id, ReportePatchRequestDto patchDto) throws Exception;
    List<Comentario> obtenerComentariosDelReporte(String idReporte) throws Exception;
    Comentario agregarComentarioAlReporte (String id, ComentarioRequestDto comentarioDto) throws Exception;
    List<Reporte> obtenerTodosLosReportes() throws Exception;
    List<Reporte> obtenerReportesPorUsuario(String idUsuario) throws Exception;
    List<Reporte> obtenerReportesPorUsuarioYEstado(String idUsuario, StatusReporte estado) throws Exception;
    List<Reporte> obtenerReportesImportantes(int contMinimo) throws Exception;
    List<Reporte> obtenerReportesPorCategoria(String categoriaId) throws Exception;
    List<Reporte> obtenerReportesCercanos(Point ubicacion, Distance distancia) throws Exception;
    List<Reporte> obtenerReportesEntreFechas(LocalDateTime inicio, LocalDateTime fin) throws Exception;
    List<Reporte> obtenerReportesDespuesDe(LocalDateTime fecha) throws Exception;
    List<Reporte> obtenerReportesPorRangoDeImportancia(int min, int max) throws Exception;
    void eliminarReporte(String id) throws Exception;
    long contarReportesEntreFechas(LocalDateTime inicio, LocalDateTime fin) throws Exception;
}



