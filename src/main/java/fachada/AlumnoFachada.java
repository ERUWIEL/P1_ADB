
package fachada;

import DTO.GuardarAlumnoDTO;
import DTO.ModificarAlumnoDTO;
import dominio.AlumnoDominio;
import negocio.AlumnoNegocio;
import negocio.NegocioException;
import persistencia.AlumnoDAO;
import persistencia.ConexionBD;
import persistencia.IAlumnoDAO;
import persistencia.IConexionBD;

/**
 *
 * @author angel
 */
public class AlumnoFachada implements IAlumnoFachada {
    
    private static AlumnoNegocio alumnoNegocio;
    
    public AlumnoFachada(){
        IConexionBD conexion = new ConexionBD();
        IAlumnoDAO alumnoDAO = new AlumnoDAO(conexion);
        alumnoNegocio = new AlumnoNegocio(alumnoDAO);
    }
    
    public void guardarAlumno() throws NegocioException{
        alumnoNegocio.guardar(new GuardarAlumnoDTO("Angel", "Flores", "Campoy"));
    }
    
    public void buscarAlumno() throws NegocioException{
        AlumnoDominio alumno = alumnoNegocio.buscarPorId(2);
        System.out.println(alumno.getId());
        System.out.println(alumno.getNombre());
        System.out.println(alumno.getApellidoPaterno());
        System.out.println(alumno.getApellidoMaterno());
    }
    
    public void modificar() throws NegocioException {
        alumnoNegocio.modificar(new ModificarAlumnoDTO(1, "ERIKA", "Gastelum", "Vazques"));
    }
    
}
