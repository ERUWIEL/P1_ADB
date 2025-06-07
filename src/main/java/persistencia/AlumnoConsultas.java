package persistencia;

/**
 * clase que contiene las consultas SQL esenciales
 * en formato de cadenas
 * @author erwbyel
 */
public class AlumnoConsultas {

    /**
     * Sentencia SQL para inserción de alumnos.
     * <p>
     * parámetros
     * <ol>
     * <li>nombre (String)</li>
     * <li>apellidoPaterno (String)</li>
     * <li>apellidoMaterno (String)</li>
     * </ol>
     *
     * @see persistencia.AlumnoDAO#guardar(GuardarAlumnoDTO)
     */
    public static final String INSERT_QUERY = """
                                              INSERT 
                                              INTO alumnos(nombre,apellidoPaterno,apellidoMaterno) 
                                              VALUES(?,?,?); """;

    /**
     * Sentencia SQL para consulta de alumnos por id.
     * <p>
     * parámetros
     * <ol>
     * <li>id (int)</li>
     * </ol>
     *
     * @see persistencia.AlumnoDAO#buscarPorId(int)
     */
    public static final String SELECT_ID_QUERY = """
                                            SELECT * 
                                            FROM alumnos WHERE id = ?; """;

    /**
     * Sentencia SQL para modificar alumnos por id.
     * <p>
     * parámetros
     * <ol>
     * <li>nombre (String)</li>
     * <li>apellidoPaterno (String)</li>
     * <li>apellidoMaterno (String)</li>
     * <li>id de dato a modificar(int)</li>
     * </ol>
     *
     * @see persistencia.AlumnoDAO#modificar(ModificarAlumnoDTO)
     */
    public static final String UPDATE_QUERY = """
                                            UPDATE alumnos
                                            SET
                                                nombre = ?,
                                                apellidoPaterno = ?,
                                                apellidoMaterno = ?
                                            WHERE id = ?; """;

    /**
     * Sentencia SQL para settear un alumno a eliminado.
     * <p>
     * parámetros
     * <ol>
     * <li>nombre (String)</li>
     * <li>apellidoPaterno (String)</li>
     * <li>apellidoMaterno (String)</li>
     * <li>estaActivo(boolean)</li>
     * <li>estaEliminado (boolean)</li>
     * <li>id del alumno(int)</li>
     * </ol>
     *
     * @see persistencia.AlumnoDAO#eliminar(int)
     */
    public static final String SET_ELIMINADO_QUERY = """
                                            UPDATE alumnos
                                            SET
                                                nombre = ?,
                                                apellidoPaterno = ?,
                                                apellidoMaterno = ?,
                                                estaActivo = ?,
                                                estaEliminado = ?
                                            WHERE id = ?; """;

    /**
     * Sentencia SQL para la consultar de registros filtrados
     * con limites y saltos
     * <p>
     * parámetros
     * <ol>
     * <li>patron del nombre (String)</li>
     * <li>patron del apellidoPaterno (String)</li>
     * <li>patron del apellidoMaterno (String)</li>
     * <li>limit (int)</li>
     * <li>offset(int)</li>
     * </ol>
     *
     * @see persistencia.AlumnoDAO#eliminar(int)
     */
    public static final String CONSULTAR_FILTRO = """
                                            SELECT * FROM alumnos
                                            WHERE (nombre LIKE ? OR apellidoPaterno LIKE ? OR apellidoMaterno LIKE ?)
                                            AND estaEliminado = false
                                            LIMIT ? OFFSET ?; """;
}
