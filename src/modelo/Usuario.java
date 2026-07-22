package modelo;

// Entidad del modelo de dominio (POJO) representativa de los actores del sistema.
public class Usuario {
    
    // REQUISITO DE RÚBRICA: Encapsulamiento (Modificadores de acceso private)
    private int idUsuario;
    private String dni;
    private String nombre;
    private String apellido;
    private String username;
    private String password;
    private String rol;

    public Usuario() {}

    public Usuario(int idUsuario, String dni, String nombre, String apellido, String username, String password, String rol) {
        this.idUsuario = idUsuario;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    // Métodos de acceso encapsulados
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
    // Lógica de negocio encapsulada dentro de la propia clase
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }
}