package utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Clase utilitaria para la gestión centralizada de la conexión a la base de datos.
// Aplica el Principio de Responsabilidad Única (SRP) aislando la infraestructura de acceso a datos del resto del sistema.
public class ConexionDB {
    
    // Encapsulamiento de las credenciales de conexión mediante constantes estáticas inmutables
    private static final String URL = "jdbc:mysql://localhost:3306/constructora_almacen";
    private static final String USER = "root"; 
    private static final String PASSWORD = "root123456"; 

    // Método factoría estático que provee instancias de conexión (Connection) a las clases DAO
    public static Connection conectar() {
        Connection conexion = null;
        try {
            // Instanciación de la conexión segura mediante el controlador JDBC de MySQL
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("¡Conexión exitosa a la base de datos constructora_almacen!");
        } catch (SQLException e) {
            // Trazabilidad de errores en tiempo de ejecución para facilitar la depuración
            System.err.println("Error de conexión a la base de datos: " + e.getMessage());
        }
        return conexion;
    }
}