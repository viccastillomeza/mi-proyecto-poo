package utilidades;

import modelo.Usuario;

// Implementación de gestión de estado global para la sesión activa
public class Sesion {
    
    // Mantenimiento de la identidad del usuario autenticado en memoria estática.
    // Permite el acceso transversal a los privilegios del usuario desde cualquier capa del sistema (Controladores, Vistas, DAOs)
    // sin necesidad de pasar el objeto como parámetro constantemente.
    public static Usuario usuarioLogueado;
}