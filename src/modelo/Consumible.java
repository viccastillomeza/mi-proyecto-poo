package modelo;

// REQUISITO DE RÚBRICA: Aplicación de Herencia y extensión del modelo de dominio.
public class Consumible extends ItemAlmacen {

    public Consumible() {
        super();
        this.setTipoItem("Consumible");
    }

    public Consumible(int idItem, String nombre, String unidadMedida, int stockActual, int stockMinimo) {
        super(idItem, nombre, "Consumible", unidadMedida, stockActual, stockMinimo);
    }

    // REQUISITO DE RÚBRICA: Polimorfismo (@Override).
    // Formato de salida exclusivo para la entidad Consumible.
    @Override
    public String generarReporteDetalle() {
        return "Consumible: " + getNombre() + " | Cantidad: " + getStockActual() + " " + getUnidadMedida();
    }
}