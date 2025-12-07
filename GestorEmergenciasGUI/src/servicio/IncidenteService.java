package servicio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import modelo.Incidente;
import modelo.Brigadista;

public class IncidenteService {

    private List<Incidente> incidentes;
    private final String RUTA_ARCHIVO = "incidentes.dat";

    private BrigadistaService brigadistaService;

    // Inyección del servicio de brigadistas (NO crear uno nuevo)
    public IncidenteService(BrigadistaService brigadistaService) {
        this.brigadistaService = brigadistaService;
        this.incidentes = cargarIncidentes();
    }

    public List<Incidente> getAll() {
        return incidentes;
    }

    public int getNuevoId() {
        if (incidentes.isEmpty()) return 1;

        return incidentes.stream()
                .mapToInt(Incidente::getId)
                .max()
                .orElse(0) + 1;
    }

    // -----------------------------------------------------------
    // AGREGAR INCIDENTE (NO asigna brigadista por defecto)
    // -----------------------------------------------------------
    public void addIncidente(Incidente incidente) {
        // Garantizar que esté vacío
        incidente.setIdBrigadista(0);
        incidente.setNombreBrigadista("");
        incidentes.add(incidente);
        guardarIncidentes();
    }

    // -----------------------------------------------------------
    // ACTUALIZAR INCIDENTE
    // -----------------------------------------------------------
    public void actualizarIncidente(Incidente incidente) {
        for (int i = 0; i < incidentes.size(); i++) {
            if (incidentes.get(i).getId() == incidente.getId()) {
                incidentes.set(i, incidente);
                break;
            }
        }
        guardarIncidentes();
    }

    // -----------------------------------------------------------
    // MARCAR INCIDENTE COMO RESUELTO
    // -----------------------------------------------------------
    public void marcarComoResuelto(int id) {
        for (Incidente i : incidentes) {
            if (i.getId() == id) {
                i.setResuelto(true);
                i.setTiempoActivo("Finalizado");

                // Liberar brigadista si estaba asignado
                if (i.getIdBrigadista() != 0) {
                    Brigadista b = brigadistaService.buscarPorId(i.getIdBrigadista());
                    if (b != null) {
                        b.setEstado("Libre");
                        brigadistaService.updateBrigadista(b);
                    }
                }

                // Limpiar asignación
                i.setIdBrigadista(0);
                i.setNombreBrigadista("");
                break;
            }
        }
        guardarIncidentes();
    }

    // -----------------------------------------------------------
    // ELIMINAR INCIDENTE
    // -----------------------------------------------------------
    public void eliminarIncidente(int id) {
        incidentes.removeIf(i -> i.getId() == id);
        guardarIncidentes();
    }

    // -----------------------------------------------------------
    // DESASIGNAR BRIGADISTA
    // -----------------------------------------------------------
    public void desasignarBrigadistaDeIncidente(int idIncidente) {
        Incidente inc = buscarPorId(idIncidente);
        if (inc != null) {
            // Liberar brigadista si estaba asignado
            if (inc.getIdBrigadista() != 0) {
                Brigadista b = brigadistaService.buscarPorId(inc.getIdBrigadista());
                if (b != null) {
                    b.setEstado("Libre");
                    brigadistaService.updateBrigadista(b);
                }
            }

            inc.setIdBrigadista(0);
            inc.setNombreBrigadista("");
            guardarIncidentes();
        }
    }

    // -----------------------------------------------------------
    // BUSCAR INCIDENTE POR ID
    // -----------------------------------------------------------
    public Incidente buscarPorId(int id) {
        for (Incidente i : incidentes) {
            if (i.getId() == id) return i;
        }
        return null;
    }

    // -----------------------------------------------------------
    // GUARDAR INCIDENTES EN ARCHIVO
    // -----------------------------------------------------------
    private void guardarIncidentes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA_ARCHIVO))) {
            oos.writeObject(incidentes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // -----------------------------------------------------------
    // CARGAR INCIDENTES DESDE ARCHIVO
    // -----------------------------------------------------------
    @SuppressWarnings("unchecked")
    private List<Incidente> cargarIncidentes() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Incidente>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
