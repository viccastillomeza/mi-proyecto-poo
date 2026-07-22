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
        
        // Suscripción de eventos para los ítems del menú principal
        this.vistaPrincipal.menuMateriales.addActionListener(this);
        this.vistaPrincipal.menuMovimientos.addActionListener(this);
        this.vistaPrincipal.menuSalir.addActionListener(this);
    }

    public void iniciar() {
        // Modificación dinámica del título integrando los datos de la sesión activa
        vistaPrincipal.setTitle("Sistema de Almacén - Usuario: " + utilidades.Sesion.usuarioLogueado.getUsername());
        vistaPrincipal.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        
        // ====================================================
        // IMPLEMENTACIÓN DE SEGURIDAD: CONTROL DE ACCESO BASADO EN ROLES (RBAC)
        // ====================================================
        String rolUsuario = utilidades.Sesion.usuarioLogueado.getRol();
        
        // Restricción de visibilidad de módulos operativos según el rol del usuario autenticado
        if (rolUsuario.equals("Almacenero")) {
            vistaPrincipal.menuMovimientosBarra.setVisible(false);
        }
        // Para perfiles 'Administrador' o 'Supervisor', el menú mantiene visibilidad completa
        
        vistaPrincipal.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Despliegue del submódulo: Gestión de Inventario
        if (e.getSource() == vistaPrincipal.menuMateriales) {
            
            // 1. Instanciación de componentes internos
            JIfrmMateriales vistaMateriales = new JIfrmMateriales();
            MaterialDAO daoMateriales = new MaterialDAO();

            // 2. Aplicación del patrón MVC: Inyección de la vista y modelo al controlador
            ControladorInventario ctrlMat = new ControladorInventario(vistaMateriales, daoMateriales);

            // 3. Renderizado del InternalFrame dentro del contenedor MDI
            vistaPrincipal.escritorio.add(vistaMateriales);
            vistaMateriales.setVisible(true);
        }
        
        // Ejecución de flujo de Cierre de Sesión
        if (e.getSource() == vistaPrincipal.menuSalir) {
            // Trazabilidad interna de consola para auditoría de acciones del usuario
            System.out.println("Cierre de Sesión interceptado");
            
            // Validación de intencionalidad de salida
            int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
                    vistaPrincipal, 
                    "¿Está seguro que desea cerrar sesión y salir del sistema?", 
                    "Confirmar Salida", 
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE
            );
            
            if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
                // 1. Destrucción de la sesión global activa por seguridad
                utilidades.Sesion.usuarioLogueado = null;
                
                // 2. Liberación de memoria gráfica del contenedor principal
                vistaPrincipal.dispose();
                
                // 3. Reinicio del ciclo de vida del software llamando al Login nuevamente
                vista.FrmLogin vistaLogin = new vista.FrmLogin();
                dao.UsuarioDAO daoUsuario = new dao.UsuarioDAO();
                controlador.ControladorLogin ctrlLogin = new controlador.ControladorLogin(vistaLogin, daoUsuario);
                ctrlLogin.iniciar();
                
                // Nota arquitectónica: Si se requiriera terminar la ejecución del proceso por completo 
                // se podría implementar una llamada a System.exit(0);
            }
        }
        
        // Despliegue del submódulo: Historial Transaccional
        if (e.getSource() == vistaPrincipal.menuMovimientos) {

            JIfrmMovimientos vistaMov = new JIfrmMovimientos();
            MovimientoDAO daoMov = new MovimientoDAO();

            // Despliegue modular del controlador de auditoría
            ControladorMovimientos ctrlMov = new ControladorMovimientos(vistaMov, daoMov);

            // Renderizado en el MDI central
            vistaPrincipal.escritorio.add(vistaMov);
            vistaMov.setVisible(true);
        }
    }
}