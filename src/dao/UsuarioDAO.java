package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.IOperacionesCRUD;
import modelo.Usuario;
import utilidades.ConexionDB;

public class UsuarioDAO implements IOperacionesCRUD<Usuario> {

    // ==========================================
    // MÉTODO ESPECÍFICO: AUTENTICACIÓN DE USUARIO (LOGIN)
    // ==========================================
    public Usuario login(String username, String password) {
        Usuario usuario = null;
        
        // SEGURIDAD: Implementación de consultas parametrizadas (?) para prevenir vulnerabilidades de Inyección SQL
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
        
        // Uso de try-with-resources para la gestión eficiente de conexiones a la base de datos
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Mapeo relacional-objeto (ORM manual): Traslado del ResultSet hacia la entidad Usuario
                    usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("rol")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en el Login: " + e.getMessage());
        }
        return usuario;
    }

    // ==========================================
    // IMPLEMENTACIÓN DE LOS CONTRATOS DE LA INTERFAZ GENÉRICA IOperacionesCRUD
    // ==========================================

    @Override
    public boolean crear(Usuario objeto) {
        String sql = "INSERT INTO usuarios (dni, nombre, apellido, username, password, rol) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, objeto.getDni());
            ps.setString(2, objeto.getNombre());
            ps.setString(3, objeto.getApellido());
            ps.setString(4, objeto.getUsername());
            ps.setString(5, objeto.getPassword());
            ps.setString(6, objeto.getRol());
            
            return ps.executeUpdate() > 0; 
        } catch (SQLException e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Usuario> leerTodos() {
        List<Usuario> listaUsuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                Usuario u = new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("rol")
                );
                listaUsuarios.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer usuarios: " + e.getMessage());
        }
        return listaUsuarios;
    }

    @Override
    public boolean actualizar(Usuario objeto) {
        String sql = "UPDATE usuarios SET dni=?, nombre=?, apellido=?, username=?, password=?, rol=? WHERE id_usuario=?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, objeto.getDni());
            ps.setString(2, objeto.getNombre());
            ps.setString(3, objeto.getApellido());
            ps.setString(4, objeto.getUsername());
            ps.setString(5, objeto.getPassword());
            ps.setString(6, objeto.getRol());
            ps.setInt(7, objeto.getIdUsuario());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario=?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}