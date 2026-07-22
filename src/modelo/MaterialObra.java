package modelo;

// REQUISITO DE RÚBRICA: Aplicación de Herencia (extends ItemAlmacen).
public class MaterialObra extends ItemAlmacen {
    
    // Atributo específico de la subclase (Especialización)
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

    // REQUISITO DE RÚBRICA: Polimorfismo por Sobreescritura (@Override).
    // Implementación del comportamiento específico de la subclase MaterialObra.
    @Override
    public String generarReporteDetalle() {
        return "Material de Obra: " + getNombre() + " | Stock: " + getStockActual() + " " + getUnidadMedida() + " | Merma: " + desperdicioAcumulado;
    }
}