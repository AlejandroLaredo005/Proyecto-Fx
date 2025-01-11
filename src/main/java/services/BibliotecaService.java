package services;

import models.Biblioteca;

import java.util.List;
import java.util.Optional;

public interface BibliotecaService {

    void addBiblioteca(Biblioteca biblioteca);

    void updateBiblioteca(Biblioteca biblioteca);

    void deleteBiblioteca(String id);

    Optional<Biblioteca> getBibliotecaById(String id);

    List<Biblioteca> getAllBibliotecas();
}
