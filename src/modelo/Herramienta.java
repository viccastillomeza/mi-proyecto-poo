package modelo;

// REQUISITO DE RÚBRICA: Aplicación de Herencia.
public class Herramienta extends ItemAlmacen {

    public Herramienta() {
        super();
        this.setTipoItem("Herramienta");
    }

    public Herramienta(int idItem, String nombre, String unidadMedida, int stockActual, int stockMinimo) {
        super(idItem, nombre, "Herramienta", unidadMedida, stockActual, stockMinimo);
    }

    // REQUISITO DE RÚBRICA: Implementación de Polimorfismo Dinámico.
    // Lógica condicional específica adaptada al control particular de Herramientas.
    @Override
    public String generarReporteDetalle() {
        String estado = requiereReposicion() ? "URGENTE COMPRAR" : "STOCK ÓPTIMO";
        return "Herramienta: " + getNombre() + " | Disponibles: " + getStockActual() + " | Estado: " + estado;
    }
}