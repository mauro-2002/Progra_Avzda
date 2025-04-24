package org.openapitools.services.implementations;

import lombok.RequiredArgsConstructor;
import org.openapitools.dto.NotificacionDTO.NotificacionRequest;
import org.openapitools.dto.NotificacionDTO.NotificacionResponse;
import org.openapitools.dto.SuccessResponse;
import org.openapitools.exceptions.UserNotFoundException;
import org.openapitools.mappers.NotificacionMapper;
import org.openapitools.model.Notificacion;
import org.openapitools.model.Usuario;
import org.openapitools.repositories.UserRepository;
import org.openapitools.services.interfaces.NotificacionService;
import org.openapitools.services.interfaces.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionMapper notificacionMapper;
    private final UserRepository usuarioRepository;

    @Override
    public Optional<SuccessResponse> sendNotificacion(Long idUsuario, NotificacionRequest notificacionRequest) {
        var noti = notificacionMapper.parseToNotificacion(notificacionRequest);
        var user = usuarioRepository.findUserByID(idUsuario);
        if (user.isEmpty()) {
            throw new UserNotFoundException("No existe el usuario con el id: " + idUsuario);
        }
        user.get().getNotificaciones().add(noti);

        return Optional.of(new SuccessResponse("Notificacion enviada correctamente"));
    }

    @Override
    public Optional<NotificacionResponse> getNotificacion(NotificacionRequest notificacionRequest) {
        return Optional.empty();
    }

    @Override
    public void markAsView(Long idNoti, Long idUsuario) {
        var user = usuarioRepository.findUserByID(idUsuario);
        if (user.isEmpty()) {
            throw new UserNotFoundException("No existe el usuario con el id: " + idUsuario);
        }
        markNoti(user.get(), idNoti);
    }

    private void markNoti (Usuario user, Long idNoti){
        for (Notificacion notificacion : user.getNotificaciones()) {
            if (notificacion.getId().equals(idNoti)) {
                notificacion.setLeida(Boolean.TRUE);
                break;
            }
        }
    }
}
