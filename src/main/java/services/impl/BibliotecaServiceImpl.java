package services.impl;

import dao.BibliotecaDao;
import models.Biblioteca;
import services.BibliotecaService;

import java.util.List;
import java.util.Optional;

public class BibliotecaServiceImpl implements BibliotecaService {

    private final BibliotecaDao bibliotecaDao;

    public BibliotecaServiceImpl(BibliotecaDao bibliotecaDao) {
        this.bibliotecaDao = bibliotecaDao;
    }

    @Override
    public void addBiblioteca(Biblioteca biblioteca) {
        // Validación o lógica adicional si es necesario
        bibliotecaDao.save(biblioteca);
    }

    @Override
    public void updateBiblioteca(Biblioteca biblioteca) {
        // Validación o lógica adicional si es necesario
        bibliotecaDao.update(biblioteca);
    }

    @Override
    public void deleteBiblioteca(String id) {
        // Validación o lógica adicional si es necesario
        bibliotecaDao.delete(id);
    }

    @Override
    public Optional<Biblioteca> getBibliotecaById(String id) {
        return bibliotecaDao.findById(id);
    }

    @Override
    public List<Biblioteca> getAllBibliotecas() {
        return bibliotecaDao.findAll();
    }
}
