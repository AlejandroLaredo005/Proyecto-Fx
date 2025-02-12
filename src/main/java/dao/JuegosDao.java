package dao;

import java.util.Optional;

import models.Juegos;

public interface JuegosDao extends CommonDao<Juegos, String> {
    
  Optional<Juegos> findByNombre(String nombreJuego);
}
