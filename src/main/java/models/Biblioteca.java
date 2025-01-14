package models;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import java.io.Serializable;

@Entity
@Table(name = "biblioteca")
public class Biblioteca implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_biblioteca")
    private Integer idBiblioteca;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    @Cascade(CascadeType.ALL)
    private Usuarios usuario;

    @ManyToOne
    @JoinColumn(name = "id_juego", referencedColumnName = "id_juego", nullable = false)
    @Cascade(CascadeType.ALL)
    private Juegos juego;

    @Column(name = "comentarios")
    private String comentarios;

    @Column(name = "estado", nullable = false)
    private String estado;

    // Constructor vacio
    public Biblioteca() {}

    // Constructor con parametros
    public Biblioteca(Usuarios usuario, Juegos juego, String comentarios, String estado) {
        this.usuario = usuario;
        this.juego = juego;
        this.comentarios = comentarios;
        this.estado = estado;
    }

    // Getters y Setters
    public Integer getIdBiblioteca() {
        return idBiblioteca;
    }

    public void setIdBiblioteca(Integer idBiblioteca) {
        this.idBiblioteca = idBiblioteca;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Juegos getJuego() {
        return juego;
    }

    public void setJuego(Juegos juego) {
        this.juego = juego;
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
                "idBiblioteca=" + idBiblioteca +
                ", usuario=" + usuario +
                ", juego=" + juego +
                ", comentarios='" + comentarios + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
