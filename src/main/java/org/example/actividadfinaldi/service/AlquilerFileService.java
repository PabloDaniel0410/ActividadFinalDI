package org.example.actividadfinaldi.service;

import org.example.actividadfinaldi.model.Alquiler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Servicio para guardar alquileres en archivo de texto
 * Ampliacion A
 */
public class AlquilerFileService {

    private static final String ARCHIVO = "alquileres.txt";

    /**
     * Guarda un alquiler en el archivo de texto
     * @param alquiler alquiler a guardar
     * @return true si se guardo correctamente
     */
    public boolean guardarAlquiler(Alquiler alquiler) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            String linea = formatearAlquiler(alquiler);
            writer.write(linea);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Formatea un alquiler para guardarlo en archivo
     * @param alquiler alquiler a formatear
     * @return linea de texto con datos del alquiler
     */
    private String formatearAlquiler(Alquiler alquiler) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);

        return String.format("%s | Cliente: %s %s (DNI: %s) | Vehiculo: %s | Inicio: %s | Fin: %s | Dias: %d",
                timestamp,
                alquiler.getCliente().getNombre(),
                alquiler.getCliente().getApellidos(),
                alquiler.getCliente().getDni(),
                alquiler.getVehiculo().getMatricula(),
                alquiler.getFechaInicio(),
                alquiler.getFechaFin(),
                alquiler.getDuracionDias());
    }
}