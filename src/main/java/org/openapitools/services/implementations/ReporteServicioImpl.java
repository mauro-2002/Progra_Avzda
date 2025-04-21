package org.openapitools.services.implementations;

import lombok.RequiredArgsConstructor;
import org.openapitools.dto.ComentarioDTO.ComentarioRequestDto;
import org.openapitools.dto.ReporteDTO.ReportePatchRequestDto;
import org.openapitools.dto.UbicacionDTO.UbicacionDto;
import org.openapitools.model.Comentario;
import org.openapitools.model.Usuario;
import org.openapitools.model.enums.Rol;
import org.openapitools.model.enums.StatusUsuario;
import org.openapitools.repositories.UserRepository;
import org.springframework.data.geo.Point;
import org.openapitools.model.Reporte;
import org.openapitools.model.enums.StatusReporte;
import org.openapitools.repositories.ReportRepository;
import org.openapitools.services.interfaces.ReporteServicio;
import org.springframework.data.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.openapitools.exceptions.ResourceNotFoundException;
import org.openapitools.exceptions.UnauthorizedException;
import org.openapitools.exceptions.InvalidStateException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReporteServicioImpl implements ReporteServicio {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;


    @Override
    public Reporte guardarReporte(Reporte reporte) throws Exception {
        validarUsuario(reporte.getIdUsuario());
        validarDatosDelReporte(reporte);

        reporte.setFechaCreacion(LocalDateTime.now());
        reporte.setContImportancia(0);
        reporte.setEstado(StatusReporte.PENDIENTE);

        return reportRepository.save(reporte);
    }

    @Override
    public void eliminarReporte(String id) throws Exception {
        var reporte = getReporteByIdOrThrow(id);
        var solicitante = obtenerUsuarioAutenticado(); // Obtener usuario autenticado

        validateActivo(solicitante);

        if (!isOwnerOrAdmin(solicitante, reporte.getIdUsuario().toString())) {
            throw new UnauthorizedException("No tienes permiso para eliminar este reporte.");
        }

        // Soft delete
        reporte.setEstado(StatusReporte.ELIMINADO);
        reportRepository.save(reporte);
    }

    @Override
    public Reporte obtenerReporte(String id) throws Exception {
        return validarExistenciaReporte(id);
    }

    @Override
    public Reporte actualizarReporte(String id, ReportePatchRequestDto patchDto) throws Exception {
        Reporte reporte = reportRepository.findById(id)
                .orElseThrow(() -> new Exception("Reporte no encontrado con id: " + id));

        if (patchDto.getTitulo() != null) reporte.setTitulo(patchDto.getTitulo());
        if (patchDto.getDescripcion() != null) reporte.setDescripcion(patchDto.getDescripcion());
        if (patchDto.getUbicacion() != null) {
            reporte.setUbicacion(convertirADocumentoGeoJson(patchDto.getUbicacion()));
        }
        if (patchDto.getImagenes() != null) reporte.setImagesUrl(patchDto.getImagenes());
        if (patchDto.getEstado() != null) reporte.setEstado(patchDto.getEstado());
        if (patchDto.getContadorImportancia() != null) reporte.setContImportancia(patchDto.getContadorImportancia());

        return reportRepository.save(reporte);
    }

    @Override
    public List<Comentario> obtenerComentariosDelReporte(String idReporte) throws Exception {
        Reporte reporte = validarExistenciaReporte(idReporte);
        return reporte.getComentarios();
    }

    @Override
    public Comentario agregarComentarioAlReporte(String id, ComentarioRequestDto comentarioDto) throws Exception {
        Reporte reporte = validarExistenciaReporte(id);

        // Validar el usuario
        if (comentarioDto.getIdUsuario() == null || comentarioDto.getIdUsuario().isBlank()) {
            throw new Exception("Debe especificarse el ID del usuario que comenta.");
        }

        Usuario usuario = userRepository.findById(comentarioDto.getIdUsuario())
                .orElseThrow(() -> new Exception("Usuario del comentario no registrado."));

        if (usuario.getStatus() != StatusUsuario.ACTIVO) {
            throw new Exception("El usuario no está activo y no puede comentar.");
        }

        if (comentarioDto.getDescripcion() == null || comentarioDto.getDescripcion().isBlank()) {
            throw new Exception("La descripción del comentario es obligatoria.");
        }

        Comentario comentario = Comentario.builder()
                .id(UUID.randomUUID().toString())
                .idUsuario(comentarioDto.getIdUsuario())
                .descripcion(comentarioDto.getDescripcion())
                .fechaCreacion(LocalDateTime.now())
                .build();

        if (reporte.getComentarios() == null) {
            reporte.setComentarios(new ArrayList<>());
        }

        reporte.getComentarios().add(comentario);
        reportRepository.save(reporte);

        return comentario;
    }   

    @Override
    public List<Reporte> obtenerTodosLosReportes() throws Exception {
        return reportRepository.findAll();
    }

    @Override
    public List<Reporte> obtenerReportesPorUsuario(String idUsuario) throws Exception {
        return reportRepository.findByIdUsuarioOrderByFechaDesc(idUsuario);
    }

    @Override
    public List<Reporte> obtenerReportesPorUsuarioYEstado(String idUsuario, StatusReporte estado) throws Exception {
        var solicitante = obtenerUsuarioAutenticado(); // Obtener usuario autenticado

        validateActivo(solicitante);

        if (!isOwnerOrAdmin(solicitante, idUsuario)) {
            throw new UnauthorizedException("No tienes permiso para consultar estos reportes.");
        }

        return reportRepository.findByIdUsuarioAndEstado(idUsuario, estado);
    }

    @Override
    public List<Reporte> obtenerReportesImportantes(int contMinimo) throws Exception {
        return reportRepository.findByContImportanciaGreaterThanOrderByFechaDesc(contMinimo);
    }

    @Override
    public List<Reporte> obtenerReportesPorCategoria(String categoriaId) throws Exception {
        return reportRepository.findByCategorias_Id(categoriaId);
    }

    @Override
    public List<Reporte> obtenerReportesCercanos(Point ubicacion, Distance distancia) throws Exception {
        return reportRepository.findByUbicacionNear(ubicacion, distancia);
    }

    @Override
    public List<Reporte> obtenerReportesEntreFechas(LocalDateTime inicio, LocalDateTime fin) throws Exception {
        return reportRepository.findByFechaBetween(inicio, fin);
    }

    @Override
    public List<Reporte> obtenerReportesDespuesDe(LocalDateTime fecha) throws Exception {
        return reportRepository.findByFechaAfter(fecha);
    }

    @Override
    public List<Reporte> obtenerReportesPorRangoDeImportancia(int min, int max) throws Exception {
        return reportRepository.findByContImportanciaBetween(min, max);
    }

    @Override
    public long contarReportesEntreFechas(LocalDateTime inicio, LocalDateTime fin) throws Exception {
        return reportRepository.countByFechaBetween(inicio, fin);
    }

    // ================= MÉTODOS PRIVADOS ===================

    private GeoJsonPoint convertirADocumentoGeoJson(UbicacionDto ubicacionDto) {
        return new GeoJsonPoint(ubicacionDto.getLongitud(), ubicacionDto.getLatitud());
    }

    private void validarUsuario(Object idUsuario) throws Exception {
        if (idUsuario == null) {
            throw new Exception("Debe especificarse el ID del usuario.");
        }

        Usuario usuario = userRepository.findById(String.valueOf(idUsuario))
                .orElseThrow(() -> new Exception("Usuario no registrado."));

        if (usuario.getStatus() != StatusUsuario.ACTIVO) {
            throw new Exception("El usuario no está activo.");
        }
    }

    private void validarDatosDelReporte(Reporte reporte) throws Exception {
        if (reporte.getTitulo() == null || reporte.getTitulo().isBlank()) {
            throw new Exception("El título es obligatorio.");
        }

        if (reporte.getDescripcion() == null || reporte.getDescripcion().isBlank()) {
            throw new Exception("La descripción es obligatoria.");
        }

        if (reporte.getUbicacion() == null) {
            throw new Exception("Debe especificar la ubicación.");
        }

        if (reporte.getImagesUrl() == null || reporte.getImagesUrl().isEmpty()) {
            throw new Exception("Debe agregar al menos una imagen.");
        }
    }

    private Reporte validarExistenciaReporte(String id) throws Exception {
        return reportRepository.findById(id)
                .orElseThrow(() -> new Exception("Reporte no encontrado con id: " + id));
    }

    private Reporte getReporteByIdOrThrow(String id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con id: " + id));
    }

    private Usuario getUsuarioByIdOrThrow(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    private void validateActivo(Usuario usuario) {
        if (usuario.getStatus() != StatusUsuario.ACTIVO) {
            throw new InvalidStateException("El usuario no está activo.");
        }
    }

    private boolean isOwnerOrAdmin(Usuario usuario, String idRecurso) {
        return usuario.getRol() == Rol.ADMIN || usuario.getId().equals(idRecurso);
    }

    public Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();
    }
}
