package dao;

import java.util.Optional;

import models.Biblioteca;

public interface BibliotecaDao extends CommonDao<Biblioteca, String> {
    
  Optional<Biblioteca> findByUsuarioAndJuego(models.Usuarios usuario, models.Juegos juego);
}
