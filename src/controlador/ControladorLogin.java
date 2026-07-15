package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import dao.UsuarioDAO;
import modelo.Usuario;
import vista.FrmLogin;
import vista.FrmPrincipal;

// Implementar ActionListener permite que esta clase "escuche" los clics de los botones
public class ControladorLogin implements ActionListener {
    
    private FrmLogin vista;
    private UsuarioDAO dao;

    // El constructor recibe la ventana y el traductor de la base de datos
    public ControladorLogin(FrmLogin vista, UsuarioDAO dao) {
        this.vista = vista;
        this.dao = dao;
        
        // Le decimos al botón de la vista que ESTA clase controlará sus clics
        this.vista.btnIngresar.addActionListener(this);
    }

    // Método para arrancar y configurar la ventana
    public void iniciar() {
        vista.setTitle("Sistema de Almacén - Login");
        vista.setLocationRelativeTo(null); // Centra la ventana en la pantalla
        vista.setResizable(false);
        vista.setVisible(true);
    }

    // Este método se dispara automáticamente cuando alguien hace clic en un botón
    @Override
    public void actionPerformed(ActionEvent e) {
        
        // Verificamos si el clic vino exactamente del botón Ingresar
        if (e.getSource() == vista.btnIngresar) {
            
            // Extraemos el texto de las cajas públicas de la vista
            String usuario = vista.txtUsuario.getText();
            String password = new String(vista.txtPassword.getPassword());

            // Validación básica para evitar campos vacíos
            if (usuario.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor, llene todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Enviamos los datos al DAO para consultar MySQL
            Usuario usuarioLogueado = dao.login(usuario, password);

            // Si el DAO devuelve un objeto, el usuario existe y la clave es correcta
            if (usuarioLogueado != null) {
                // Guardamos al usuario en la sesión global <---
                utilidades.Sesion.usuarioLogueado = usuarioLogueado;
                
                JOptionPane.showMessageDialog(vista, 
                        "¡Bienvenido " + usuarioLogueado.getNombreCompleto() + "!\nRol: " + usuarioLogueado.getRol(), 
                        "Acceso Concedido", 
                        JOptionPane.INFORMATION_MESSAGE);
                
                // Aquí cerraremos el login y abriremos FrmPrincipal más adelante
                                
                // 1. Cerramos la ventana de Login
                vista.dispose(); 
                
                // 2. Instanciamos el menú principal y su controlador
                FrmPrincipal vistaPrincipal = new FrmPrincipal();
                ControladorPrincipal ctrlPrincipal = new ControladorPrincipal(vistaPrincipal);
                
                // 3. Arrancamos el MDI
                ctrlPrincipal.iniciar();
                
            } else {
                 

            }
        }
    }
}