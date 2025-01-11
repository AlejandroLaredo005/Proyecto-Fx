package services.impl;

import dao.UsuariosDao;
import models.Usuarios;
import services.UsuariosService;

import java.util.List;
import java.util.Optional;

public class UsuariosServiceImpl implements UsuariosService {

    private final UsuariosDao usuariosDao;

    public UsuariosServiceImpl(UsuariosDao usuariosDao) {
        this.usuariosDao = usuariosDao;
    }

    @Override
    public void addUsuario(Usuarios usuario) {
        usuariosDao.save(usuario);
    }

    @Override
    public void updateUsuario(Usuarios usuario) {
        usuariosDao.update(usuario);
    }

    @Override
    public void deleteUsuario(String id) {
        usuariosDao.delete(id);
    }

    @Override
    public Optional<Usuarios> getUsuarioById(String id) {
        return usuariosDao.findById(id);
    }

    @Override
    public List<Usuarios> getAllUsuarios() {
        return usuariosDao.findAll();
    }
}
