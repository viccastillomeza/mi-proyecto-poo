package controlador;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import dao.MovimientoDAO;
import modelo.Movimiento;
import vista.JIfrmMovimientos;

public class ControladorMovimientos {
    
    private JIfrmMovimientos vista;
    private MovimientoDAO dao;
    private DefaultTableModel modeloTabla;

    public ControladorMovimientos(JIfrmMovimientos vista, MovimientoDAO dao) {
        this.vista = vista;
        this.dao = dao;
        
        listarMovimientos();
    }

    private void listarMovimientos() {
        modeloTabla = (DefaultTableModel) vista.tblMovimientos.getModel();
        modeloTabla.setRowCount(0); 

        // Recuperación del historial completo
        List<Movimiento> lista = dao.leerTodos();

        // REQUISITO DE RÚBRICA: Uso de Expresiones Lambda para la iteración eficiente de la estructura
        lista.forEach(mov -> {
            Object[] fila = new Object[6];
            // Acceso a datos mediante los métodos inmutables propios de la estructura Record
            fila[0] = mov.idMovimiento(); 
            fila[1] = mov.idItem();
            fila[2] = mov.idUsuario();
            fila[3] = mov.tipoMovimiento();
            fila[4] = mov.cantidad();
            fila[5] = mov.fecha();
            modeloTabla.addRow(fila);
        });
    }
}