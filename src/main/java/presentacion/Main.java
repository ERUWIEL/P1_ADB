package presentacion;

import DTO.FiltroTablaAlumnoDTO;
import DTO.GuardarAlumnoDTO;
import DTO.ModificarAlumnoDTO;
import DTO.TablaAlumnoDTO;
import dominio.AlumnoDominio;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import persistencia.*;

public class Main extends JFrame {

    public static void main(String[] args) throws PersistenciaException {
        new Main().setVisible(true);
        //pruebas para la conexion de DAO


        /**
         *         TablaAlumnoDTO tabla = alumnoDTO.buscarTabla(new FiltroTablaAlumnoDTO(5, 15, "Samiraaa"));
        tabla.getAlumnos().forEach(p -> {
            System.out.println(p.getId() + ": " + p.getNombre() + " " + p.getApellidoPaterno() + " " + p.getApellidoMaterno());
        });
         * AlumnoDominio alumno = alumnoDTO.modificar(new ModificarAlumnoDTO(1,
         * "Angelika", "Soto", "Guamo")); alumnoDTO.guardar(new
         * GuardarAlumnoDTO("Samiraaa", "Cassiopeiaaa","Martinez"));
         * AlumnoDominio alumno = alumnoDTO.eliminar(1);
         * System.out.println("ELIMINADO"); System.out.println(alumno.getId() +
         * ": " + alumno.getNombre() + " " + alumno.getApellidoPaterno() + " " +
         * alumno.getApellidoMaterno());
         */
    }

    public Main() throws PersistenciaException {
        setTitle("Pruebas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLayout(null);

        // creando columnas
        List<TablaPaginada.Columna<AlumnoDominio>> columnas = new ArrayList<>();
        columnas.add(new TablaPaginada.Columna<>("ID", Integer.class, p -> p.getId()));
        columnas.add(new TablaPaginada.Columna<>("Nombre", String.class, p -> p.getNombre()));
        columnas.add(new TablaPaginada.Columna<>("Apellido Paterno", String.class, p -> p.getApellidoPaterno()));
        columnas.add(new TablaPaginada.Columna<>("Apellido Materno", String.class, p -> p.getApellidoMaterno()));
        columnas.add(new TablaPaginada.Columna<>("Activo", boolean.class, p -> p.isEstaActivo()));

        //creando la tabla
        TablaPaginada<AlumnoDominio> tablaPaginada = new TablaPaginada<>(columnas);
        IConexionBD conexion = new ConexionBD();
        IAlumnoDAO alumnoDTO = new AlumnoDAO(conexion);
        TablaAlumnoDTO tabla = alumnoDTO.buscarTabla(new FiltroTablaAlumnoDTO(50, 5, "Samiraaa"));
        tabla.getAlumnos().forEach(p -> {
            System.out.println(p.getId() + ": " + p.getNombre() + " " + p.getApellidoPaterno() + " " + p.getApellidoMaterno());
        });
        
        tablaPaginada.setDatos(tabla.getAlumnos());
        
        
        //egregando la informacion
        tablaPaginada.setBounds(0, 0, 750, 400);
        add(tablaPaginada);
    }
}
