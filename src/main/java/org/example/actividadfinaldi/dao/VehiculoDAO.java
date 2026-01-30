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
            e.printStackTrace();
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
            String filtro = "matricula=eq." + DatabaseConnection.encode(matricula);
            JSONArray resultados = DatabaseConnection.get("vehiculos", filtro);

            if (resultados.length() > 0) {
                return mapearVehiculo(resultados.getJSONObject(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            String filtro = "activo=eq.true&tipo=eq." + tipo.name() + "&order=matricula.asc";
            JSONArray resultados = DatabaseConnection.get("vehiculos", filtro);

            for (int i = 0; i < resultados.length(); i++) {
                vehiculos.add(mapearVehiculo(resultados.getJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            JSONObject datos = new JSONObject();
            datos.put("poliza_seguro", vehiculo.getPolizaSeguro());
            datos.put("tipo", vehiculo.getTipo().name());
            datos.put("fecha_matriculacion", vehiculo.getFechaMatriculacion().toString());
            datos.put("activo", vehiculo.isActivo());

            String filtro = "id=eq." + vehiculo.getId();
            JSONObject resultado = DatabaseConnection.patch("vehiculos", filtro, datos);

            return resultado != null;
        } catch (Exception e) {
            e.printStackTrace();
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