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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionMapper notificacionMapper;
    private final UserRepository usuarioRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<SuccessResponse> sendNotificacion(String idUsuario, NotificacionRequest notificacionRequest) {
        var noti = notificacionMapper.parseToNotificacion(notificacionRequest);
        var user = usuarioRepository.findUserByID(idUsuario);
        if (user.isEmpty()) {
            throw new UserNotFoundException("No existe el usuario con el id: " + idUsuario);
        }
        user.get().getNotificaciones().add(noti);

        return Optional.of(new SuccessResponse("Notificacion enviada"));
    }

    @Override
    public Optional<List<NotificacionResponse>> getNotificaciones(NotificacionRequest notificacionRequest) {
        var user = userRepository.findUserByID(notificacionRequest.idUsuario());
        if (user.isEmpty()) {
            throw new UserNotFoundException("No existe el usuario con el id: " + notificacionRequest.idUsuario());
        }
        List<NotificacionResponse> notificaciones = new ArrayList<>();
        for (Notificacion notificacion : user.get().getNotificaciones()) {
            notificaciones.add(notificacionMapper.toNotificacionResponse(notificacion));
        }
        return Optional.of(notificaciones);
    }

    @Override
    public void markAsView(String idNoti, String idUsuario) {
        var user = usuarioRepository.findUserByID(idUsuario);
        if (user.isEmpty()) {
            throw new UserNotFoundException("No existe el usuario con el id: " + idUsuario);
        }
        markNoti(user.get(), idNoti);
    }

    private void markNoti (Usuario user, String idNoti){
        for (Notificacion notificacion : user.getNotificaciones()) {
            if (notificacion.getId().equals(idNoti)) {
                notificacion.setLeida(Boolean.TRUE);
                break;
            }
        }
    }
}
