package models;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.List;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "juegos")
public class Juegos implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_juego")
    private Integer idJuego;

    @Column(name = "nombre_juego", nullable = false, unique = true)
    private String nombreJuego;

    @Column(name = "puntuacion_metacritic")
    private Integer puntuacionMetacritic;

    @Column(name = "fecha_salida", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaSalida;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "categorias")
    private String categorias;

    @Column(name = "juegos_relacionados")
    private String juegosRelacionados;

    @OneToMany(mappedBy = "juego", fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    private List<Biblioteca> biblioteca;

    // Constructor vacío
    public Juegos() {}

    // Constructor con parámetros
    public Juegos(String nombreJuego, Integer puntuacionMetacritic, Date fechaSalida, String descripcion, String categorias, String juegosRelacionados) {
        this.nombreJuego = nombreJuego;
        this.puntuacionMetacritic = puntuacionMetacritic;
        this.fechaSalida = fechaSalida;
        this.descripcion = descripcion;
        this.categorias = categorias;
        this.juegosRelacionados = juegosRelacionados;
    }

    // Getters y Setters
    public Integer getIdJuego() {
        return idJuego;
    }

    public void setIdJuego(Integer idJuego) {
        this.idJuego = idJuego;
    }

    public String getNombreJuego() {
        return nombreJuego;
    }

    public void setNombreJuego(String nombreJuego) {
        this.nombreJuego = nombreJuego;
    }

    public Integer getPuntuacionMetacritic() {
        return puntuacionMetacritic;
    }

    public void setPuntuacionMetacritic(Integer puntuacionMetacritic) {
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
                "idJuego=" + idJuego +
                ", nombreJuego='" + nombreJuego + '\'' +
                ", puntuacionMetacritic=" + puntuacionMetacritic +
                ", fechaSalida=" + fechaSalida +
                ", descripcion='" + descripcion + '\'' +
                ", categorias='" + categorias + '\'' +
                ", juegosRelacionados='" + juegosRelacionados + '\'' +
                '}';
    }
}
