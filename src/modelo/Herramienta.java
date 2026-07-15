package modelo;

public class Herramienta extends ItemAlmacen {

    public Herramienta() {
        super();
        this.setTipoItem("Herramienta");
    }

    public Herramienta(int idItem, String nombre, String unidadMedida, int stockActual, int stockMinimo) {
        super(idItem, nombre, "Herramienta", unidadMedida, stockActual, stockMinimo);
    }

    // POLIMORFISMO: Comportamiento distinto al MaterialObra
    @Override
    public String generarReporteDetalle() {
        String estado = requiereReposicion() ? "URGENTE COMPRAR" : "STOCK ÓPTIMO";
        return "Herramienta: " + getNombre() + " | Disponibles: " + getStockActual() + " | Estado: " + estado;
    }
}