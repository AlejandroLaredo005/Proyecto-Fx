package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class Biblioteca {

  @Id
  @Column(name = "id_biblioteca")
  private String idBiblioteca;

  @ManyToOne
  @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
  private Usuarios idUsuario;

  @ManyToOne
  @JoinColumn(name = "id_juego", referencedColumnName = "id_juego")
  private Juegos idJuego;

  @Column(name = "comentarios")
  private String comentarios;

  @Column(name = "estado")
  private String estado;

  // Constructor sin parámetros
  public Biblioteca() {}

  // Constructor con parámetros
  public Biblioteca(String idBiblioteca, Usuarios idUsuario, Juegos idJuego, String comentarios, String estado) {
    this.idBiblioteca = idBiblioteca;
    this.idUsuario = idUsuario;
    this.idJuego = idJuego;
    this.comentarios = comentarios;
    this.estado = estado;
  }

  // Getters y setters
  public String getIdBiblioteca() {
    return idBiblioteca;
  }

  public void setIdBiblioteca(String idBiblioteca) {
    this.idBiblioteca = idBiblioteca;
  }

  public Usuarios getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Usuarios idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Juegos getIdJuego() {
    return idJuego;
  }

  public void setIdJuego(Juegos idJuego) {
    this.idJuego = idJuego;
  }

  public String getComentarios() {
    return comentarios;
  }

  public void setComentarios(String comentarios) {
    this.comentarios = comentarios;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  @Override
  public String toString() {
    return "Biblioteca{" +
           "idBiblioteca='" + idBiblioteca + '\'' +
           ", idUsuario=" + idUsuario +
           ", idJuego=" + idJuego +
           ", comentarios='" + comentarios + '\'' +
           ", estado='" + estado + '\'' +
           '}';
  }
}
