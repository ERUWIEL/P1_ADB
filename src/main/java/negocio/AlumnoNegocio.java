package negocio;

import DTO.GuardarAlumnoDTO;
import DTO.ModificarAlumnoDTO;
import dominio.AlumnoDominio;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import persistencia.IAlumnoDAO;
import persistencia.PersistenciaException;

/**
 * clase que modela las reglas del negocio
 *
 * @author erwbyel
 */
public class AlumnoNegocio implements IAlumnoNegocio {

    private final IAlumnoDAO alumnoDAO;

    /**
     * metodo constructor de la clase
     * @param alumnoDAO 
     */
    public AlumnoNegocio(IAlumnoDAO alumnoDAO) {
        this.alumnoDAO = alumnoDAO;
    }

    @Override
    public AlumnoDominio guardar(GuardarAlumnoDTO alumno) throws NegocioException {
        try {
            //variables a validar
            String nombre = alumno.getNombre();
            String apellidoPaterno = alumno.getApellidoPaterno();
            String apellidoMaterno = alumno.getApellidoMaterno();
            //validaciones
            validarNombreNulo(nombre, apellidoPaterno, apellidoMaterno);
            validarNombreLongitud(nombre, apellidoPaterno, apellidoMaterno);
            validarNombreCaracteres(nombre, apellidoPaterno, apellidoMaterno);
            //ejecucion
            return this.alumnoDAO.guardar(alumno);
        } catch (PersistenciaException ex) {
            throw new NegocioException("error al implementar " + ex.getMessage());
        }
    }
    
    @Override
    public AlumnoDominio modificar(ModificarAlumnoDTO alumno) throws NegocioException {
        try {
            //variables a validar
            String nombre = alumno.getNombre();
            String apellidoPaterno = alumno.getApellidoPaterno();
            String apellidoMaterno = alumno.getApellidoMaterno();
            //validaciones
            validarNombreNulo(nombre, apellidoPaterno, apellidoMaterno);
            validarNombreLongitud(nombre, apellidoPaterno, apellidoMaterno);
            validarNombreCaracteres(nombre, apellidoPaterno, apellidoMaterno);
            //ejecucion
            return this.alumnoDAO.modificar(alumno);
        } catch (PersistenciaException ex) {
            throw new NegocioException("error al implementar " + ex.getMessage());
        }
    }

    @Override
    public AlumnoDominio buscarPorId(int id) throws NegocioException {
        try {
            return this.alumnoDAO.buscarPorId(id);
        } catch (PersistenciaException ex) {
            throw new NegocioException("error al implementar " + ex.getMessage());
        }
    }

    @Override
    public AlumnoDominio eliminar(int id) throws NegocioException {
        try {
            return this.alumnoDAO.eliminar(id);
        } catch (PersistenciaException ex) {
            throw new NegocioException("error al implementar " + ex.getMessage());
        }
    }

    
    //METODOS DE VALIDACION NEGOCIO
    
    /**
     * valida si el nombre utilizado contiene caracteres
     * @param nombre
     * @throws NegocioException 
     */
    private static void validarNombreNulo(String nombre, String apellidoPaterno, String apellidoMaterno)throws NegocioException {
        if(nombre == null || nombre.isBlank()){
            throw new NegocioException("el nombre usado debe contener caracteres");
        }
        if(apellidoPaterno == null || apellidoPaterno.isBlank()){
            throw new NegocioException("el apellido paterno usado debe contener caracteres");
        }
        if(apellidoMaterno == null || apellidoMaterno.isBlank()){
            throw new NegocioException("el apellido materno usado debe contener caracteres");
        }
    }
    
    /**
     * valida si el nombre utilizado contiene caracteres
     * @param nombre
     * @throws NegocioException 
     */
    private static void validarNombreLongitud(String nombre, String apellidoPaterno, String apellidoMaterno)throws NegocioException {
        if(nombre.length() > 70){
            throw new NegocioException("el nombre usado es demasiado grande");
        }
        if(apellidoPaterno.length() > 50){
            throw new NegocioException("el apellido paterno usado es demasiado grande");
        }
        if(apellidoMaterno.length() > 50){
            throw new NegocioException("el apellido materno usado es demasiado grande");
        }
    }
    
    /**
     * valida si el nombre utilizado contiene caracteres ilegales
     * @param nombre
     * @throws NegocioException 
     */
    private static void validarNombreCaracteres(String nombre, String apellidoPaterno, String apellidoMaterno)throws NegocioException {
        Pattern pattern = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]+$");
        Matcher mtNombre = pattern.matcher(nombre);
        Matcher mtApellidoPaterno = pattern.matcher(apellidoPaterno);
        Matcher mtApellidoMaterno = pattern.matcher(apellidoMaterno);
        
        if(!mtNombre.matches()){
            throw new NegocioException("el nombre usado contiene caracteres invalidos");
        }
        if(!mtApellidoPaterno.matches()){
            throw new NegocioException("el nombre usado contiene caracteres invalidos");
        }
        if(!mtApellidoMaterno.matches()){
            throw new NegocioException("el nombre usado contiene caracteres invalidos");
        }
    }
}
