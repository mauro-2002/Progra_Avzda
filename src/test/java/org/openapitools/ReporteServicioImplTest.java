package org.openapitools;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openapitools.dto.ComentarioDTO.ComentarioRequestDto;
import org.openapitools.dto.ReporteDTO.ReportePatchRequestDto;
import org.openapitools.model.Comentario;
import org.openapitools.model.Reporte;
import org.openapitools.model.Usuario;
import org.openapitools.model.enums.Rol;
import org.openapitools.model.enums.StatusReporte;
import org.openapitools.model.enums.StatusUsuario;
import org.openapitools.repositories.ReportRepository;
import org.openapitools.repositories.UserRepository;
import org.openapitools.services.implementations.ReporteServicioImpl;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReporteServicioImplTest {

    private ReportRepository reportRepository;
    private UserRepository userRepository;
    private ReporteServicioImpl reporteServicio;

    @BeforeEach
    void setUp() {
        reportRepository = mock(ReportRepository.class);
        userRepository = mock(UserRepository.class);
        reporteServicio = new ReporteServicioImpl(reportRepository, userRepository);
    }

    // ---------- GUARDAR ----------

    @Test
    void guardarReporte_exitoso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId("usuario123");
        usuario.setStatus(StatusUsuario.ACTIVO);

        Reporte reporte = Reporte.builder()
                .titulo("Reporte de prueba")
                .descripcion("Descripción")
                .ubicacion(new GeoJsonPoint(1, 1))
                .imagesUrl(List.of("https://img.com"))
                .idUsuario(new ObjectId("64e9ad4d01ab8e17ddfa29c7"))
                .build();

        when(userRepository.findById("64e9ad4d01ab8e17ddfa29c7")).thenReturn(Optional.of(usuario));
        when(reportRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Reporte resultado = reporteServicio.guardarReporte(reporte);

        assertNotNull(resultado.getFechaCreacion());
        assertEquals(0, resultado.getContImportancia());
        assertEquals(StatusReporte.PENDIENTE, resultado.getEstado());
    }

    @Test
    void guardarReporte_usuarioInactivo_lanzaExcepcion() {
        Usuario usuario = new Usuario();
        usuario.setId("usuario123");
        usuario.setStatus(StatusUsuario.REGISTRADO);

        Reporte reporte = new Reporte();
        reporte.setIdUsuario(new ObjectId("64e9ad4d01ab8e17ddfa29c7"));

        when(userRepository.findById("64e9ad4d01ab8e17ddfa29c7")).thenReturn(Optional.of(usuario));

        Exception ex = assertThrows(Exception.class, () -> reporteServicio.guardarReporte(reporte));
        assertEquals("El usuario no está activo.", ex.getMessage());
    }

    @Test
    void guardarReporte_tituloNulo_lanzaExcepcion() {
        Usuario usuario = new Usuario();
        usuario.setId("usuario123");
        usuario.setStatus(StatusUsuario.ACTIVO);

        Reporte reporte = new Reporte();
        reporte.setIdUsuario(new ObjectId("64e9ad4d01ab8e17ddfa29c7"));
        reporte.setDescripcion("desc");
        reporte.setUbicacion(new GeoJsonPoint(1, 1));
        reporte.setImagesUrl(List.of("https://img.com"));

        when(userRepository.findById("64e9ad4d01ab8e17ddfa29c7")).thenReturn(Optional.of(usuario));

        Exception ex = assertThrows(Exception.class, () -> reporteServicio.guardarReporte(reporte));
        assertEquals("El título es obligatorio.", ex.getMessage());
    }

    // ---------- OBTENER / ELIMINAR ----------

    @Test
    void obtenerReporte_exitoso() throws Exception {
        Reporte reporte = new Reporte();
        reporte.setIdReporte("abc");

        when(reportRepository.findById("abc")).thenReturn(Optional.of(reporte));

        Reporte result = reporteServicio.obtenerReporte("abc");

        assertEquals("abc", result.getIdReporte());
    }

    @Test
    void eliminarReporte_exitoso() throws Exception {
        // Mock del reporte
        ObjectId idUsuario = new ObjectId("661c207a5b5b5a3d487d1f31");
        Reporte reporte = Reporte.builder()
                .idReporte("abc")
                .idUsuario(idUsuario)
                .estado(StatusReporte.VIGENTE)
                .build();

        // Mock del usuario autenticado (mismo ID que el del reporte)
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(idUsuario.toString());
        usuarioMock.setRol(Rol.STANDAR); // no necesita ser ADMIN
        usuarioMock.setStatus(StatusUsuario.ACTIVO);

        // Spy para interceptar métodos internos como obtenerUsuarioAutenticado()
        ReporteServicioImpl spyServicio = Mockito.spy(reporteServicio);

        // Simular comportamiento del repositorio y el método protegido
        when(reportRepository.findById("abc")).thenReturn(Optional.of(reporte));
        doReturn(usuarioMock).when(spyServicio).obtenerUsuarioAutenticado();

        // Ejecutar método
        spyServicio.eliminarReporte("abc");

        // Verificar que se haya guardado el reporte (soft delete)
        verify(reportRepository).save(argThat(r -> r.getEstado() == StatusReporte.ELIMINADO));
    }

    // ---------- ACTUALIZAR ----------

    @Test
    void actualizarReporte_modificaCamposCorrectamente() throws Exception {
        Reporte reporte = new Reporte();
        reporte.setIdReporte("id123");

        ReportePatchRequestDto patch = new ReportePatchRequestDto();
        patch.setTitulo("Nuevo título");
        patch.setDescripcion("Nueva descripción");
        patch.setEstado(StatusReporte.RESUELTO);
        patch.setContadorImportancia(5);

        when(reportRepository.findById("id123")).thenReturn(Optional.of(reporte));
        when(reportRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Reporte actualizado = reporteServicio.actualizarReporte("id123", patch);

        assertEquals("Nuevo título", actualizado.getTitulo());
        assertEquals(StatusReporte.RESUELTO, actualizado.getEstado());
    }

    // ---------- COMENTARIOS ----------

    @Test
    void agregarComentarioExitosamente() throws Exception {

        Usuario usuario = Usuario.builder()
                .id("usuario123")
                .status(StatusUsuario.ACTIVO)
                .build();

        ComentarioRequestDto comentarioDto = new ComentarioRequestDto();
        comentarioDto.setIdUsuario("usuario123");
        comentarioDto.setDescripcion("Comentario de prueba");

        Reporte reporte = new Reporte();
        reporte.setIdReporte("reporte123");
        reporte.setComentarios(new ArrayList<>());

        when(reportRepository.findById("reporte123")).thenReturn(Optional.of(reporte));
        when(userRepository.findById("usuario123")).thenReturn(Optional.of(usuario));
        when(reportRepository.save(any())).thenReturn(reporte);

        Comentario comentario = reporteServicio.agregarComentarioAlReporte("reporte123", comentarioDto);

        assertEquals("usuario123", comentario.getIdUsuario());
        assertNotNull(comentario.getFechaCreacion());
        assertEquals(1, reporte.getComentarios().size());
    }

    @Test
    void obtenerComentariosDelReporte_exitoso() throws Exception {
        Comentario c1 = new Comentario();
        c1.setId("c1");

        Reporte reporte = new Reporte();
        reporte.setComentarios(List.of(c1));

        when(reportRepository.findById("r1")).thenReturn(Optional.of(reporte));

        List<Comentario> comentarios = reporteServicio.obtenerComentariosDelReporte("r1");

        assertEquals(1, comentarios.size());
        assertEquals("c1", comentarios.get(0).getId());
    }

    // ---------- BUSQUEDAS SIMPLES ----------

    @Test
    void obtenerTodosLosReportes_devuelveLista() throws Exception {
        when(reportRepository.findAll()).thenReturn(List.of(new Reporte(), new Reporte()));
        List<Reporte> resultados = reporteServicio.obtenerTodosLosReportes();
        assertEquals(2, resultados.size());
    }

    @Test
    void obtenerReportesPorUsuario_devuelveLista() throws Exception {
        when(reportRepository.findByIdUsuarioOrderByFechaDesc("usuario1")).thenReturn(List.of(new Reporte()));
        List<Reporte> resultados = reporteServicio.obtenerReportesPorUsuario("usuario1");
        assertEquals(1, resultados.size());
    }

    @Test
    void contarReportesEntreFechas_devuelveValor() throws Exception {
        LocalDateTime ini = LocalDateTime.now().minusDays(1);
        LocalDateTime fin = LocalDateTime.now();

        when(reportRepository.countByFechaBetween(ini, fin)).thenReturn(5L);

        long count = reporteServicio.contarReportesEntreFechas(ini, fin);

        assertEquals(5, count);
    }

    @Test
    void obtenerTodosLosReportes_deberiaRetornarListaCompleta() throws Exception {
        List<Reporte> mockReportes = List.of(new Reporte(), new Reporte());
        when(reportRepository.findAll()).thenReturn(mockReportes);

        List<Reporte> resultado = reporteServicio.obtenerTodosLosReportes();

        assertEquals(2, resultado.size());
        verify(reportRepository).findAll();
    }

    @Test
    void obtenerReportesPorUsuario_deberiaRetornarReportesDelUsuario() throws Exception {
        String idUsuario = "usuario123";
        List<Reporte> mockReportes = List.of(new Reporte());
        when(reportRepository.findByIdUsuarioOrderByFechaDesc(idUsuario)).thenReturn(mockReportes);

        List<Reporte> resultado = reporteServicio.obtenerReportesPorUsuario(idUsuario);

        assertEquals(1, resultado.size());
        verify(reportRepository).findByIdUsuarioOrderByFechaDesc(idUsuario);
    }

    @Test
    void obtenerReportesPorUsuarioYEstado_deberiaFiltrarPorEstadoCuandoUsuarioEsPropietario() throws Exception {
        // Arrange
        String idUsuario = "usuario123";
        StatusReporte estado = StatusReporte.RESUELTO;
        List<Reporte> mockReportes = List.of(new Reporte());

        // Usuario autenticado simulado
        Usuario usuarioAutenticado = Usuario.builder()
                .id(idUsuario)
                .rol(Rol.STANDAR)
                .status(StatusUsuario.ACTIVO)
                .build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(usuarioAutenticado);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Simular respuesta del repositorio
        when(reportRepository.findByIdUsuarioAndEstado(idUsuario, estado)).thenReturn(mockReportes);

        // Act
        List<Reporte> resultado = reporteServicio.obtenerReportesPorUsuarioYEstado(idUsuario, estado);

        // Assert
        assertEquals(1, resultado.size());
        verify(reportRepository).findByIdUsuarioAndEstado(idUsuario, estado);
    }

    @Test
    void obtenerReportesImportantes_deberiaFiltrarPorImportancia() throws Exception {
        when(reportRepository.findByContImportanciaGreaterThanOrderByFechaDesc(5))
                .thenReturn(List.of(new Reporte()));

        List<Reporte> resultado = reporteServicio.obtenerReportesImportantes(5);

        assertFalse(resultado.isEmpty());
        verify(reportRepository).findByContImportanciaGreaterThanOrderByFechaDesc(5);
    }

    @Test
    void obtenerReportesPorCategoria_deberiaFiltrarPorCategoria() throws Exception {
        String categoriaId = "categoria1";
        when(reportRepository.findByCategorias_Id(categoriaId))
                .thenReturn(List.of(new Reporte()));

        List<Reporte> resultado = reporteServicio.obtenerReportesPorCategoria(categoriaId);

        assertEquals(1, resultado.size());
        verify(reportRepository).findByCategorias_Id(categoriaId);
    }

    @Test
    void obtenerReportesCercanos_deberiaRetornarReportesCercanos() throws Exception {
        Point punto = new Point(-75.5, 4.6);
        Distance distancia = new Distance(1.0); // 1 km

        when(reportRepository.findByUbicacionNear(punto, distancia))
                .thenReturn(List.of(new Reporte()));

        List<Reporte> resultado = reporteServicio.obtenerReportesCercanos(punto, distancia);

        assertEquals(1, resultado.size());
        verify(reportRepository).findByUbicacionNear(punto, distancia);
    }

    @Test
    void obtenerReportesEntreFechas_deberiaFiltrarCorrectamente() throws Exception {
        LocalDateTime inicio = LocalDateTime.now().minusDays(10);
        LocalDateTime fin = LocalDateTime.now();

        when(reportRepository.findByFechaBetween(inicio, fin))
                .thenReturn(List.of(new Reporte()));

        List<Reporte> resultado = reporteServicio.obtenerReportesEntreFechas(inicio, fin);

        assertEquals(1, resultado.size());
        verify(reportRepository).findByFechaBetween(inicio, fin);
    }

    @Test
    void obtenerReportesDespuesDe_deberiaRetornarReportesRecientes() throws Exception {
        LocalDateTime fecha = LocalDateTime.now().minusDays(3);

        when(reportRepository.findByFechaAfter(fecha))
                .thenReturn(List.of(new Reporte()));

        List<Reporte> resultado = reporteServicio.obtenerReportesDespuesDe(fecha);

        assertEquals(1, resultado.size());
        verify(reportRepository).findByFechaAfter(fecha);
    }

    @Test
    void obtenerReportesPorRangoDeImportancia_deberiaFiltrarPorRango() throws Exception {
        when(reportRepository.findByContImportanciaBetween(2, 10))
                .thenReturn(List.of(new Reporte()));

        List<Reporte> resultado = reporteServicio.obtenerReportesPorRangoDeImportancia(2, 10);

        assertFalse(resultado.isEmpty());
        verify(reportRepository).findByContImportanciaBetween(2, 10);
    }

    @Test
    void contarReportesEntreFechas_deberiaRetornarConteo() throws Exception {
        LocalDateTime inicio = LocalDateTime.now().minusDays(5);
        LocalDateTime fin = LocalDateTime.now();

        when(reportRepository.countByFechaBetween(inicio, fin)).thenReturn(4L);

        long conteo = reporteServicio.contarReportesEntreFechas(inicio, fin);

        assertEquals(4L, conteo);
        verify(reportRepository).countByFechaBetween(inicio, fin);
    }
}