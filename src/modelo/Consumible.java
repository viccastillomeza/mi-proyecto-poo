package modelo;

public class Consumible extends ItemAlmacen {

    public Consumible() {
        super();
        this.setTipoItem("Consumible");
    }

    public Consumible(int idItem, String nombre, String unidadMedida, int stockActual, int stockMinimo) {
        super(idItem, nombre, "Consumible", unidadMedida, stockActual, stockMinimo);
    }

    @Override
    public String generarReporteDetalle() {
        return "Consumible: " + getNombre() + " | Cantidad: " + getStockActual() + " " + getUnidadMedida();
    }
}