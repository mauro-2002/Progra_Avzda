package org.openapitools.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.openapitools.dto.ComentarioDTO.ComentarioRequestDto;
import org.openapitools.dto.ComentarioDTO.ComentarioResponseDto;
import org.openapitools.dto.ReporteDTO.ReportePatchRequestDto;
import org.openapitools.dto.ReporteDTO.ReporteRequestDto;
import org.openapitools.dto.ReporteDTO.ReporteResponseDto;
import org.openapitools.mappers.ComentarioMapper;
import org.openapitools.mappers.ReportMapper;
import org.openapitools.model.Comentario;
import org.openapitools.model.Reporte;
import org.openapitools.services.interfaces.ReporteServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteControlador {

    private final ReporteServicio reporteServicio;
    private final ReportMapper reportMapper;
    private final ComentarioMapper comentarioMapper;

    // Crear un nuevo reporte
    @PostMapping
    public ResponseEntity<ReporteResponseDto> crearReporte(@RequestBody ReporteRequestDto dto) throws Exception {
        Reporte reporte = reportMapper.toEntity(dto);
        Reporte guardado = reporteServicio.guardarReporte(reporte);
        return new ResponseEntity<>(reportMapper.toDto(guardado), HttpStatus.CREATED);
    }

    // Obtener todos los reportes
    @GetMapping
    public ResponseEntity<List<ReporteResponseDto>> obtenerTodos() throws Exception {
        List<Reporte> reportes = reporteServicio.obtenerTodosLosReportes();
        List<ReporteResponseDto> respuesta = reportes.stream()
                .map(reportMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuesta);
    }

    // Obtener reporte por ID
    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponseDto> obtenerPorId(@PathVariable String id) throws Exception {
        Reporte reporte = reporteServicio.obtenerReporte(id);
        return ResponseEntity.ok(reportMapper.toDto(reporte));
    }

    // Actualizar reporte (patch)
    @PatchMapping("/{id}")
    public ResponseEntity<ReporteResponseDto> actualizarReporte(@PathVariable String id, @RequestBody ReportePatchRequestDto patchDto) throws Exception {
        Reporte actualizado = reporteServicio.actualizarReporte(id, patchDto);
        return ResponseEntity.ok(reportMapper.toDto(actualizado));
    }

    // Eliminar un reporte
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable String id) throws Exception {
        reporteServicio.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }

    // Agregar comentario a un reporte
    @PostMapping("/{id}/comentarios")
    public ResponseEntity<ComentarioResponseDto> agregarComentario(
            @PathVariable String id,
            @RequestBody @Valid ComentarioRequestDto comentarioDto) throws Exception {

        Comentario comentario = reporteServicio.agregarComentarioAlReporte(id, comentarioDto);
        return new ResponseEntity<>(comentarioMapper.toDto(comentario), HttpStatus.CREATED);
    }

    // Obtener comentarios de un reporte
    @GetMapping("/{id}/comentarios")
    public ResponseEntity<List<Comentario>> obtenerComentarios(@PathVariable String idReporte) throws Exception {
        List<Comentario> comentarios = reporteServicio.obtenerComentariosDelReporte(idReporte);
        return ResponseEntity.ok(comentarios);
    }
}

