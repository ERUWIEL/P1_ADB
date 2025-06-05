package persistencia;

import DTO.GuardarAlumnoDTO;
import dominio.AlumnoDominio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Laboratorios
 */
public class AlumnoDAO implements IAlumnoDAO {

    private final IConexionBD conexion;

    public AlumnoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

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
            if(filasAfectadas == 0){
                throw new PersistenciaException("no se inserto alumno");
            }
            
            //formato de salida
            int id = 0;
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while(resultSet.next()){
                id = resultSet.getInt(1);
            }
            System.out.println(id);
            return null;
            
        } catch (SQLException ex) {
            throw new PersistenciaException("error al guardar " + ex.getMessage());
        }
    }

    @Override
    public AlumnoDominio buscarPorId(int id) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
