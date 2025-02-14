package models;

import java.io.Serializable;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

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

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;


    @Transient // No se almacena en la base de datos
    private String imagenUrl;

    @Transient // Este campo se usa para almacenar los tags extraídos de la API
    private String tags;

    @Transient // Campo para almacenar los géneros
    private String generosString;

    @Transient // Campo que almacena los géneros como lista
    private List<String> generos;

    public Juegos() {}

    public Juegos(String nombreJuego, String puntuacionMetacritic, String descripcion, String imagenUrl, String tags, String generosString) {
        this.nombreJuego = nombreJuego;
        this.puntuacionMetacritic = puntuacionMetacritic;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.tags = tags;
        this.generosString = generosString;
    
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
        // Convertir tags a lista si es necesario
        if (tags != null && !tags.isEmpty()) {
            this.generos = Arrays.stream(tags.split(","))
                                 .map(String::trim)
                                 .collect(Collectors.toList());
        }
    }

    public List<String> getGeneros() {
        return generos;
    }

    public void setGeneros(List<String> generos) {
        this.generos = generos;
        // Convertir la lista de géneros a una cadena de texto si se desea almacenar como String
        if (generos != null) {
            this.generosString = String.join(",", generos);
        }
    }

    public String getGenerosString() {
        return generosString;
    }

    public void setGenerosString(String generosString) {
        this.generosString = generosString;
        // Convertir la cadena de géneros a lista
        if (generosString != null && !generosString.isEmpty()) {
            this.generos = Arrays.stream(generosString.split(","))
                                 .map(String::trim)
                                 .collect(Collectors.toList());
        }
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
                ", generos=" + generos +
                '}';
    }
}
