package services.impl;

import dao.JuegosDao;
import models.Juegos;
import services.JuegosService;

import java.util.List;
import java.util.Optional;

public class JuegosServiceImpl implements JuegosService {

    private final JuegosDao juegosDao;

    public JuegosServiceImpl(JuegosDao juegosDao) {
        this.juegosDao = juegosDao;
    }

    @Override
    public void addJuego(Juegos juego) {
        juegosDao.save(juego);
    }

    @Override
    public void updateJuego(Juegos juego) {
        juegosDao.update(juego);
    }

    @Override
    public void deleteJuego(String id) {
        juegosDao.delete(id);
    }

    @Override
    public Optional<Juegos> getJuegoById(String id) {
        return juegosDao.findById(id);
    }

    @Override
    public List<Juegos> getAllJuegos() {
        return juegosDao.findAll();
    }
}
