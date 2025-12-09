/*
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package servicio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de Servicio encargada de la gestión de la persistencia de las guías
 * de primeros auxilios. Su principal función es manejar la creación de la carpeta
 * de almacenamiento y guardar/cargar la lista de nombres de archivo de las guías PDF.
 * Demuestra el patrón de **Servicio** y el **Manejo Básico de Archivos** (I/O) para
 * gestionar metadatos (la lista de nombres de archivos).
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class GuiaService {

    /**
     * Ruta relativa a la carpeta donde se almacenarán los archivos físicos de las guías.
     */
    private final String rutaCarpeta = "guias";
    
    /**
     * Ruta relativa al archivo de texto que almacena la lista de nombres de las guías.
     */
    private final String archivoLista = "guias/listaGuias.txt";

    /**
     * Constructor del servicio.
     * Al inicializarse, asegura que la carpeta de almacenamiento de las guías exista,
     * creando la estructura de directorios si es necesario.
     */
    public GuiaService() {
        // Crear carpeta si no existe
        File carpeta = new File(rutaCarpeta);
        if (!carpeta.exists()) carpeta.mkdirs();
    }

    /**
     * Guarda la lista de nombres de archivos de las guías en un archivo de texto.
     * Cada nombre se guarda en una línea separada, sobrescribiendo el contenido anterior.
     * Utiliza {@link BufferedWriter} para una escritura eficiente.
     * @param nombres La lista de {@link String} con los nombres de las guías.
     */
    public void guardarLista(List<String> nombres) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoLista))) {
            for (String nombre : nombres) {
                bw.write(nombre);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar la lista de guías.");
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de nombres de archivos de las guías desde el archivo de texto.
     * Utiliza {@link BufferedReader} para una lectura eficiente, línea por línea.
     * @return Una {@link List} de {@link String} con los nombres de las guías, o una lista vacía si el archivo no existe o hay un error.
     */
    public List<String> cargarLista() {
        List<String> lista = new ArrayList<>();
        File archivo = new File(archivoLista);
        
        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                // Lee cada línea y la añade a la lista
                while ((linea = br.readLine()) != null) {
                    lista.add(linea);
                }
            } catch (IOException e) {
                System.err.println("Error al cargar la lista de guías.");
                e.printStackTrace();
            }
        }
        return lista;
    }
}