package servicio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GuiaService {

    private final String rutaCarpeta = "guias";
    private final String archivoLista = "guias/listaGuias.txt";

    public GuiaService() {
        // Crear carpeta si no existe
        File carpeta = new File(rutaCarpeta);
        if (!carpeta.exists()) carpeta.mkdirs();
    }

    // Guardar nombres de PDFs en archivo
    public void guardarLista(List<String> nombres) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoLista))) {
            for (String nombre : nombres) {
                bw.write(nombre);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Cargar lista de nombres de PDFs
    public List<String> cargarLista() {
        List<String> lista = new ArrayList<>();
        File archivo = new File(archivoLista);
        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    lista.add(linea);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }
}
