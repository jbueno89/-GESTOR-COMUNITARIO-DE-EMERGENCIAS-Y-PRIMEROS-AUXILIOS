package servicio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Incidente;

public class IncidenteService {

    private List<Incidente> incidentes;
    private final String RUTA_ARCHIVO = "incidentes.dat"; // Archivo donde se guardan los incidentes

    public IncidenteService() {
        incidentes = cargarIncidentes(); // Cargar al iniciar la aplicación
    }

    // ---------- Obtener todos los incidentes ----------
    public List<Incidente> getAll() {
        return incidentes;
    }

    // ---------- Añadir un incidente ----------
    public void addIncidente(Incidente incidente) {
        incidentes.add(incidente);
        guardarIncidentes(); // Guardar cambios automáticamente
    }

    // ---------- Actualizar un incidente existente ----------
    public void actualizarIncidente(Incidente incidente) {
        for (int i = 0; i < incidentes.size(); i++) {
            if (incidentes.get(i).getId() == incidente.getId()) {
                incidentes.set(i, incidente); // Reemplazar el incidente por el actualizado
                break;
            }
        }
        guardarIncidentes();
    }

    // ---------- Marcar un incidente como resuelto ----------
    public void marcarComoResuelto(int id) {
        for (Incidente i : incidentes) {
            if (i.getId() == id) {
                i.setResuelto(true);
                break;
            }
        }
        guardarIncidentes(); // Guardar cambios automáticamente
    }

    // ---------- Eliminar un incidente ----------
    public void eliminarIncidente(int id) {
        incidentes.removeIf(i -> i.getId() == id);
        guardarIncidentes(); // Guardar cambios automáticamente
    }

    // ---------- Guardar lista en archivo (.dat) ----------
    private void guardarIncidentes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA_ARCHIVO))) {
            oos.writeObject(incidentes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------- Cargar lista desde archivo (.dat) ----------
    @SuppressWarnings("unchecked")
    private List<Incidente> cargarIncidentes() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            return new ArrayList<>(); // No hay archivo, iniciar lista vacía
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Incidente>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>(); // En caso de error, lista vacía
        }
    }
}
