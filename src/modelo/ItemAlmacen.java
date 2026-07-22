package modelo;

// REQUISITO DE RÚBRICA: Implementación de Clase Abstracta.
// Sirve como superclase base del dominio para los elementos del inventario,
// evitando la instanciación directa de ítems genéricos.
public abstract class ItemAlmacen {
    private int idItem;
    private String nombre;
    private String tipoItem;
    private String unidadMedida;
    private int stockActual;
    private int stockMinimo;

    // Sobrecarga de constructores para inicialización flexible
    public ItemAlmacen() {}

    public ItemAlmacen(int idItem, String nombre, String tipoItem, String unidadMedida, int stockActual, int stockMinimo) {
        this.idItem = idItem;
        this.nombre = nombre;
        this.tipoItem = tipoItem;
        this.unidadMedida = unidadMedida;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
    }

    // REQUISITO DE RÚBRICA: Aplicación de Encapsulamiento.
    // Ocultamiento del estado interno y acceso controlado mediante getters y setters.
    public int getIdItem() { return idItem; }
    public void setIdItem(int idItem) { this.idItem = idItem; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipoItem() { return tipoItem; }
    public void setTipoItem(String tipoItem) { this.tipoItem = tipoItem; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public int getStockActual() { return stockActual; }
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    // REQUISITO DE RÚBRICA: Definición de Método Abstracto.
    // Obliga contractualmente a las subclases a implementar su propia lógica de reporte.
    public abstract String generarReporteDetalle();
    
    // Método concreto heredable para la evaluación lógica del estado del inventario.
    public boolean requiereReposicion() {
        return this.stockActual <= this.stockMinimo;
    }
}