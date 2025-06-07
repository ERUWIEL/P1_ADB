package presentacion;

import DTO.FiltroTablaAlumnoDTO;

import DTO.TablaAlumnoDTO;
import dominio.AlumnoDominio;
import fachada.AlumnoFachada;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import negocio.NegocioException;
import persistencia.*;

public class Main extends JFrame {

    public static void main(String[] args) throws NegocioException {
        AlumnoFachada fachada = new AlumnoFachada();
        try{
            fachada.modificar();
        }catch(NegocioException ex){
            System.out.println(ex.getMessage());
        }
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
