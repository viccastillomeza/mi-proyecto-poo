package principal; // O el paquete donde lo pongas

import controlador.ControladorLogin;
import dao.UsuarioDAO;
import vista.FrmLogin;

public class Main {
    public static void main(String[] args) {
        // 1. Instanciamos el Modelo (DAO)
        UsuarioDAO dao = new UsuarioDAO();
        
        // 2. Instanciamos la Vista
        FrmLogin vista = new FrmLogin();
        
        // 3. Instanciamos el Controlador uniendo la Vista y el DAO
        ControladorLogin controlador = new ControladorLogin(vista, dao);
        
        // 4. ¡Arrancamos el programa!
        controlador.iniciar();
    }
}