package servicio;

import modelo.Brigadista;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BrigadistaService {

    private final String archivo = "brigadistas.dat";
    private List<Brigadista> brigadistas;

    // Constructor: carga lista desde archivo o crea nueva si no existe
    public BrigadistaService() {
        this.brigadistas = cargarBrigadistas();
    }

    @SuppressWarnings("unchecked")
    private List<Brigadista> cargarBrigadistas() {
        File f = new File(archivo);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Brigadista>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void guardarBrigadistas() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(brigadistas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Obtener todos
    public List<Brigadista> getAllBrigadistas() {
        return new ArrayList<>(brigadistas);
    }

    // Agregar
    public void agregarBrigadista(Brigadista b) {
        brigadistas.add(b);
        guardarBrigadistas();
    }

    // Actualizar
    public void updateBrigadista(Brigadista actualizado) {
        for (int i = 0; i < brigadistas.size(); i++) {
            if (brigadistas.get(i).getId() == actualizado.getId()) {
                brigadistas.set(i, actualizado);
                guardarBrigadistas();
                return;
            }
        }
    }

    // Eliminar
    public void eliminarBrigadista(int id) {
        brigadistas.removeIf(b -> b.getId() == id);
        guardarBrigadistas();
    }

    // Cambiar estado
    public void cambiarEstado(int id, String nuevoEstado) {
        Brigadista b = buscarPorId(id);
        if (b != null) {
            b.setEstado(nuevoEstado);
            guardarBrigadistas();
        }
    }

    // Buscar por ID
    public Brigadista buscarPorId(int id) {
        return brigadistas.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }

    // Buscar por nombre
    public Brigadista buscarPorNombre(String nombre) {
        return brigadistas.stream().filter(b -> b.getNombre().equalsIgnoreCase(nombre)).findFirst().orElse(null);
    }

    // Brigadistas libres
    public List<Brigadista> getBrigadistasLibres() {
        return brigadistas.stream().filter(b -> "Libre".equalsIgnoreCase(b.getEstado())).collect(Collectors.toList());
    }

    // Brigadistas en servicio
    public List<Brigadista> getBrigadistasEnServicio() {
        return brigadistas.stream().filter(b -> "En Servicio".equalsIgnoreCase(b.getEstado())).collect(Collectors.toList());
    }

    // Obtener primer libre
    public Brigadista getPrimerBrigadistaLibre() {
        return brigadistas.stream().filter(b -> "Libre".equalsIgnoreCase(b.getEstado())).findFirst().orElse(null);
    }

    // Reset de brigadistas (opcional, para limpiar todo)
    public void resetBrigadistas() {
        brigadistas.clear();
        guardarBrigadistas();
    }
}
