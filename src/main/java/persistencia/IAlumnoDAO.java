
package persistencia;

import DTO.*;
import dominio.AlumnoDominio;

/**
 * Interfaz que describe las operaciones CRUD para alumnos
 * @author Laboratorios
 */
public interface IAlumnoDAO {
    TablaAlumnoDTO buscarTabla(FiltroTablaAlumnoDTO filtros) throws PersistenciaException;
    AlumnoDominio guardar(GuardarAlumnoDTO alumno) throws PersistenciaException;
    AlumnoDominio buscarPorId(int id) throws PersistenciaException;
    AlumnoDominio modificar(ModificarAlumnoDTO alumno) throws PersistenciaException;
    AlumnoDominio eliminar(int id) throws PersistenciaException;
}
