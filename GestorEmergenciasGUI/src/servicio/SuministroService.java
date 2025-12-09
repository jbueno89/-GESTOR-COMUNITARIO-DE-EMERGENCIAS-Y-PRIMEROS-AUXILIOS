/*
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package servicio;

import modelo.Suministro;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de Servicio encargada de la lógica de negocio y la persistencia (manejo de archivos)
 * de los objetos {@link Suministro}.
 * Proporciona métodos CRUD y de gestión de estado crítico, manejando la persistencia mediante serialización
 * y asegurando la generación de IDs únicos.
 * Demuestra el patrón de **Servicio** y **Persistencia por Serialización**.
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class SuministroService {

    /**
     * Nombre del archivo de persistencia para los suministros.
     */
    private final String archivo = "suministros.dat";
    
    /**
     * Lista de suministros cargada en memoria.
     */
    private List<Suministro> suministros;
    
    /**
     * Contador interno para asegurar la asignación de IDs únicos e incrementales.
     */
    private int ultimoId = 0;

    /**
     * Constructor del servicio.
     * Carga los datos existentes desde el archivo y determina el último ID utilizado para
     * mantener la secuencia.
     */
    public SuministroService() {
        // Cargar los suministros al inicio
        this.suministros = cargarSuministros();
        
        // Inicializar último ID con el máximo existente
        this.ultimoId = suministros.stream().mapToInt(Suministro::getId).max().orElse(0);
    }

    // ====================================================================
    // PERSISTENCIA
    // ====================================================================

    /**
     * Carga la lista de suministros desde el archivo de persistencia mediante deserialización.
     * @return Una lista de {@link Suministro} cargada, o una lista vacía si falla o no existe el archivo.
     */
    @SuppressWarnings("unchecked")
    public List<Suministro> cargarSuministros() {
        File f = new File(archivo);
        if (!f.exists()) return new ArrayList<>();
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Suministro>) ois.readObject();
        } catch (Exception e) {
            System.err.println("Error al cargar suministros desde el archivo. Inicializando lista vacía.");
            // No imprimir stack trace aquí, es común al inicio
            return new ArrayList<>();
        }
    }

    /**
     * Guarda la lista actual de suministros en el archivo de persistencia mediante serialización.
     */
    public void guardarSuministros() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(suministros);
        } catch (Exception e) {
            System.err.println("Error al guardar suministros en el archivo.");
            e.printStackTrace();
        }
    }

    // ====================================================================
    // OPERACIONES CRUD Y SETTERS
    // ====================================================================

    /**
     * Obtiene la lista completa de suministros cargada en memoria.
     * @return La lista de {@link Suministro}.
     */
    public List<Suministro> getAll() {
        return suministros;
    }

    /**
     * Reemplaza la lista actual de suministros en memoria con una nueva.
     * Este método se usa cuando el controlador maneja la lista completa (ej. para actualizar IDs o después de un filtro).
     * También actualiza el contador de IDs y persiste los cambios inmediatamente.
     * @param lista La nueva lista de {@link Suministro} a establecer.
     */
    public void setSuministros(List<Suministro> lista) {
        this.suministros = lista;
        // Actualizar último ID para futuras inserciones
        this.ultimoId = suministros.stream().mapToInt(Suministro::getId).max().orElse(0);
        // Persistir la lista completa
        guardarSuministros();
    }

    /**
     * Agrega un nuevo suministro a la lista. Asigna un ID único y secuencial si el ID original es 0.
     * Persiste los cambios en el archivo.
     * @param s El objeto {@link Suministro} a agregar.
     */
    public void addSuministro(Suministro s) {
        // Asignar ID automáticamente si es 0
        if (s.getId() == 0) {
            ultimoId++;          // aumentar último ID
            s.setId(ultimoId);   // asignar ID único
        }
        suministros.add(s);
        guardarSuministros();
    }

    /**
     * Actualiza un suministro existente en la lista buscándolo por su ID.
     * Persiste los cambios en el archivo.
     * @param actualizado El objeto {@link Suministro} con los datos modificados.
     */
    public void actualizarSuministro(Suministro actualizado) {
        for (int i = 0; i < suministros.size(); i++) {
            if (suministros.get(i).getId() == actualizado.getId()) {
                suministros.set(i, actualizado);
                guardarSuministros();
                return;
            }
        }
    }

    /**
     * Marca un suministro específico como crítico o no crítico.
     * Persiste los cambios en el archivo.
     * @param id El ID del suministro a modificar.
     * @param critico El nuevo estado crítico (true o false).
     */
    public void marcarCritico(int id, boolean critico) {
        for (Suministro s : suministros) {
            if (s.getId() == id) {
                s.setCritico(critico);
                guardarSuministros();
                return;
            }
        }
    }

    /**
     * Elimina un suministro de la lista por su ID.
     * Persiste los cambios en el archivo.
     * @param id El ID del suministro a eliminar.
     */
    public void eliminarSuministro(int id) {
        suministros.removeIf(s -> s.getId() == id);
        guardarSuministros();
    }

    // ====================================================================
    // MANTENIMIENTO
    // ====================================================================

    /**
     * Ejecuta un reset completo del servicio: limpia la lista en memoria y en el archivo
     * de persistencia, y reinicia el contador de IDs.
     */
    public void resetSuministros() {
        suministros.clear();
        ultimoId = 0; // Reiniciar contador de IDs
        guardarSuministros();
    }
}