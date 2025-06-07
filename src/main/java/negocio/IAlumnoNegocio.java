
package negocio;

import DTO.GuardarAlumnoDTO;
import DTO.ModificarAlumnoDTO;
import dominio.AlumnoDominio;

/**
 * interfaz que modela las funciones de nuestro alumno dentro de nuestro negocio
 * @author erwybiel
 */
public interface IAlumnoNegocio {
    AlumnoDominio guardar(GuardarAlumnoDTO alumno) throws NegocioException;
    AlumnoDominio buscarPorId(int id) throws NegocioException;
    AlumnoDominio modificar(ModificarAlumnoDTO alumno) throws NegocioException;
    AlumnoDominio eliminar(int id) throws NegocioException;
}
