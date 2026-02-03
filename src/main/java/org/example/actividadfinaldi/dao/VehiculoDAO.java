package org.example.actividadfinaldi.dao;

import org.example.actividadfinaldi.model.TipoVehiculo;
import org.example.actividadfinaldi.model.Vehiculo;
import org.example.actividadfinaldi.util.DatabaseConnection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Vehiculo usando Supabase REST API
 */
public class VehiculoDAO {

    /**
     * Inserta un nuevo vehiculo en la BD
     * @param vehiculo vehiculo a insertar
     * @return true si se insertó correctamente
     */
    public boolean insertar(Vehiculo vehiculo) {
        try {
            // Validación de datos antes de insertar
            if (vehiculo == null || vehiculo.getMatricula() == null ||
                    vehiculo.getPolizaSeguro() == null || vehiculo.getTipo() == null ||
                    vehiculo.getFechaMatriculacion() == null) {
                return false;
            }

            JSONObject datos = new JSONObject();
            datos.put("matricula", vehiculo.getMatricula());
            datos.put("poliza_seguro", vehiculo.getPolizaSeguro());
            datos.put("tipo", vehiculo.getTipo().name());
            datos.put("fecha_matriculacion", vehiculo.getFechaMatriculacion().toString());
            datos.put("activo", vehiculo.isActivo());

            JSONObject resultado = DatabaseConnection.post("vehiculos", datos);

            if (resultado != null && resultado.has("id")) {
                vehiculo.setId(resultado.getInt("id"));
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error al insertar vehículo: " + e.getMessage());
        }
        return false;
    }

    /**
     * Busca un vehiculo por matricula
     * @param matricula matricula del vehiculo
     * @return vehiculo encontrado o null
     */
    public Vehiculo buscarPorMatricula(String matricula) {
        try {
            if (matricula == null || matricula.isEmpty()) {
                return null;
            }

            String filtro = "matricula=eq." + DatabaseConnection.encode(matricula);
            JSONArray resultados = DatabaseConnection.get("vehiculos", filtro);

            if (resultados.length() > 0) {
                return mapearVehiculo(resultados.getJSONObject(0));
            }
        } catch (Exception e) {
            System.err.println("Error al buscar vehículo: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene todos los vehiculos activos
     * @return lista de vehiculos activos
     */
    public List<Vehiculo> obtenerActivos() {
        List<Vehiculo> vehiculos = new ArrayList<>();
        try {
            String filtro = "activo=eq.true&order=matricula.asc";
            JSONArray resultados = DatabaseConnection.get("vehiculos", filtro);

            for (int i = 0; i < resultados.length(); i++) {
                vehiculos.add(mapearVehiculo(resultados.getJSONObject(i)));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener vehículos: " + e.getMessage());
        }
        return vehiculos;
    }

    /**
     * Obtiene vehiculos activos filtrados por tipo
     * @param tipo tipo de vehiculo
     * @return lista de vehiculos del tipo especificado
     */
    public List<Vehiculo> obtenerPorTipo(TipoVehiculo tipo) {
        List<Vehiculo> vehiculos = new ArrayList<>();
        try {
            if (tipo == null) {
                return vehiculos;
            }

            String filtro = "activo=eq.true&tipo=eq." + tipo.name() + "&order=matricula.asc";
            JSONArray resultados = DatabaseConnection.get("vehiculos", filtro);

            for (int i = 0; i < resultados.length(); i++) {
                vehiculos.add(mapearVehiculo(resultados.getJSONObject(i)));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener vehículos por tipo: " + e.getMessage());
        }
        return vehiculos;
    }

    /**
     * Actualiza un vehiculo existente
     * @param vehiculo vehiculo a actualizar
     * @return true si se actualizó correctamente
     */
    public boolean actualizar(Vehiculo vehiculo) {
        try {
            // Validación: el vehículo debe tener ID
            if (vehiculo == null || vehiculo.getId() == null) {
                return false;
            }

            // Validación de datos
            if (vehiculo.getPolizaSeguro() == null || vehiculo.getTipo() == null ||
                    vehiculo.getFechaMatriculacion() == null) {
                return false;
            }

            JSONObject datos = new JSONObject();
            datos.put("poliza_seguro", vehiculo.getPolizaSeguro());
            datos.put("tipo", vehiculo.getTipo().name());
            datos.put("fecha_matriculacion", vehiculo.getFechaMatriculacion().toString());
            datos.put("activo", vehiculo.isActivo());

            String filtro = "id=eq." + vehiculo.getId();
            JSONObject resultado = DatabaseConnection.patch("vehiculos", filtro, datos);

            return resultado != null;
        } catch (Exception e) {
            System.err.println("Error al actualizar vehículo: " + e.getMessage());
        }
        return false;
    }

    /**
     * Mapea un JSONObject a un objeto Vehiculo
     * @param json JSONObject con datos del vehiculo
     * @return objeto Vehiculo
     */
    private Vehiculo mapearVehiculo(JSONObject json) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(json.getInt("id"));
        vehiculo.setMatricula(json.getString("matricula"));
        vehiculo.setPolizaSeguro(json.getString("poliza_seguro"));
        vehiculo.setTipo(TipoVehiculo.valueOf(json.getString("tipo")));
        vehiculo.setFechaMatriculacion(LocalDate.parse(json.getString("fecha_matriculacion")));
        vehiculo.setActivo(json.getBoolean("activo"));
        return vehiculo;
    }
}