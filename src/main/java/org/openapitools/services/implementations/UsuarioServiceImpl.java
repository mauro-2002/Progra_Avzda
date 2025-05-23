package org.openapitools.services.implementations;

import lombok.RequiredArgsConstructor;
import org.openapitools.dto.UsuarioDTO.*;
import org.openapitools.exceptions.UserAlreadyExists;
import org.openapitools.exceptions.UserNotFoundException;
import org.openapitools.mappers.UsuarioMapper;
import org.openapitools.model.Notificacion;
import org.openapitools.model.UsuarioStandar;
import org.openapitools.model.enums.StatusUsuario;
import org.openapitools.repositories.UserRepository;
import org.openapitools.services.EmailService;
import org.openapitools.services.interfaces.UsuarioService;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final SecureRandom random = new SecureRandom();
    private final UserRepository userRepository;
    private final UsuarioMapper usuarioMapper;
    private final EmailService emailService;

    @Override
    public UsuarioResponse createUsuario(UsuarioRequest usuarioRequest) {
        //comprobacion de usuario registrado (en la bd)
        var usuario = findUsuarioByEmail(usuarioRequest.email());
        if (usuario != null) {
            // lanza una excepcion
            throw new UserAlreadyExists("Ya existe un usuario anclado al correo proporcionado");
        }
        //creacion del usuario

        usuario = usuarioMapper.parseOf(usuarioRequest);
        String codigo = generarCodigo();
        usuario.setCodigoActivacion(codigo);
        usuario.setExpiracionCodigo(LocalDateTime.now().plusMinutes(15));

        //Agregar el usuario a la bd
        userRepository.save(usuario);

        //Envia el correo con el cogido de activacion
        emailService.enviarCorreo(
                        usuario.getEmail(),
                "Activacion cuenta de usuario - Seguridad Ciudadana",
                "Su registro a sido realizado correctamente, por" +
                        "favor utilice el siguiente codigo para activar" +
                        "su usuario: " + codigo + "\n"+"\n"+
                        "Tenga en cuenta que el codigo solo es valido durante 15 minutos"
        );

        return usuarioMapper.toUserResponse(usuario);
    }

    @Override
    public UsuarioResponse updateUsuario(String id, UsuarioUpdateRequest usuarioUpdateRequest) {
        var usuario = findUsuarioByID(id);
        if (usuario == null){
            // lanza una exception
            throw new UserNotFoundException("No se encontro el usuario");
        }
        usuario.setNombre(usuarioUpdateRequest.newNombre());
        usuario.setCiudad(usuarioUpdateRequest.newCiudad());
        usuario.setDireccion(usuarioUpdateRequest.newDireccion());
        usuario.setTelefono(usuarioUpdateRequest.newTelefono());
        userRepository.save(usuario);

        return usuarioMapper.toUserResponse(usuario);
    }

    @Override
    public UsuarioResponse deleteUsuario(String id) {
        var usuario = findUsuarioByID(id);
        if (usuario == null){
            throw new UserNotFoundException("Usuario no encontrado");
        }
        usuario.setStatus(StatusUsuario.ELIMINADO);
        userRepository.save(usuario);
        return usuarioMapper.toUserResponse(usuario);
    }

    @Override
    public UsuarioResponse getUsuario(String id) {
        var usuario = findUsuarioByID(id);
        if (usuario == null){
            throw new UserNotFoundException("El usuario no existe");
        }
        return usuarioMapper.toUserResponse(usuario);
    }

    @Override
    public List<Notificacion> getNotificacionesUsuario(String id) {
        var usuario = findUsuarioByID(id);
        if (usuario == null){
            throw new UserNotFoundException("El usuario no existe");
        }
        return usuario.getNotificaciones();
    }

//------------------------------------METODOS AUXILIARES ---------------------------------------//

    private UsuarioStandar findUsuarioByEmail(String Email) {
        return userRepository.findExistingUserByEmail(Email).orElse(null);
    }

    private UsuarioStandar findUsuarioByID(String id) {
        return userRepository.findUserByID(id).orElse(null);
    }

    private String generarCodigo() {
        int tam = 6;
        StringBuilder sb = new StringBuilder(tam);
        for (int i = 0; i < tam; i++) {
            int digito = random.nextInt(10); // genera un número entre 0 y 9
            sb.append(digito);
        }
        return sb.toString();
    }
}
