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
        String sql = "SELECT * FROM movimientos ORDER BY fecha DESC"; // Trae los más recientes primero
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                // Instanciamos usando tu Record moderno
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
}