package services;

import models.Juegos;

import java.util.List;
import java.util.Optional;

public interface JuegosService {

    void addJuego(Juegos juego);

    void updateJuego(Juegos juego);

    void deleteJuego(String id);

    Optional<Juegos> getJuegoById(String id);

    List<Juegos> getAllJuegos();
}
