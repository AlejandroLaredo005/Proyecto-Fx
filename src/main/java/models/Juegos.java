package models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

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
    private String puntuacionMetacritic;

    @Column(name = "descripcion")
    private String descripcion;

    @Transient // No se almacena en la base de datos
    private String imagenUrl;
    
    @Transient // Este campo se usa para almacenar los tags extraídos de la API
    private String tags;

    public Juegos() {}

    public Juegos(String nombreJuego, String puntuacionMetacritic, String descripcion, String imagenUrl, String tags) {
        this.nombreJuego = nombreJuego;
        this.puntuacionMetacritic = puntuacionMetacritic;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.tags = tags;
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

    public String getPuntuacionMetacritic() {
        return puntuacionMetacritic;
    }

    public void setPuntuacionMetacritic(String puntuacionMetacritic) {
        this.puntuacionMetacritic = puntuacionMetacritic;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
    
    public String getTags() {
        return tags;
    }
    
    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Juegos{" +
                "idJuego=" + idJuego +
                ", nombreJuego='" + nombreJuego + '\'' +
                ", puntuacionMetacritic='" + puntuacionMetacritic + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagenUrl='" + imagenUrl + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}
