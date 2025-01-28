package models;

import jakarta.persistence.*;
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

    @Transient // No se almacena en la base de datos
    private String imagenUrl;

    public Juegos() {}

    public Juegos(String nombreJuego, Integer puntuacionMetacritic, String fechaSalida, String descripcion, String categorias, String juegosRelacionados, String imagenUrl) {
        this.nombreJuego = nombreJuego;
        this.puntuacionMetacritic = puntuacionMetacritic;
        this.descripcion = descripcion;
        this.categorias = categorias;
        this.juegosRelacionados = juegosRelacionados;
        this.imagenUrl = imagenUrl;
    }

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

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Date getFechaSalida() {
      return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
      this.fechaSalida = fechaSalida;
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
                ", imagenUrl='" + imagenUrl + '\'' +
                '}';
    }
}
