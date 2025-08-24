package com.xlogant.login;

import static com.xlogant.controlador.ControlaUsuario.*;
import static com.xlogant.encripta.EncriptaGenerador.getHash;
import static java.lang.String.valueOf;

import java.util.Optional;

/**
 * Servicio encargado de la lógica de autenticación de usuarios.
 */
public class AuthenticationService {

    /**
     * Intenta autenticar a un usuario con su nombre y clave.
     *
     * @param username El nombre de usuario.
     * @param password La clave sin encriptar.
     * @return Un Optional que contiene el UsuarioDTO si la autenticación es exitosa y el usuario está activo,
     * de lo contrario, un Optional vacío.
     */
    public Optional<UsuarioDTO> authenticate(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return Optional.empty();
        }

        String encryptedPassword = getHash(password, 5);

        if (!login(username, encryptedPassword)) {
            return Optional.empty(); // Usuario o clave incorrectos
        }

        if (!usuarioActivo(username, encryptedPassword)) {
            return Optional.empty(); // Usuario inactivo
        }

        // Si llegamos aquí, el usuario es válido y está activo.
        // Recopilamos sus datos.
        int id = obtenerID(username, encryptedPassword, true);
        String rol = obtenerRole(username, encryptedPassword, true);
        long idEmpresa = obtenerIDEmp(username, encryptedPassword, true);
        String nombreCompleto = obtenerPorNombre(username, encryptedPassword, true);
        int idSucursal = IDSUCURSAL(username, encryptedPassword);
        String nombreSucursal = NombreSuc(username, encryptedPassword, idSucursal);

        UsuarioDTO usuario = new UsuarioDTO(id, username, nombreCompleto, rol, idEmpresa, nombreSucursal);

        // Aquí podrías insertar el registro de ingreso a la BD
        // insertaIngreso(...);

        return Optional.of(usuario);
    }
}