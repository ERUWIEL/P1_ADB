package DTO;

import dominio.AlumnoDominio;
import java.util.List;

/**
 *
 * @author angel
 */
public class TablaAlumnoDTO {
    private List<AlumnoDominio> alumnos;

    // Constructor, getters y setters
    public TablaAlumnoDTO(List<AlumnoDominio> alumnos) {
        this.alumnos = alumnos;
    }

    // Getters
    public List<AlumnoDominio> getAlumnos() {
        return alumnos;
    }
}
