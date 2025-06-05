package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD implements IConexionBD {

    private final String SERVER = "127.0.0.1";
    private final String BASE_DATOS = "registro";
    private final String CADENA_CONEXION = "jdbc:mysql://" + SERVER + "/" + BASE_DATOS;
    private final String USUARIO = "root";
    private final String CONTRASEÑA = "ITSON";

    @Override
    public Connection crearConexion() throws SQLException {
        Connection conexion = DriverManager.getConnection(CADENA_CONEXION, USUARIO, CONTRASEÑA);
        return conexion;
    }

}
