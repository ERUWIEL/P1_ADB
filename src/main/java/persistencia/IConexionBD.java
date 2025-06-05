package persistencia;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConexionBD {

    Connection crearConexion() throws SQLException;
    
}
