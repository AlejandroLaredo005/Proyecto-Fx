package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import java.util.List;
import java.util.Date;

@Entity
public class Juegos {

  @Id
  @Column(name = "id_juego")
  private String idJuego;

  @Column(name = "nombre_juego")
  private String nombreJuego;

  @Column(name = "puntuacion_metacritic")
  private Double puntuacionMetacritic;

  @Column(name = "fecha_salida")
  private Date fechaSalida;

  @Column(name = "descripcion")
  private String descripcion;

  @Column(name = "categorias")
  private String categorias;

  @Column(name = "juegos_relacionados")
  private String juegosRelacionados;

  @OneToMany(mappedBy = "idJuego", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Biblioteca> biblioteca;

  // Constructor sin parámetros
  public Juegos() {}

  // Constructor con parámetros
  public Juegos(String idJuego, String nombreJuego, Double puntuacionMetacritic, Date fechaSalida, String descripcion, String categorias, String juegosRelacionados) {
    this.idJuego = idJuego;
    this.nombreJuego = nombreJuego;
    this.puntuacionMetacritic = puntuacionMetacritic;
    this.fechaSalida = fechaSalida;
    this.descripcion = descripcion;
    this.categorias = categorias;
    this.juegosRelacionados = juegosRelacionados;
  }

  // Getters y setters
  public String getIdJuego() {
    return idJuego;
  }

  public void setIdJuego(String idJuego) {
    this.idJuego = idJuego;
  }

  public String getNombreJuego() {
    return nombreJuego;
  }

  public void setNombreJuego(String nombreJuego) {
    this.nombreJuego = nombreJuego;
  }

  public Double getPuntuacionMetacritic() {
    return puntuacionMetacritic;
  }

  public void setPuntuacionMetacritic(Double puntuacionMetacritic) {
    this.puntuacionMetacritic = puntuacionMetacritic;
  }

  public Date getFechaSalida() {
    return fechaSalida;
  }

  public void setFechaSalida(Date fechaSalida) {
    this.fechaSalida = fechaSalida;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getCategorias() {
    return categorias;
  }

  public void setCategorias(String categorias) {
    this.categorias = categorias;
  }

  public String getJuegosRelacionados() {
    return juegosRelacionados;
  }

  public void setJuegosRelacionados(String juegosRelacionados) {
    this.juegosRelacionados = juegosRelacionados;
  }

  public List<Biblioteca> getBiblioteca() {
    return biblioteca;
  }

  public void setBiblioteca(List<Biblioteca> biblioteca) {
    this.biblioteca = biblioteca;
  }

  @Override
  public String toString() {
    return "Juegos{" +
           "idJuego='" + idJuego + '\'' +
           ", nombreJuego='" + nombreJuego + '\'' +
           ", puntuacionMetacritic=" + puntuacionMetacritic +
           ", fechaSalida=" + fechaSalida +
           ", descripcion='" + descripcion + '\'' +
           ", categorias='" + categorias + '\'' +
           ", juegosRelacionados='" + juegosRelacionados + '\'' +
           '}';
  }
}
