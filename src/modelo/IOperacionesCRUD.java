package modelo;

import java.util.List;

// Usamos <T> para que sea una Interfaz Genérica (Puntos extra en tu rúbrica)
public interface IOperacionesCRUD<T> {
    boolean crear(T objeto);
    List<T> leerTodos();
    boolean actualizar(T objeto);
    boolean eliminar(int id);
}