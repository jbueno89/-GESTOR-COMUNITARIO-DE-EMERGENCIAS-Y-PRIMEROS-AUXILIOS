package servicio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Suministro;

public class SuministroService {

    private List<Suministro> suministros;
    private int nextId = 1; // ID automático
    private final String RUTA_ARCHIVO = "suministros.dat";

    public SuministroService() {
        suministros = cargarSuministros();
        // actualizar nextId si ya hay elementos cargados
        for (Suministro s : suministros) {
            if (s.getId() >= nextId) {
                nextId = s.getId() + 1;
            }
        }
    }

    public List<Suministro> getAll() {
        return suministros;
    }

    public void addSuministro(Suministro s) {
        s.setId(nextId++); // asignar ID único
        suministros.add(s);
        guardarSuministros();
    }

    public void actualizarSuministro(Suministro s) {
        for (int i = 0; i < suministros.size(); i++) {
            if (suministros.get(i).getId() == s.getId()) {
                suministros.set(i, s);
                break;
            }
        }
        guardarSuministros();
    }

    public void eliminarSuministro(int id) {
        suministros.removeIf(s -> s.getId() == id);
        guardarSuministros();
    }

    private void guardarSuministros() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA_ARCHIVO))) {
            oos.writeObject(suministros);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private List<Suministro> cargarSuministros() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Suministro>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

