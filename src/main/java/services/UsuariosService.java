package services;

import models.Usuarios;

import java.util.List;
import java.util.Optional;

public interface UsuariosService {

    void addUsuario(Usuarios usuario);

    void updateUsuario(Usuarios usuario);

    void deleteUsuario(String id);

    Optional<Usuarios> getUsuarioById(String id);

    List<Usuarios> getAllUsuarios();
}
