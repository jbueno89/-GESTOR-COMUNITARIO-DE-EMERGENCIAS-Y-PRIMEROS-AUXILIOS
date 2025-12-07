package servicio;

import modelo.Suministro;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SuministroService {

    private final String archivo = "suministros.dat";
    private List<Suministro> suministros;
    private int ultimoId = 0; // Para asegurar IDs únicos

    public SuministroService() {
        this.suministros = cargarSuministros();
        // Inicializar último ID con el máximo existente
        this.ultimoId = suministros.stream().mapToInt(Suministro::getId).max().orElse(0);
    }

    // ---------------------- Cargar suministros desde archivo ----------------------
    @SuppressWarnings("unchecked")
    public List<Suministro> cargarSuministros() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Suministro>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // ---------------------- Guardar lista completa ----------------------
    public void guardarSuministros() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(suministros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------- Obtener todos ----------------------
    public List<Suministro> getAll() {
        return suministros;
    }

    // ---------------------- Reemplazar lista completa ----------------------
    public void setSuministros(List<Suministro> lista) {
        this.suministros = lista;
        // Actualizar último ID para futuras inserciones
        this.ultimoId = suministros.stream().mapToInt(Suministro::getId).max().orElse(0);
        guardarSuministros();
    }

    // ---------------------- Agregar suministro ----------------------
    public void addSuministro(Suministro s) {
        // Asignar ID automáticamente si es 0
        if (s.getId() == 0) {
            ultimoId++;          // aumentar último ID
            s.setId(ultimoId);   // asignar ID único
        }
        suministros.add(s);
        guardarSuministros();
    }

    // ---------------------- Actualizar suministro ----------------------
    public void actualizarSuministro(Suministro actualizado) {
        for (int i = 0; i < suministros.size(); i++) {
            if (suministros.get(i).getId() == actualizado.getId()) {
                suministros.set(i, actualizado);
                guardarSuministros();
                return;
            }
        }
    }

    // ---------------------- Marcar suministro como crítico ----------------------
    public void marcarCritico(int id, boolean critico) {
        for (Suministro s : suministros) {
            if (s.getId() == id) {
                s.setCritico(critico);
                guardarSuministros();
                return;
            }
        }
    }

    // ---------------------- Eliminar suministro ----------------------
    public void eliminarSuministro(int id) {
        suministros.removeIf(s -> s.getId() == id);
        guardarSuministros();
    }

    // ---------------------- Reset de archivo ----------------------
    public void resetSuministros() {
        suministros.clear();
        ultimoId = 0; // Reiniciar contador de IDs
        guardarSuministros();
    }
}


