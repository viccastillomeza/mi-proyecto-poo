package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import dao.MaterialDAO;
import dao.MovimientoDAO;
import vista.FrmPrincipal;
import vista.JIfrmMateriales;
import vista.JIfrmMovimientos;

public class ControladorPrincipal implements ActionListener {
    
    private FrmPrincipal vistaPrincipal;

    public ControladorPrincipal(FrmPrincipal vistaPrincipal) {
        this.vistaPrincipal = vistaPrincipal;
        
        // Ponemos a la escucha el submenú de materiales
        this.vistaPrincipal.menuMateriales.addActionListener(this);
        
        // Ponemos a la escucha el submenú de movimientos
        this.vistaPrincipal.menuMovimientos.addActionListener(this);
        
        // Escuchamos el botón Salir <---
        this.vistaPrincipal.menuSalir.addActionListener(this);
    }

    public void iniciar() {
        // En el título, mostramos quién está conectado
        vistaPrincipal.setTitle("Sistema de Almacén - Usuario: " + utilidades.Sesion.usuarioLogueado.getUsername());
        vistaPrincipal.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        
        // ====================================================
        // MAGIA DE SEGURIDAD: CONTROL DE ACCESO POR ROLES
        // ====================================================
        String rolUsuario = utilidades.Sesion.usuarioLogueado.getRol();
        
        // Si es Almacenero, ocultamos el menú de movimientos por completo
        if (rolUsuario.equals("Almacenero")) {
            vistaPrincipal.menuMovimientosBarra.setVisible(false);
        }
        // Si es Administrador o Supervisor, el menú seguirá visible por defecto.
        
        vistaPrincipal.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Si hacen clic en el menú "Gestión de Materiales"
        if (e.getSource() == vistaPrincipal.menuMateriales) {
            
            // 1. Instanciamos la ventana interna y su DAO
            JIfrmMateriales vistaMateriales = new JIfrmMateriales();
            MaterialDAO daoMateriales = new MaterialDAO();

            // 2. CONECTAMOS TODO: Instanciamos tu nuevo ControladorInventario
            ControladorInventario ctrlMat = new ControladorInventario(vistaMateriales, daoMateriales);

            // 3. Agregamos la ventanita al escritorio MDI y la mostramos
            vistaPrincipal.escritorio.add(vistaMateriales);
            vistaMateriales.setVisible(true);
        }
        // Si hacen clic en el menú "Salir"
        if (e.getSource() == vistaPrincipal.menuSalir) {
            // ---> AGREGA ESTO PARA DEPURAR <---
            System.out.println("¡Se hizo clic en Cerrar Sesión!");
            
            // Preguntamos al usuario si está seguro
            int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
                    vistaPrincipal, 
                    "¿Está seguro que desea cerrar sesión y salir del sistema?", 
                    "Confirmar Salida", 
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE
            );
            
            // Si elige "Sí"
            if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
                // 1. Limpiamos la sesión global por seguridad
                utilidades.Sesion.usuarioLogueado = null;
                
                // 2. Cerramos el escritorio principal
                vistaPrincipal.dispose();
                
                // 3. Volvemos a abrir la pantalla de Login (para que otro usuario pueda entrar)
                vista.FrmLogin vistaLogin = new vista.FrmLogin();
                dao.UsuarioDAO daoUsuario = new dao.UsuarioDAO();
                controlador.ControladorLogin ctrlLogin = new controlador.ControladorLogin(vistaLogin, daoUsuario);
                ctrlLogin.iniciar();
                
                // Nota: Si en lugar de volver al login prefieres que el programa se cierre 
                // por completo, puedes borrar los pasos 1, 2 y 3 y simplemente escribir: 
                // System.exit(0);
            }
        }
        
        // Si hacen clic en el menú "Historial de Movimientos"
        if (e.getSource() == vistaPrincipal.menuMovimientos) {

            JIfrmMovimientos vistaMov = new JIfrmMovimientos();
            MovimientoDAO daoMov = new MovimientoDAO();

            // Instanciamos el controlador que acabamos de crear
            ControladorMovimientos ctrlMov = new ControladorMovimientos(vistaMov, daoMov);

            // Lo agregamos al escritorio
            vistaPrincipal.escritorio.add(vistaMov);
            vistaMov.setVisible(true);
        }
    }
}