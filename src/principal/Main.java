package principal;

import controlador.ControladorLogin;
import dao.UsuarioDAO;
import vista.FrmLogin;

// Clase principal que actúa como punto de entrada (Entry Point) del sistema.
public class Main {
    public static void main(String[] args) {
        
        // =========================================================
        // APLICACIÓN DEL PATRÓN ARQUITECTÓNICO MVC (Modelo-Vista-Controlador)
        // Inicialización aislada y acoplamiento de las capas del sistema
        // =========================================================
        
        // 1. Instanciación de la capa de persistencia y reglas de negocio (Modelo)
        UsuarioDAO dao = new UsuarioDAO();
        
        // 2. Instanciación de la interfaz gráfica de usuario (Vista)
        FrmLogin vista = new FrmLogin();
        
        // 3. Inyección de dependencias: Ensamblamos el Controlador vinculando la Vista y el Modelo
        ControladorLogin controlador = new ControladorLogin(vista, dao);
        
        // 4. Transferencia del hilo de ejecución al controlador para iniciar el ciclo de vida del software
        controlador.iniciar();
    }
}