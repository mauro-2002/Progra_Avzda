package org.openapitools.services.implementations;

import lombok.RequiredArgsConstructor;
import org.openapitools.dto.*;
import org.openapitools.mappers.UsuarioMapper;
import org.openapitools.model.Notificacion;
import org.openapitools.model.Usuario;
import org.openapitools.model.enums.Rol;
import org.openapitools.model.enums.StatusUsuario;
import org.openapitools.repositories.UserRepository;
import org.openapitools.services.interfaces.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UserRepository userRepository;
    private final UsuarioMapper userMapper;

    @Override
    public UsuarioResponse createUsuario(UsuarioRequest usuarioRequest) {
        //comprobacion de usuario registrado (en la bd)
        var usuario = findUsuarioByEmail(usuarioRequest.email());
        if (usuario != null) {
            // lanza una excepcion
        }
        //creacion del usuario
        usuario = userMapper.parseOf(usuarioRequest);
        usuario.setId(UUID.randomUUID().toString());
        usuario.setStatus(StatusUsuario.REGISTRADO);
        usuario.setRol(Rol.STANDAR);

        //Agregar el usuario a la bd
        userRepository.save(usuario);
        return userMapper.toUserResponse(usuario);
    }

    @Override
    public UsuarioResponse updateUsuario(Long id, UsuarioUpdateRequest usuarioUpdateRequest) {
        var usuario = findUsuarioByID(id);
        if (usuario == null){
            // lanza una exception
            throw new NullPointerException("El usuario no existe");
        }
        usuario.setNombre(usuarioUpdateRequest.newNombre());
        usuario.setCiudad(usuarioUpdateRequest.newCiudad());
        usuario.setDireccion(usuarioUpdateRequest.newDireccion());
        usuario.setTelefono(usuarioUpdateRequest.newTelefono());
        userRepository.save(usuario);

        return userMapper.toUserResponse(usuario);
    }

    @Override
    public SuccessResponse deleteUsuario(Long id) {
        var usuario = findUsuarioByID(id);
        if (usuario == null){
            throw new NullPointerException("El usuario no existe");
        }
        usuario.setStatus(StatusUsuario.ELIMINADO);
        userRepository.save(usuario);
        return new SuccessResponse("Usuario eliminado");
    }

    @Override
    public UsuarioResponse getUsuario(Long id) {
        var usuario = findUsuarioByID(id);
        if (usuario == null){
            throw new NullPointerException("El usuario no existe");
        }
        return userMapper.toUserResponse(usuario);
    }

    @Override
    public List<Notificacion> getNotificacionesUsuario(Long id) {
        var usuario = findUsuarioByID(id);
        if (usuario == null){
            throw new NullPointerException("El usuario no existe");
        }
        return usuario.getNotificaciones();
    }

    @Override
    public Optional<UsuarioResponse> LogIn(LoginRequest loginRequest) {
        return Optional.empty();
    }

    @Override
    public void activarUsuario(ActivarCuentaRequest activarRequest) {

    }

    @Override
    public void recuperarPassword(RecuperarPasswordRequest request) {

    }

    @Override
    public Optional<SuccessResponse> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        return Optional.empty();
    }

    private Usuario findUsuarioByEmail(String Email) {
        return userRepository.findUserByEmail(Email).orElse(null);
    }

    private Usuario findUsuarioByID(Long id) {
        return userRepository.findUserByID(id).orElse(null);
    }
}
