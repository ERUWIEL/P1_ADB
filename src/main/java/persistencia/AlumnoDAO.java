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
            AlumnoDominio alumnoBuscado;
            try (Connection connection = this.conexion.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(AlumnoConsultas.INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, alumno.getNombre());
                preparedStatement.setString(2, alumno.getApellidoPaterno());
                preparedStatement.setString(3, alumno.getApellidoMaterno());

                int filasAfectadas = preparedStatement.executeUpdate();
                if (filasAfectadas == 0) {
                    throw new PersistenciaException("no se inserto alumno");
                }
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    alumnoBuscado = null;
                    while (resultSet.next()) {
                        int id = resultSet.getInt(1);
                        alumnoBuscado = this.buscarPorId(id);
                    }
                }
            }
            if (alumnoBuscado == null) {
                throw new PersistenciaException("no se encontro el alumno guardado");
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
            AlumnoDominio alumno;
            try (Connection connection = this.conexion.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(AlumnoConsultas.SELECT_ID_QUERY)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    alumno = null;
                    while (resultSet.next()) {
                        alumno = this.convertirAlumnoDominio(resultSet);
                    }
                }
            }
            if (alumno == null) {
                throw new PersistenciaException("no se encontro el alumno con id " + id);
            }
            return alumno;
        } catch (SQLException ex) {
            throw new PersistenciaException("ocurrion un error en leer un alumno " + ex.getMessage());
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
            this.buscarPorId(alumno.getId());

            AlumnoDominio alumnoActualizado;
            try (Connection connection = this.conexion.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(AlumnoConsultas.UPDATE_QUERY)) {
                preparedStatement.setString(1, alumno.getNombre());
                preparedStatement.setString(2, alumno.getApellidoPaterno());
                preparedStatement.setString(3, alumno.getApellidoMaterno());
                preparedStatement.setInt(4, alumno.getId());

                int filasAfectadas = preparedStatement.executeUpdate();
                if (filasAfectadas == 0) {
                    throw new PersistenciaException("alumno no modificado");
                }
                alumnoActualizado = this.buscarPorId(alumno.getId());
            }
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
            AlumnoDominio alumno = this.buscarPorId(id);

            try (Connection connection = this.conexion.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(AlumnoConsultas.SET_ELIMINADO_QUERY)) {
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
            }
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
            String patronBusqueda = "%" + filtros.getPatron() + "%";
            List<AlumnoDominio> alumnos = new ArrayList<>();
            
            try (Connection connection = this.conexion.crearConexion(); PreparedStatement preparedStatement = connection.prepareStatement(AlumnoConsultas.CONSULTAR_FILTRO);) {
                preparedStatement.setString(1, patronBusqueda);
                preparedStatement.setString(2, patronBusqueda);
                preparedStatement.setString(3, patronBusqueda);
                preparedStatement.setInt(4, filtros.getLimit());
                preparedStatement.setInt(5, filtros.getOffset());
                
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    alumnos.add(this.convertirAlumnoDominio(resultSet));
                }
            }
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
