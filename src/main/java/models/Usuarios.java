package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import java.util.List;

@Entity
public class Usuarios {

  @Id
  @Column(name = "id_usuario")
  private String id;

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "apellidos")
  private String apellidos;

  @Column(name = "nombre_usuario")
  private String nombreUsuario;

  @Column(name = "contraseña")
  private String contraseña;

  @Column(name = "correo")
  private String correo;

  @OneToMany(mappedBy = "idUsuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Biblioteca> biblioteca;

  // Constructor sin parámetros
  public Usuarios() {}

  // Constructor con parámetros
  public Usuarios(String id, String nombre, String apellidos, String nombreUsuario, String contraseña, String correo) {
    this.id = id;
    this.nombre = nombre;
    this.apellidos = apellidos;
    this.nombreUsuario = nombreUsuario;
    this.contraseña = contraseña;
    this.correo = correo;
  }

  // Getters y setters
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellidos() {
    return apellidos;
  }

  public void setApellidos(String apellidos) {
    this.apellidos = apellidos;
  }

  public String getNombreUsuario() {
    return nombreUsuario;
  }

  public void setNombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
  }

  public String getContraseña() {
    return contraseña;
  }

  public void setContraseña(String contraseña) {
    this.contraseña = contraseña;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public List<Biblioteca> getBiblioteca() {
    return biblioteca;
  }

  public void setBiblioteca(List<Biblioteca> biblioteca) {
    this.biblioteca = biblioteca;
  }

  @Override
  public String toString() {
    return "Usuarios{" +
           "id='" + id + '\'' +
           ", nombre='" + nombre + '\'' +
           ", apellidos='" + apellidos + '\'' +
           ", nombreUsuario='" + nombreUsuario + '\'' +
           ", contraseña='" + contraseña + '\'' +
           ", correo='" + correo + '\'' +
           '}';
  }
}
