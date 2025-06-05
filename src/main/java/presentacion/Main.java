package presentacion;

import dominio.Estudiante;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Main extends JFrame {

    public static void main(String[] args) {
        new Main().setVisible(true);
    }

    public Main() {
        setTitle("Pruebas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLayout(null);
        
        // infor entrante
        List<Estudiante> list = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            Estudiante e = new Estudiante();
            e.setNombre("name" + i);
            e.setEdad("edad" + i);
            list.add(e);
        }
        // creando columnas
        List<TablaPaginada.Columna<Estudiante>> columnas = new ArrayList<>();
        columnas.add(new TablaPaginada.Columna<>("Nombre", String.class, p -> p.getNombre()));
        columnas.add(new TablaPaginada.Columna<>("Edad", Integer.class, p -> p.getEdad()));
        //creando la tabla
        TablaPaginada<Estudiante> tablaPaginada = new TablaPaginada<>(columnas);
        //egregando la informacion
        tablaPaginada.setDatos(list);
        tablaPaginada.setBounds(0, 0, 750, 400);
        add(tablaPaginada);
    }
}
