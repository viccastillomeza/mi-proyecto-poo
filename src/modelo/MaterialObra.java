package modelo;

public class MaterialObra extends ItemAlmacen {
    private int desperdicioAcumulado;

    public MaterialObra() {
        super();
        this.setTipoItem("MaterialObra");
    }

    public MaterialObra(int idItem, String nombre, String unidadMedida, int stockActual, int stockMinimo, int desperdicioAcumulado) {
        super(idItem, nombre, "MaterialObra", unidadMedida, stockActual, stockMinimo);
        this.desperdicioAcumulado = desperdicioAcumulado;
    }

    public int getDesperdicioAcumulado() { return desperdicioAcumulado; }
    public void setDesperdicioAcumulado(int desperdicioAcumulado) { this.desperdicioAcumulado = desperdicioAcumulado; }

    // POLIMORFISMO: Sobreescribimos el método abstracto
    @Override
    public String generarReporteDetalle() {
        return "Material de Obra: " + getNombre() + " | Stock: " + getStockActual() + " " + getUnidadMedida() + " | Merma: " + desperdicioAcumulado;
    }
}