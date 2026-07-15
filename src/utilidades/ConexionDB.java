package utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    
    // Cambia el puerto 3306 si tu MySQL usa uno distinto
    private static final String URL = "jdbc:mysql://localhost:3306/constructora_almacen";
    private static final String USER = "root"; // Tu usuario de MySQL (usualmente root)
    private static final String PASSWORD = "root123456"; // Tu contraseña de MySQL (déjalo vacío si no tienes)

    // Método estático para obtener la conexión
    public static Connection conectar() {
        Connection conexion = null;
        try {
            // Estableciendo la conexión
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("¡Conexión exitosa a la base de datos constructora_almacen!");
        } catch (SQLException e) {
            System.err.println("Error de conexión a la base de datos: " + e.getMessage());
        }
        return conexion;
    }
}