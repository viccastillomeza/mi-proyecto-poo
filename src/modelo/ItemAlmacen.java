package modelo;

public abstract class ItemAlmacen {
    private int idItem;
    private String nombre;
    private String tipoItem;
    private String unidadMedida;
    private int stockActual;
    private int stockMinimo;

    // Constructor vacío
    public ItemAlmacen() {}

    // Constructor completo
    public ItemAlmacen(int idItem, String nombre, String tipoItem, String unidadMedida, int stockActual, int stockMinimo) {
        this.idItem = idItem;
        this.nombre = nombre;
        this.tipoItem = tipoItem;
        this.unidadMedida = unidadMedida;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
    }

    // Getters y Setters (Encapsulamiento)
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

    // MÉTODOS ABSTRACTOS: Las clases hijas estarán obligadas a programar su propio comportamiento
    public abstract String generarReporteDetalle();
    
    // Método común para saber si hay que comprar más material
    public boolean requiereReposicion() {
        return this.stockActual <= this.stockMinimo;
    }
}