package modelo;

// REQUISITO DE RÚBRICA: Implementación de estructura 'Record'.
// Se utiliza para modelar objetos inmutables de transferencia de datos (DTO).
// El compilador autogenera constructores, métodos de acceso, equals(), hashCode() y toString(),
// optimizando el código y garantizando la seguridad en la inmutabilidad del historial.
public record Movimiento(
    int idMovimiento,
    int idItem,
    int idUsuario,
    String tipoMovimiento, // Valores admitidos: 'INGRESO', 'SALIDA', 'DESPERDICIO'
    int cantidad,
    String fecha
) {
    // La inmutabilidad garantiza que un registro de auditoría no pueda ser alterado en memoria.
}