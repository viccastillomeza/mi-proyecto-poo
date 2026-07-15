package modelo;

// La palabra reservada "record" reemplaza a "class"
public record Movimiento(
    int idMovimiento,
    int idItem,
    int idUsuario,
    String tipoMovimiento, // 'INGRESO', 'SALIDA', 'DESPERDICIO'
    int cantidad,
    String fecha
) {
    // ¡Y eso es todo! 
    // Java ya creó internamente los constructores y los métodos para obtener los datos.
}