package controlador;

import modelo.Brigadista;
import servicio.BrigadistaService;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BrigadistaController {

    private final BrigadistaService brigadistaService;
    private List<Brigadista> brigadistas;

    // Constructor
    public BrigadistaController() {
        brigadistaService = new BrigadistaService();
        brigadistas = new ArrayList<>(brigadistaService.getAllBrigadistas());
    }

    // ---------------------- Obtener lista completa ----------------------
    public List<Brigadista> getAllBrigadistas() {
        return new ArrayList<>(brigadistas);
    }

    // ---------------------- Guardar cambios ----------------------
    private void guardarCambios() {
        brigadistaService.resetBrigadistas(); // limpia archivo
        for (Brigadista b : brigadistas) {
            brigadistaService.agregarBrigadista(b); // vuelve a guardar toda la lista
        }
    }

    // ---------------------- Agregar brigadista ----------------------
    public void agregarBrigadista(Brigadista b, JTable table) {
        brigadistas.add(b);
        guardarCambios();
        actualizarTabla(table);
    }

    // ---------------------- Actualizar brigadista ----------------------
    public void actualizarBrigadista(Brigadista b, JTable table) {
        for (int i = 0; i < brigadistas.size(); i++) {
            if (brigadistas.get(i).getId() == b.getId()) {
                brigadistas.set(i, b);
                break;
            }
        }
        guardarCambios();
        actualizarTabla(table);
    }

    // ---------------------- Eliminar brigadista ----------------------
    public void eliminarBrigadista(int id, JTable table) {
        brigadistas.removeIf(b -> b.getId() == id);
        guardarCambios();
        actualizarTabla(table);
    }

    // ---------------------- Actualizar tabla completa ----------------------
    public void actualizarTabla(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Brigadista b : brigadistas) {
            model.addRow(new Object[]{
                    b.getId(),
                    b.getNombre(),
                    b.getEstado(),
                    b.getEspecialidad(),
                    b.getTelefono()
            });
        }
    }

    // ---------------------- Actualizar tabla con lista filtrada ----------------------
    public void actualizarTablaFiltrada(JTable table, List<Brigadista> lista) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Brigadista b : lista) {
            model.addRow(new Object[]{
                    b.getId(),
                    b.getNombre(),
                    b.getEstado(),
                    b.getEspecialidad(),
                    b.getTelefono()
            });
        }
    }

    // ---------------------- Filtrar por estado ----------------------
    public List<Brigadista> filtrarPorEstado(String estado) {
        if (estado == null || estado.isEmpty() || estado.equalsIgnoreCase("Estado") || estado.equalsIgnoreCase("Todos")) {
            return getAllBrigadistas();
        }
        return brigadistas.stream()
                .filter(b -> b.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }

    // ---------------------- Buscar por nombre ----------------------
    public List<Brigadista> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) return getAllBrigadistas();

        return brigadistas.stream()
                .filter(b -> b.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    // ---------------------- Buscar brigadista por ID ----------------------
    public Brigadista buscarPorId(int id) {
        return brigadistas.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }

    // ---------------------- Reset completo (limpia lista y archivo) ----------------------
    public void resetBrigadistas(JTable table) {
        brigadistas.clear();
        guardarCambios();
        actualizarTabla(table);
    }
}

