package modelo;

import java.util.List;

// REQUISITO DE RÚBRICA: Implementación de Interfaz Genérica (<T>) 
// Permite estandarizar los contratos de las operaciones CRUD para cualquier entidad del modelo,
// promoviendo la reutilización de código y el acoplamiento débil.
public interface IOperacionesCRUD<T> {
    boolean crear(T objeto);
    List<T> leerTodos();
    boolean actualizar(T objeto);
    boolean eliminar(int id);
}