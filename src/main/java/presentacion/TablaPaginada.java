package presentacion;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.function.*;
import java.util.List;

/**
 * Clase Generica que permite modelar una tabla CRUD paginada
 *
 * @author angel
 * @param <T>
 */
public class TablaPaginada<T> extends JPanel {

    private PTabla tabla;
    private JPanel panelPaginacion;
    private int paginaActual = 0;
    private int elementosPorPagina = 10;
    private List<T> todosLosDatos;

    /**
     * Metodo constructor de la clase
     *
     * @param columnas
     */
    public TablaPaginada(List<Columna<T>> columnas) {
        setLayout(new BorderLayout());
        tabla = new PTabla(columnas); // Crear la tabla interna
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        // Panel de paginación
        panelPaginacion = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        panelPaginacion.setBackground(new Color(18, 18, 18)); //	
        add(panelPaginacion, BorderLayout.SOUTH);
    }

    /**
     * Metodoq ue permite settear datos a nuestra tabla
     *
     * @param datos
     */
    public void setDatos(List<T> datos) {
        this.todosLosDatos = new ArrayList<>(datos);
        actualizarPaginacion();
        mostrarPagina(0);
    }

    /**
     * metodo que permite cambiar de pagina
     *
     * @param numeroPagina
     */
    private void mostrarPagina(int numeroPagina) {
        paginaActual = numeroPagina;
        int inicio = paginaActual * elementosPorPagina;
        int fin = Math.min(inicio + elementosPorPagina, todosLosDatos.size());

        List<T> datosPagina = todosLosDatos.subList(inicio, fin);
        tabla.setDatos(datosPagina);
        actualizarPaginacion();
    }

    /**
     * barra inferior de paginacion
     */
    private void actualizarPaginacion() {
        panelPaginacion.removeAll();
        int totalPaginas = (int) Math.ceil((double) todosLosDatos.size() / elementosPorPagina);

        Color btnColor = new Color(187, 134, 252);
        // Botón Anterior
        JButton btnAnterior = new JButton("Anterior");
        btnAnterior.setForeground(Color.BLACK);
        btnAnterior.setBackground(btnColor);

        btnAnterior.setEnabled(paginaActual > 0);
        btnAnterior.addActionListener(e -> mostrarPagina(paginaActual - 1));
        panelPaginacion.add(btnAnterior);

        // Indicador de página
        JLabel lblPagina = new JLabel(String.format("Página %d de %d", paginaActual + 1, totalPaginas));
        lblPagina.setForeground(Color.WHITE);
        panelPaginacion.add(lblPagina);

        // Botón Siguiente
        JButton btnSiguiente = new JButton("Siguiente");
        btnSiguiente.setForeground(Color.BLACK);
        btnSiguiente.setBackground(btnColor);

        btnSiguiente.setEnabled(paginaActual < totalPaginas - 1);
        btnSiguiente.addActionListener(e -> mostrarPagina(paginaActual + 1));
        panelPaginacion.add(btnSiguiente);

        panelPaginacion.revalidate();
        panelPaginacion.repaint();
    }

    /**
     * clase interna que modela la tabla
     */
    private class PTabla extends JTable {

        private DefaultTableModel modelo;
        private List<Columna<T>> columnas;
        private List<T> datosActuales;
        private Consumer<T> onEliminar = this::evtEliminar;
        private Consumer<T> onActualizar = this::evtActualizar;

        public PTabla(List<Columna<T>> columnas) {
            // Configurar nombres de columnas (+2 para botones)
            String[] nombresColumnas = new String[columnas.size() + 2];
            for (int i = 0; i < columnas.size(); i++) {
                nombresColumnas[i] = columnas.get(i).getTitulo();
            }
            nombresColumnas[columnas.size()] = "Eliminar";
            nombresColumnas[columnas.size() + 1] = "Actualizar";
            
            // Configurar modelo base de la tabla
            modelo = new DefaultTableModel(nombresColumnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column >= getColumnCount() - 2;
                }

                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnIndex >= getColumnCount() - 2 ? JButton.class : super.getColumnClass(columnIndex);
                }
            };
            this.columnas = columnas;
            this.setModel(modelo);

            // Renderizador de celdas
            this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    // Aplicar estilos base para todas las celdas
                    setFont(new Font("Oswald", Font.BOLD, 18));
                    setForeground(Color.BLACK);

                    // Fondo alternado
                    if (!isSelected) {
                        Color colorA = new Color(243, 243, 243);
                        Color colorB = new Color(230, 230, 230);
                        setBackground(row % 2 == 0 ? colorA : colorB);
                    } else {
                        setBackground(new Color(204, 229, 255)); // Color para selección
                        setForeground(Color.BLACK);
                    }

                    // Alineación
                    if (value instanceof Number) {
                        setHorizontalAlignment(JLabel.RIGHT);
                    } else {
                        setHorizontalAlignment(JLabel.LEFT);
                    }

                    return this;
                }
            });
            // Renderizador de botones
            TableCellRenderer buttonRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    if (value instanceof Component) {
                        return (Component) value;
                    }
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
            };
            // setteo de renderizados
            this.getColumnModel().getColumn(columnas.size()).setCellRenderer(buttonRenderer);
            this.getColumnModel().getColumn(columnas.size()).setCellEditor(new ButtonEditor(onEliminar));
            this.getColumnModel().getColumn(columnas.size() + 1).setCellRenderer(buttonRenderer);
            this.getColumnModel().getColumn(columnas.size() + 1).setCellEditor(new ButtonEditor(onActualizar));

            
            // Configuración adicional que ya tienes
            this.setRowHeight(25);
            this.setShowGrid(false);
            this.setIntercellSpacing(new Dimension(0, 1));
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // IMPORTANTE para control manual

            // Obtener el ancho preferido de la tabla (o establecer uno fijo si es necesario)
            int totalWidth = 700; // Puedes ajustar este valor según tus necesidades

            // Configurar porcentajes exactos
            TableColumnModel columnModel = this.getColumnModel();

            // Columna Nombre (80%)
            columnModel.getColumn(0).setPreferredWidth((int) (totalWidth * 0.70));

            // Columna Edad (10%)
            columnModel.getColumn(1).setPreferredWidth((int) (totalWidth * 0.10));

            // Columnas de botones (5% cada una)
            int buttonWidth = (int) (totalWidth * 0.10);
            columnModel.getColumn(columnas.size()).setPreferredWidth(buttonWidth);     // Eliminar
            columnModel.getColumn(columnas.size() + 1).setPreferredWidth(buttonWidth); // Actualizar

            // Asegurar que el ancho total sea exacto
            int calculatedTotal = (int) (totalWidth * 0.80) + (int) (totalWidth * 0.10)
                    + buttonWidth * 2;
            if (calculatedTotal != totalWidth) {
                // Ajustar la columna principal para compensar
                columnModel.getColumn(0).setPreferredWidth(
                        columnModel.getColumn(0).getPreferredWidth()
                        + (totalWidth - calculatedTotal)
                );
            }
        }

        public void setDatos(List<T> datos) {
            this.datosActuales = new ArrayList<>(datos);
            modelo.setRowCount(0);

            for (T dato : datos) {
                Object[] fila = new Object[columnas.size() + 2];
                for (int i = 0; i < columnas.size(); i++) {
                    fila[i] = columnas.get(i).getValor().apply(dato);
                }

                // Crear botones con acciones
                fila[columnas.size()] = crearBoton("X", new Color(181, 0, 0), e -> { //255, 2, 102
                    int row = getSelectedRow();
                    if (row >= 0 && row < datosActuales.size()) {
                        onEliminar.accept(datosActuales.get(row));
                    }
                });

                fila[columnas.size() + 1] = crearBoton("M", new Color(255, 215, 0), e -> {
                    int row = getSelectedRow();
                    if (row >= 0 && row < datosActuales.size()) {
                        onActualizar.accept(datosActuales.get(row));
                    }
                });

                modelo.addRow(fila);
            }
        }

        /**
         * logica para eliminar campo
         *
         * @param dato
         */
        private void evtEliminar(T dato) {
            int respuesta = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro que desea eliminar este elemento?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {
                datosActuales.remove(dato);
                setDatos(datosActuales);
            }
        }

        /**
         * logica para actualizar campo
         *
         * @param dato
         */
        private void evtActualizar(T dato) {
            JOptionPane.showMessageDialog(null, "Actualizar elemento: " + dato.toString(), "Actualización", JOptionPane.INFORMATION_MESSAGE);
        }

        private JButton crearBoton(String texto, Color color, ActionListener action) {
            JButton btn = new JButton(texto);
            btn.setBackground(color);
            btn.setForeground(new Color(255, 103, 103));
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.addActionListener(action);
            return btn;
        }

        private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

            private JButton button;
            private Consumer<T> action;

            public ButtonEditor(Consumer<T> action) {
                this.action = action;
                button = new JButton();
                button.setOpaque(true);
                button.addActionListener(e -> {
                    fireEditingStopped();
                    int row = getSelectedRow();
                    if (row >= 0 && row < datosActuales.size()) {
                        this.action.accept(datosActuales.get(row));
                    }
                });
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                    boolean isSelected, int row, int column) {
                if (value instanceof JButton) {
                    button.setText(((JButton) value).getText());
                    button.setBackground(((JButton) value).getBackground());
                }
                return button;
            }

            @Override
            public Object getCellEditorValue() {
                return button;
            }
        }
    }

    /**
     * clase interna que modela las columnas
     *
     * @param <T>
     */
    public static class Columna<T> {

        private String titulo;
        private Class<?> tipo;
        private Function<T, Object> valor;

        public Columna(String titulo, Class<?> tipo, Function<T, Object> valor) {
            this.titulo = titulo;
            this.tipo = tipo;
            this.valor = valor;
        }

        public String getTitulo() {
            return titulo;
        }

        public Class<?> getTipo() {
            return tipo;
        }

        public Function<T, Object> getValor() {
            return valor;
        }
    }
}
