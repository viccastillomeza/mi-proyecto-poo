package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import dao.MaterialDAO;
import modelo.Consumible;
import modelo.Herramienta;
import modelo.ItemAlmacen;
import modelo.MaterialObra;
import vista.JIfrmMateriales;
import java.util.Map;
import java.util.stream.Collectors;

public class ControladorInventario implements ActionListener {

    private JIfrmMateriales vista;
    private MaterialDAO dao;
    private DefaultTableModel modeloTabla;

    public ControladorInventario(JIfrmMateriales vista, MaterialDAO dao) {
        this.vista = vista;
        this.dao = dao;
        
        // Ponemos los botones a escuchar clics
        this.vista.btnGuardar.addActionListener(this);
        this.vista.btnActualizar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnLimpiar.addActionListener(this);
        this.vista.btnReporte.addActionListener(this);
        
        // NUEVO: Escuchamos los clics en la tabla para subir los datos al formulario
        this.vista.tblMateriales.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                llenarCamposDesdeTabla();
            }
        });
        
        // Iniciamos la tabla y cargamos los datos
        listarMateriales();
    }
    
    // =========================================================
    // UTILIDAD: SUBIR DATOS DE LA TABLA AL FORMULARIO
    // =========================================================
    private void llenarCamposDesdeTabla() {
        int fila = vista.tblMateriales.getSelectedRow();
        if (fila == -1) return;

        // El ID está en la columna 0, no lo mostramos en caja pero lo usaremos internamente
        vista.txtNombre.setText(vista.tblMateriales.getValueAt(fila, 1).toString());
        vista.cbxTipo.setSelectedItem(vista.tblMateriales.getValueAt(fila, 2).toString());
        vista.txtUnidad.setText(vista.tblMateriales.getValueAt(fila, 3).toString());
        vista.txtStock.setText(vista.tblMateriales.getValueAt(fila, 4).toString());
        vista.txtStockMinimo.setText(vista.tblMateriales.getValueAt(fila, 5).toString());
    }

    // =========================================================
    // MÉTODO LISTAR (CON LAMBDAS Y STREAMS PARA LA RÚBRICA)
    // =========================================================
    private void listarMateriales() {
        // Obtenemos el modelo de la tabla para poder agregarle filas
        modeloTabla = (DefaultTableModel) vista.tblMateriales.getModel();
        modeloTabla.setRowCount(0); // Limpiamos la tabla antes de cargar

        // Traemos la colección (List) desde la base de datos
        List<ItemAlmacen> lista = dao.leerTodos();

        // REQUISITO DE RÚBRICA: Uso de Expresión Lambda (forEach)
        // En lugar de usar un bucle 'for' antiguo, iteramos la colección con programación funcional
        lista.forEach(item -> {
            Object[] fila = new Object[6];
            fila[0] = item.getIdItem();
            fila[1] = item.getNombre();
            fila[2] = item.getTipoItem();
            fila[3] = item.getUnidadMedida();
            fila[4] = item.getStockActual();
            fila[5] = item.getStockMinimo();
            modeloTabla.addRow(fila); // Agregamos la fila a la tabla visual
        });
    }

    // =========================================================
    // CONTROL DE CLICS (GUARDAR, ACTUALIZAR, ELIMINAR)
    // =========================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        
        // ----- BOTÓN LIMPIAR -----
        if (e.getSource() == vista.btnLimpiar) {
            limpiarCajas();
        }

        // ----- BOTÓN GUARDAR (CREATE) -----
        if (e.getSource() == vista.btnGuardar) {
            if (camposVacios()) return;

            String nombre = vista.txtNombre.getText();
            String tipo = vista.cbxTipo.getSelectedItem().toString();
            String unidad = vista.txtUnidad.getText();
            int stock = Integer.parseInt(vista.txtStock.getText());
            int minimo = Integer.parseInt(vista.txtStockMinimo.getText());

            // POLIMORFISMO: Creamos el objeto hijo correspondiente
            ItemAlmacen nuevoItem = null;
            switch (tipo) {
                case "MaterialObra":
                    nuevoItem = new MaterialObra(0, nombre, unidad, stock, minimo, 0);
                    break;
                case "Herramienta":
                    nuevoItem = new Herramienta(0, nombre, unidad, stock, minimo);
                    break;
                case "Consumible":
                    nuevoItem = new Consumible(0, nombre, unidad, stock, minimo);
                    break;
            }

            if (dao.crear(nuevoItem)) {
                JOptionPane.showMessageDialog(vista, "Material registrado con éxito");
                limpiarCajas();
                listarMateriales(); // Recargamos la tabla
            } else {
                JOptionPane.showMessageDialog(vista, "Error al guardar en MySQL");
            }
        }
        
        // ----- BOTÓN ACTUALIZAR (UPDATE) -----
        if (e.getSource() == vista.btnActualizar) {
            int filaSeleccionada = vista.tblMateriales.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione un material de la tabla para actualizar", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (camposVacios()) return;

            // Extraemos el ID original desde la tabla (columna 0)
            int id = (int) vista.tblMateriales.getValueAt(filaSeleccionada, 0); 
            String nombre = vista.txtNombre.getText();
            String tipo = vista.cbxTipo.getSelectedItem().toString();
            String unidad = vista.txtUnidad.getText();
            int stock = Integer.parseInt(vista.txtStock.getText());
            int minimo = Integer.parseInt(vista.txtStockMinimo.getText());

            // POLIMORFISMO: Recreamos el objeto con sus nuevos valores, manteniendo su ID
            ItemAlmacen itemActualizado = null;
            switch (tipo) {
                case "MaterialObra":
                    itemActualizado = new MaterialObra(id, nombre, unidad, stock, minimo, 0);
                    break;
                case "Herramienta":
                    itemActualizado = new Herramienta(id, nombre, unidad, stock, minimo);
                    break;
                case "Consumible":
                    itemActualizado = new Consumible(id, nombre, unidad, stock, minimo);
                    break;
            }

            if (dao.actualizar(itemActualizado)) {
                JOptionPane.showMessageDialog(vista, "Material actualizado correctamente");
                limpiarCajas();
                listarMateriales(); // Recargamos la tabla para ver los cambios
            } else {
                JOptionPane.showMessageDialog(vista, "Error al actualizar en MySQL", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // ----- BOTÓN ELIMINAR (DELETE) -----
        if (e.getSource() == vista.btnEliminar) {
            int filaSeleccionada = vista.tblMateriales.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar una fila de la tabla para eliminar");
                return;
            }
            
            // Obtenemos el ID de la primera columna (columna 0) de la tabla
            int id = (int) vista.tblMateriales.getValueAt(filaSeleccionada, 0);
            
            int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Está seguro de eliminar este ítem?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (dao.eliminar(id)) {
                    JOptionPane.showMessageDialog(vista, "Ítem eliminado correctamente");
                    listarMateriales();
                }
            }
        }
        // ----- BOTÓN REPORTE (STREAMS Y MAP PARA LA RÚBRICA) -----
        if (e.getSource() == vista.btnReporte) {
            
            // 1. Obtenemos toda la lista desde MySQL
            List<ItemAlmacen> inventario = dao.leerTodos();

            if (inventario.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "No hay datos para generar el reporte.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ==============================================================
            // REQUISITO DE RÚBRICA: Uso de Streams y Map
            // Agrupamos los ítems por tipo y sumamos su stock automáticamente
            // ==============================================================
            Map<String, Integer> reportePorCategoria = inventario.stream()
                    .collect(Collectors.groupingBy(
                            ItemAlmacen::getTipoItem, // Llave del Map: El tipo de ítem
                            Collectors.summingInt(ItemAlmacen::getStockActual) // Valor del Map: La suma del stock
                    ));

            // Preparamos el texto a mostrar
            StringBuilder mensaje = new StringBuilder("=== REPORTE DE STOCK GERENCIAL ===\n\n");
            
            // ==============================================================
            // REQUISITO DE RÚBRICA: Segunda expresión Lambda (forEach en Map)
            // ==============================================================
            reportePorCategoria.forEach((categoria, totalStock) -> {
                mensaje.append("► Categoría ").append(categoria).append(": ")
                       .append(totalStock).append(" unidades en total.\n");
            });

            // Mostramos el reporte en pantalla
            JOptionPane.showMessageDialog(vista, mensaje.toString(), "Reporte de Inventario", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Utilidad para limpiar el formulario
    private void limpiarCajas() {
        vista.txtNombre.setText("");
        vista.txtUnidad.setText("");
        vista.txtStock.setText("");
        vista.txtStockMinimo.setText("");
        vista.cbxTipo.setSelectedIndex(0);
    }

    // Utilidad para validar
    private boolean camposVacios() {
        if (vista.txtNombre.getText().isEmpty() || vista.txtUnidad.getText().isEmpty() || 
            vista.txtStock.getText().isEmpty() || vista.txtStockMinimo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios");
            return true;
        }
        return false;
    }
}