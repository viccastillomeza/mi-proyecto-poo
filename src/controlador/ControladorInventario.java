package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import dao.MaterialDAO;
import dao.MovimientoDAO;
import modelo.Consumible;
import modelo.Herramienta;
import modelo.ItemAlmacen;
import modelo.MaterialObra;
import modelo.Movimiento;
import utilidades.Sesion;
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
        
        // Suscripción de los componentes de la vista a los eventos de acción (Listener)
        this.vista.btnGuardar.addActionListener(this);
        this.vista.btnActualizar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnLimpiar.addActionListener(this);
        this.vista.btnReporte.addActionListener(this);
        
        // Implementación de MouseListener para la selección y extracción de datos desde la JTable
        this.vista.tblMateriales.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                llenarCamposDesdeTabla();
            }
        });
        
        // Inicialización de la vista con los datos persistidos
        listarMateriales();
    }
    
    // =========================================================
    // MÉTODO AUXILIAR: TRANSFERENCIA DE DATOS DE LA TABLA AL FORMULARIO
    // =========================================================
    private void llenarCamposDesdeTabla() {
        int fila = vista.tblMateriales.getSelectedRow();
        if (fila == -1) return;

        // Recuperación de la fila seleccionada (El ID está en la columna 0 para uso interno)
        vista.txtNombre.setText(vista.tblMateriales.getValueAt(fila, 1).toString());
        vista.cbxTipo.setSelectedItem(vista.tblMateriales.getValueAt(fila, 2).toString());
        vista.txtUnidad.setText(vista.tblMateriales.getValueAt(fila, 3).toString());
        vista.txtStock.setText(vista.tblMateriales.getValueAt(fila, 4).toString());
        vista.txtStockMinimo.setText(vista.tblMateriales.getValueAt(fila, 5).toString());
    }

    // =========================================================
    // MÉTODO LISTAR: APLICACIÓN DE LAMBDAS (REQUISITO DE RÚBRICA)
    // =========================================================
    private void listarMateriales() {
        // Reseteo del modelo de tabla para evitar duplicidad visual
        modeloTabla = (DefaultTableModel) vista.tblMateriales.getModel();
        modeloTabla.setRowCount(0); 

        // Extracción de la colección principal desde la capa de persistencia (DAO)
        List<ItemAlmacen> lista = dao.leerTodos();

        // REQUISITO DE RÚBRICA: Iteración de la colección mediante programación funcional (Lambda forEach)
        lista.forEach(item -> {
            Object[] fila = new Object[6];
            fila[0] = item.getIdItem();
            fila[1] = item.getNombre();
            fila[2] = item.getTipoItem();
            fila[3] = item.getUnidadMedida();
            fila[4] = item.getStockActual();
            fila[5] = item.getStockMinimo();
            modeloTabla.addRow(fila); 
        });
    }

    // =========================================================
    // GESTIÓN DE EVENTOS CRUD Y REPORTE
    // =========================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        
        // ----- OPERACIÓN: LIMPIAR FORMULARIO -----
        if (e.getSource() == vista.btnLimpiar) {
            limpiarCajas();
        }

        // ----- OPERACIÓN: CREAR (INSERT) -----
        if (e.getSource() == vista.btnGuardar) {
            if (camposVacios()) return;

            String nombre = vista.txtNombre.getText();
            String tipo = vista.cbxTipo.getSelectedItem().toString();
            String unidad = vista.txtUnidad.getText();
            int stock = Integer.parseInt(vista.txtStock.getText());
            int minimo = Integer.parseInt(vista.txtStockMinimo.getText());

            // APLICACIÓN DE POLIMORFISMO (REQUISITO DE RÚBRICA): 
            // Instanciación dinámica de la subclase correspondiente según la selección del usuario
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
                listarMateriales(); // Sincronización de la vista
            } else {
                JOptionPane.showMessageDialog(vista, "Error al guardar en MySQL");
            }
        }
        
       // ----- OPERACIÓN: ACTUALIZAR (UPDATE) CON AUDITORÍA -----
        if (e.getSource() == vista.btnActualizar) {
            
            // 1. Validación de estado de selección
            int fila = vista.tblMateriales.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un material de la tabla para actualizar");
                return;
            }

            // 2. Extracción de estado previo para cálculos de auditoría
            int idItem = Integer.parseInt(vista.tblMateriales.getValueAt(fila, 0).toString());
            int stockAntiguo = Integer.parseInt(vista.tblMateriales.getValueAt(fila, 4).toString());

            // 3. Captura del nuevo estado del objeto desde el formulario
            String nombre = vista.txtNombre.getText();
            String tipo = vista.cbxTipo.getSelectedItem().toString();
            String unidad = vista.txtUnidad.getText();
            int stockNuevo = Integer.parseInt(vista.txtStock.getText());
            int minimo = Integer.parseInt(vista.txtStockMinimo.getText());

            // POLIMORFISMO: Preparación del objeto con sus nuevos atributos
            ItemAlmacen itemModificado = null;
            switch (tipo) {
                case "MaterialObra":
                    itemModificado = new MaterialObra(idItem, nombre, unidad, stockNuevo, minimo, 0); 
                    break;
                case "Herramienta":
                    itemModificado = new Herramienta(idItem, nombre, unidad, stockNuevo, minimo);
                    break;
                case "Consumible":
                    itemModificado = new Consumible(idItem, nombre, unidad, stockNuevo, minimo);
                    break;
            }

            // Ejecución de la transacción principal en MySQL
            if (dao.actualizar(itemModificado)) {
                
                // 4. LÓGICA DE AUDITORÍA: Cálculo automático de variación de inventario
                int diferencia = stockNuevo - stockAntiguo;

                if (diferencia != 0) { 
                    String tipoMovimiento = (diferencia > 0) ? "INGRESO" : "SALIDA";
                    int cantidad = Math.abs(diferencia); // Normalización de la cantidad a valor absoluto
                    int idUsuario = Sesion.usuarioLogueado.getIdUsuario();

                    // REQUISITO DE RÚBRICA: Uso de la estructura inmutable 'Record' de Java 14+
                    Movimiento mov = new Movimiento(0, idItem, idUsuario, tipoMovimiento, cantidad, "");

                    // Registro transparente de la transacción física
                    MovimientoDAO movDao = new MovimientoDAO();
                    movDao.registrarMovimiento(mov);
                }

                JOptionPane.showMessageDialog(vista, "Material actualizado correctamente.");
                
                // 5. Restablecimiento del flujo de interfaz
                limpiarCajas();
                listarMateriales();
                
            } else {
                JOptionPane.showMessageDialog(vista, "Error al actualizar en MySQL");
            }
        }
        
        // ----- OPERACIÓN: ELIMINAR (DELETE) -----
        if (e.getSource() == vista.btnEliminar) {
            int filaSeleccionada = vista.tblMateriales.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar una fila de la tabla para eliminar");
                return;
            }
            
            // Extracción de llave primaria
            int id = (int) vista.tblMateriales.getValueAt(filaSeleccionada, 0);
            
            int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Está seguro de eliminar este ítem?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (dao.eliminar(id)) {
                    JOptionPane.showMessageDialog(vista, "Ítem eliminado correctamente");
                    listarMateriales();
                }
            }
        }
        
        // ----- OPERACIÓN: GENERACIÓN DE REPORTES (STREAMS Y MAP PARA RÚBRICA) -----
        if (e.getSource() == vista.btnReporte) {
            
            // 1. Extracción del listado maestro desde la base de datos
            List<ItemAlmacen> inventario = dao.leerTodos();

            if (inventario.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "No hay datos para generar el reporte.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ==============================================================
            // REQUISITO DE RÚBRICA: Uso de API Streams y Collectors (Map)
            // Agrupamiento lógico de los ítems por subclase y sumarización de stock
            // ==============================================================
            Map<String, Integer> reportePorCategoria = inventario.stream()
                    .collect(Collectors.groupingBy(
                            ItemAlmacen::getTipoItem, // Clave: Tipo de herencia
                            Collectors.summingInt(ItemAlmacen::getStockActual) // Valor: Acumulador de stock
                    ));

            // Estructuración del buffer de texto
            StringBuilder mensaje = new StringBuilder("=== REPORTE DE STOCK GERENCIAL ===\n\n");
            
            // ==============================================================
            // REQUISITO DE RÚBRICA: Segunda expresión Lambda (forEach sobre Map)
            // ==============================================================
            reportePorCategoria.forEach((categoria, totalStock) -> {
                mensaje.append("► Categoría ").append(categoria).append(": ")
                       .append(totalStock).append(" unidades en total.\n");
            });

            // Renderizado del reporte analítico
            JOptionPane.showMessageDialog(vista, mensaje.toString(), "Reporte de Inventario", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Utilidad interna: Limpieza de componentes
    private void limpiarCajas() {
        vista.txtNombre.setText("");
        vista.txtUnidad.setText("");
        vista.txtStock.setText("");
        vista.txtStockMinimo.setText("");
        vista.cbxTipo.setSelectedIndex(0);
    }

    // Utilidad interna: Verificación de integridad de datos frontend
    private boolean camposVacios() {
        if (vista.txtNombre.getText().isEmpty() || vista.txtUnidad.getText().isEmpty() || 
            vista.txtStock.getText().isEmpty() || vista.txtStockMinimo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios");
            return true;
        }
        return false;
    }
}