
package persistencia;

import DTO.*;
import dominio.AlumnoDominio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que modela los metodos Crud de alumnos
 *
 * @author Laboratorios
 */

public class AlumnoDAO implements IAlumnoDAO {

    private final IConexionBD conexion;

    /**
     * metodo constructor que permite instanciar un nuevo objeto
     *
     * @param conexion
     */
    public AlumnoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    /**
     * Metodo que permite guardar un GuardarAlumnoDTO en la base de datos
     *
     * @param alumno
     * @return
     * @throws PersistenciaException
     */
    @Override
    public AlumnoDominio guardar(GuardarAlumnoDTO alumno) throws PersistenciaException {
        try {
            Connection connection = this.conexion.crearConexion(); // coneccion con la BD

            //aplicacion de la operacion SQL
            String query = """
                           INSERT INTO alumnos(nombre,apellidoPaterno,apellidoMaterno) VALUES(?,?,?);
                           """;
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, alumno.getNombre());
            preparedStatement.setString(2, alumno.getApellidoPaterno());
            preparedStatement.setString(3, alumno.getApellidoMaterno());

            // evaluacion de resultados
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas == 0) {
                throw new PersistenciaException("no se inserto alumno");
            }

            //formato de salida
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            AlumnoDominio alumnoBuscado = null;
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                alumnoBuscado = this.buscarPorId(id);
            }

            //cierre de canales
            resultSet.close();
            preparedStatement.close();
            connection.close();

            if (alumnoBuscado == null) {
                throw new PersistenciaException("No se encontró el alumno guardado");
            }
            return alumnoBuscado;

        } catch (SQLException ex) {
            throw new PersistenciaException("error al guardar " + ex.getMessage());
        }
    }

    /**
     * Metodo que permite buscar un Alumno en la base de datos
     *
     * @param id
     * @return
     * @throws PersistenciaException
     */
    @Override
    public AlumnoDominio buscarPorId(int id) throws PersistenciaException {
        try {
            //conexion y setteo de variables
            Connection connection = this.conexion.crearConexion();
            String query = """
                           SELECT * FROM alumnos WHERE id = ?;
                           """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            //ejecucion y resultados
            ResultSet resultSet = preparedStatement.executeQuery();
            AlumnoDominio alumno = null;
            while (resultSet.next()) {
                alumno = this.convertirAlumnoDominio(resultSet);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

            if (alumno == null) {
                throw new PersistenciaException("No se encontró el alumno con id " + id);
            }
            return alumno;

        } catch (SQLException ex) {
            throw new PersistenciaException("Ocurrion un error en leer un alumno " + ex.getMessage());
        }
    }

    /**
     * Metodo que permite modificar un registro
     *
     * @param alumno
     * @return
     * @throws PersistenciaException
     */
    @Override
    public AlumnoDominio modificar(ModificarAlumnoDTO alumno) throws PersistenciaException {
        try {
            // precondiciones
            this.buscarPorId(alumno.getId());

            //conexion y setteo de variables
            Connection connection = this.conexion.crearConexion();
            String query = """
                       UPDATE alumnos
                       SET 
                           nombre = ?,
                           apellidoPaterno = ?,
                           apellidoMaterno = ?
                       WHERE id = ?;
                       """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, alumno.getNombre());
            preparedStatement.setString(2, alumno.getApellidoPaterno());
            preparedStatement.setString(3, alumno.getApellidoMaterno());
            preparedStatement.setInt(4, alumno.getId());
            //ejecucion y resultados
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas == 0) {
                throw new PersistenciaException("alumno no modificado");
            }
            //retorno del nuevo valor
            AlumnoDominio alumnoActualizado = this.buscarPorId(alumno.getId());
            preparedStatement.close();
            connection.close();
            return alumnoActualizado;

        } catch (SQLException ex) {
            throw new PersistenciaException("error al modificar alumno: " + ex.getMessage());
        }
    }

    /**
     * Metodo que permite eliminar un alumno existente
     *
     * @param id
     * @return
     * @throws PersistenciaException
     */
    @Override
    public AlumnoDominio eliminar(int id) throws PersistenciaException {
        try {
            // precondiciones
            AlumnoDominio alumno = this.buscarPorId(id);

            //conexion y setteo de variables
            Connection connection = this.conexion.crearConexion();
            String query = """
                        UPDATE alumnos
                        SET 
                            nombre = ?,
                            apellidoPaterno = ?,
                            apellidoMaterno = ?,
                            estaActivo = ?,
                            estaEliminado = ?
                        WHERE id = ?;
                           """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, alumno.getNombre());
            preparedStatement.setString(2, alumno.getApellidoPaterno());
            preparedStatement.setString(3, alumno.getApellidoMaterno());
            preparedStatement.setBoolean(4, false);
            preparedStatement.setBoolean(5, true);
            preparedStatement.setInt(6, id);
            
            //ejecucion y resultados
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas == 0) {
                throw new PersistenciaException("alumno no eliminado");
            }
            preparedStatement.close();
            connection.close();
            return alumno;
        } catch (SQLException ex) {
            throw new PersistenciaException("error al eliminar " + ex.getMessage());
        }
    }

    /**
     * metodo que permite buscar tabla
     *
     * @param filtros
     * @return
     * @throws PersistenciaException
     */
    @Override
    public TablaAlumnoDTO buscarTabla(FiltroTablaAlumnoDTO filtros) throws PersistenciaException {
        try {
            Connection connection = this.conexion.crearConexion();
            String query = """
                       SELECT * FROM alumnos
                       WHERE (nombre LIKE ? OR apellidoPaterno LIKE ? OR apellidoMaterno LIKE ?)
                       AND estaEliminado = false
                       LIMIT ? OFFSET ?;
                       """;
            String patronBusqueda = "%" + filtros.getPatron() + "%";
            
            List<AlumnoDominio> alumnos = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, patronBusqueda);
            preparedStatement.setString(2, patronBusqueda);
            preparedStatement.setString(3, patronBusqueda);
            preparedStatement.setInt(4, filtros.getLimit());
            preparedStatement.setInt(5, filtros.getOffset());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                alumnos.add(this.convertirAlumnoDominio(resultSet));
            }
            preparedStatement.close();
            connection.close();
            return new TablaAlumnoDTO(alumnos);
        } catch (SQLException ex) {
            throw new PersistenciaException("error al consultar " + ex.getMessage());
        }
    }

    /**
     * Metodo que permite dar formato a un alumno
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private AlumnoDominio convertirAlumnoDominio(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String nombre = resultSet.getString("nombre");
        String apellidoPaterno = resultSet.getString("apellidoPaterno");
        String apellidoMaterno = resultSet.getString("apellidoMaterno");
        boolean estaActivo = resultSet.getBoolean("estaActivo");
        boolean estaEliminado = resultSet.getBoolean("estaEliminado");
        return new AlumnoDominio(id, nombre, apellidoPaterno, apellidoMaterno, estaActivo, estaEliminado);
    }
}
