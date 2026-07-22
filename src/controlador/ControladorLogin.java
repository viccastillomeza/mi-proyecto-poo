package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import dao.UsuarioDAO;
import modelo.Usuario;
import vista.FrmLogin;
import vista.FrmPrincipal;

// Implementación de la interfaz genérica ActionListener para interceptar eventos de la vista
public class ControladorLogin implements ActionListener {
    
    private FrmLogin vista;
    private UsuarioDAO dao;

    // Inyección de dependencias: El controlador requiere la Vista y el Objeto de Acceso a Datos (DAO)
    public ControladorLogin(FrmLogin vista, UsuarioDAO dao) {
        this.vista = vista;
        this.dao = dao;
        
        // Asignación del listener al botón de autenticación
        this.vista.btnIngresar.addActionListener(this);
    }

    // Inicialización y configuración de las propiedades del JFrame
    public void iniciar() {
        vista.setTitle("Sistema de Almacén - Login");
        vista.setLocationRelativeTo(null); 
        vista.setResizable(false);
        vista.setVisible(true);
    }

    // Sobreescritura del método accionado por eventos de interfaz
    @Override
    public void actionPerformed(ActionEvent e) {
        
        // Verificación del origen del evento (Botón Ingresar)
        if (e.getSource() == vista.btnIngresar) {
            
            // Extracción de credenciales desde los componentes públicos de la vista
            String usuario = vista.txtUsuario.getText();
            String password = new String(vista.txtPassword.getPassword());

            // Validación de integridad a nivel local (Frontend)
            if (usuario.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor, llene todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Delegación de la validación hacia la capa DAO conectada a MySQL
            Usuario usuarioLogueado = dao.login(usuario, password);

            // Verificación del objeto retornado (Éxito de la consulta)
            if (usuarioLogueado != null) {
                
                // Mantenimiento de Estado Global: Almacenamos la identidad del usuario en la sesión del sistema
                utilidades.Sesion.usuarioLogueado = usuarioLogueado;
                
                JOptionPane.showMessageDialog(vista, 
                        "¡Bienvenido " + usuarioLogueado.getNombreCompleto() + "!\nRol: " + usuarioLogueado.getRol(), 
                        "Acceso Concedido", 
                        JOptionPane.INFORMATION_MESSAGE);
                
                // Transición de flujo de trabajo:
                // 1. Liberación de recursos de la ventana actual de Login
                vista.dispose(); 
                
                // 2. Instanciación de los componentes arquitectónicos de la ventana MDI Principal
                FrmPrincipal vistaPrincipal = new FrmPrincipal();
                ControladorPrincipal ctrlPrincipal = new ControladorPrincipal(vistaPrincipal);
                
                // 3. Ejecución del menú contextual
                ctrlPrincipal.iniciar();
                
            } else {
                // Bloqueo de acceso por credenciales inválidas
                JOptionPane.showMessageDialog(vista, "Usuario o clave incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}