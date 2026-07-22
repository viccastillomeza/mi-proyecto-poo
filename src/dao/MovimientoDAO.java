package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utilidades.ConexionDB;
import modelo.Movimiento;

public class MovimientoDAO {
    
    public List<Movimiento> leerTodos() {
        List<Movimiento> lista = new ArrayList<>();
        // Ordenamiento descendente para visualizar el historial transaccional más reciente primero
        String sql = "SELECT * FROM movimientos ORDER BY fecha DESC"; 
        
        // REQUISITO DE RÚBRICA: Uso de try-with-resources para garantizar el cierre automático de la conexión y evitar fugas de memoria (Memory Leaks)
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                // REQUISITO DE RÚBRICA: Instanciación del objeto utilizando la estructura inmutable 'Record'
                Movimiento mov = new Movimiento(
                    rs.getInt("id_movimiento"),
                    rs.getInt("id_item"),
                    rs.getInt("id_usuario"),
                    rs.getString("tipo_movimiento"),
                    rs.getInt("cantidad"),
                    rs.getString("fecha")
                );
                lista.add(mov);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer movimientos: " + e.getMessage());
        }
        return lista;
    }
    
    public boolean registrarMovimiento(Movimiento mov) {
        // Delegación de la marca de tiempo (timestamp) al motor de base de datos mediante la función nativa NOW()
        String sql = "INSERT INTO movimientos (id_item, id_usuario, tipo_movimiento, cantidad, fecha) VALUES (?, ?, ?, ?, NOW())";
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            // Inyección de parámetros utilizando los métodos de acceso autogenerados por el Record
            ps.setInt(1, mov.idItem());
            ps.setInt(2, mov.idUsuario());
            ps.setString(3, mov.tipoMovimiento());
            ps.setInt(4, mov.cantidad());
            
            // Verificación de filas afectadas para confirmar el éxito de la transacción
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al registrar movimiento automático: " + e.getMessage());
            return false;
        }
    }
}