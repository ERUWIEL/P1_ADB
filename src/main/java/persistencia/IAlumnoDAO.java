
package persistencia;

import DTO.GuardarAlumnoDTO;
import dominio.AlumnoDominio;

/**
 *
 * @author Laboratorios
 */
public interface IAlumnoDAO {
    AlumnoDominio guardar(GuardarAlumnoDTO alumno) throws PersistenciaException;
    AlumnoDominio buscarPorId(int id) throws PersistenciaException;
    
    /**
     * modificar <- dto return AlumnoTablaDTO
     * buscarTabla <- filtros return AlumnoTablaDTO
     * eliminar <- id return AlumnoDominio
     */
}
