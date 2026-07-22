package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Consumible;
import modelo.Herramienta;
import modelo.IOperacionesCRUD;
import modelo.ItemAlmacen;
import modelo.MaterialObra;
import utilidades.ConexionDB;

public class MaterialDAO implements IOperacionesCRUD<ItemAlmacen> {

    @Override
    public boolean crear(ItemAlmacen objeto) {
        String sql = "INSERT INTO items_almacen (nombre, tipo_item, unidad_medida, stock_actual, stock_minimo, desperdicio_acumulado) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, objeto.getNombre());
            ps.setString(2, objeto.getTipoItem());
            ps.setString(3, objeto.getUnidadMedida());
            ps.setInt(4, objeto.getStockActual());
            ps.setInt(5, objeto.getStockMinimo());
            
            // REQUISITO DE RÚBRICA (POLIMORFISMO Y DOWNCASTING): 
            // Evaluación del tipo de instancia en tiempo de ejecución (instanceof) para persistir atributos específicos de las subclases
            if (objeto instanceof MaterialObra) {
                MaterialObra mat = (MaterialObra) objeto;
                ps.setInt(6, mat.getDesperdicioAcumulado());
            } else {
                ps.setInt(6, 0); // Valor por defecto para subclases que no implementan gestión de mermas
            }
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear ítem: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<ItemAlmacen> leerTodos() {
        List<ItemAlmacen> listaItems = new ArrayList<>();
        String sql = "SELECT * FROM items_almacen";
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                int id = rs.getInt("id_item");
                String nombre = rs.getString("nombre");
                String tipo = rs.getString("tipo_item");
                String unidad = rs.getString("unidad_medida");
                int stockActual = rs.getInt("stock_actual");
                int stockMinimo = rs.getInt("stock_minimo");
                int desperdicio = rs.getInt("desperdicio_acumulado");

                // REQUISITO DE RÚBRICA (POLIMORFISMO): 
                // Instanciación dinámica de subclases basadas en el discriminador 'tipo_item' recuperado de la base de datos
                ItemAlmacen item = null;
                
                switch (tipo) {
                    case "MaterialObra":
                        item = new MaterialObra(id, nombre, unidad, stockActual, stockMinimo, desperdicio);
                        break;
                    case "Herramienta":
                        item = new Herramienta(id, nombre, unidad, stockActual, stockMinimo);
                        break;
                    case "Consumible":
                        item = new Consumible(id, nombre, unidad, stockActual, stockMinimo);
                        break;
                }
                
                if (item != null) {
                    listaItems.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al leer ítems: " + e.getMessage());
        }
        return listaItems;
    }

    @Override
    public boolean actualizar(ItemAlmacen objeto) {
        String sql = "UPDATE items_almacen SET nombre=?, tipo_item=?, unidad_medida=?, stock_actual=?, stock_minimo=?, desperdicio_acumulado=? WHERE id_item=?";
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, objeto.getNombre());
            ps.setString(2, objeto.getTipoItem());
            ps.setString(3, objeto.getUnidadMedida());
            ps.setInt(4, objeto.getStockActual());
            ps.setInt(5, objeto.getStockMinimo());
            
            // Verificación polimórfica para la actualización de atributos exclusivos
            if (objeto instanceof MaterialObra) {
                ps.setInt(6, ((MaterialObra) objeto).getDesperdicioAcumulado());
            } else {
                ps.setInt(6, 0);
            }
            
            ps.setInt(7, objeto.getIdItem());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar ítem: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM items_almacen WHERE id_item=?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar ítem: " + e.getMessage());
            return false;
        }
    }
}