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

        List<Movimiento> lista = dao.leerTodos();

        // Usamos otra expresión Lambda para recorrer el historial rápidamente
        lista.forEach(mov -> {
            Object[] fila = new Object[6];
            fila[0] = mov.idMovimiento(); // Usamos los métodos autogenerados del Record
            fila[1] = mov.idItem();
            fila[2] = mov.idUsuario();
            fila[3] = mov.tipoMovimiento();
            fila[4] = mov.cantidad();
            fila[5] = mov.fecha();
            modeloTabla.addRow(fila);
        });
    }
}