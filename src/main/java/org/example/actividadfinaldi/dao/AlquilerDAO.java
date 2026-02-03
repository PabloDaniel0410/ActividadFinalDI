package org.example.actividadfinaldi.dao;

import org.example.actividadfinaldi.model.Alquiler;
import org.example.actividadfinaldi.model.Cliente;
import org.example.actividadfinaldi.model.Vehiculo;
import org.example.actividadfinaldi.util.DatabaseConnection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Alquiler usando Supabase REST API
 */
public class AlquilerDAO {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final VehiculoDAO vehiculoDAO = new VehiculoDAO();

    /**
     * Inserta un nuevo alquiler en la BD
     * @param alquiler alquiler a insertar
     * @return true si se insertó correctamente
     */
    public boolean insertar(Alquiler alquiler) {
        try {
            JSONObject datos = new JSONObject();
            datos.put("cliente_id", alquiler.getCliente().getId());
            datos.put("vehiculo_id", alquiler.getVehiculo().getId());
            datos.put("fecha_inicio", alquiler.getFechaInicio().toString());
            datos.put("fecha_fin", alquiler.getFechaFin().toString());
            datos.put("activo", alquiler.isActivo());

            JSONObject resultado = DatabaseConnection.post("alquileres", datos);

            if (resultado != null && resultado.has("id")) {
                alquiler.setId(resultado.getInt("id"));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Obtiene todos los alquileres activos
     * @return lista de alquileres activos
     */
    public List<Alquiler> obtenerActivos() {
        List<Alquiler> alquileres = new ArrayList<>();
        try {
            String filtro = "activo=eq.true&order=fecha_inicio.desc";
            JSONArray resultados = DatabaseConnection.get("alquileres", filtro);

            for (int i = 0; i < resultados.length(); i++) {
                Alquiler alquiler = mapearAlquiler(resultados.getJSONObject(i));
                if (alquiler != null) {
                    alquileres.add(alquiler);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alquileres;
    }

    /**
     * Obtiene alquileres de un cliente específico
     * @param clienteId ID del cliente
     * @return lista de alquileres del cliente
     */
    public List<Alquiler> obtenerPorCliente(int clienteId) {
        List<Alquiler> alquileres = new ArrayList<>();
        try {
            String filtro = "cliente_id=eq." + clienteId + "&order=fecha_inicio.desc";
            JSONArray resultados = DatabaseConnection.get("alquileres", filtro);

            for (int i = 0; i < resultados.length(); i++) {
                Alquiler alquiler = mapearAlquiler(resultados.getJSONObject(i));
                if (alquiler != null) {
                    alquileres.add(alquiler);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alquileres;
    }

    /**
     * Mapea un JSONObject a un objeto Alquiler
     * @param json JSONObject con datos del alquiler
     * @return objeto Alquiler o null si hay error
     */
    private Alquiler mapearAlquiler(JSONObject json) {
        try {
            Alquiler alquiler = new Alquiler();
            alquiler.setId(json.getInt("id"));

            int clienteId = json.getInt("cliente_id");
            int vehiculoId = json.getInt("vehiculo_id");

            // Obtiene el  cliente y vehículo
            Cliente cliente = obtenerClientePorId(clienteId);
            Vehiculo vehiculo = obtenerVehiculoPorId(vehiculoId);

            if (cliente == null || vehiculo == null) {
                return null;
            }

            alquiler.setCliente(cliente);
            alquiler.setVehiculo(vehiculo);
            alquiler.setFechaInicio(LocalDate.parse(json.getString("fecha_inicio")));
            alquiler.setFechaFin(LocalDate.parse(json.getString("fecha_fin")));
            alquiler.setActivo(json.getBoolean("activo"));

            return alquiler;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene un cliente por su ID
     */
    private Cliente obtenerClientePorId(int id) {
        try {
            String filtro = "id=eq." + id;
            JSONArray resultados = DatabaseConnection.get("clientes", filtro);

            if (resultados.length() > 0) {
                JSONObject json = resultados.getJSONObject(0);
                Cliente cliente = new Cliente();
                cliente.setId(json.getInt("id"));
                cliente.setNombre(json.getString("nombre"));
                cliente.setApellidos(json.getString("apellidos"));
                cliente.setDni(json.getString("dni"));
                cliente.setFechaNacimiento(LocalDate.parse(json.getString("fecha_nacimiento")));
                cliente.setActivo(json.getBoolean("activo"));
                return cliente;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtiene un vehículo por su ID
     */
    private Vehiculo obtenerVehiculoPorId(int id) {
        try {
            String filtro = "id=eq." + id;
            JSONArray resultados = DatabaseConnection.get("vehiculos", filtro);

            if (resultados.length() > 0) {
                JSONObject json = resultados.getJSONObject(0);
                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setId(json.getInt("id"));
                vehiculo.setMatricula(json.getString("matricula"));
                vehiculo.setPolizaSeguro(json.getString("poliza_seguro"));
                vehiculo.setTipo(org.example.actividadfinaldi.model.TipoVehiculo.valueOf(json.getString("tipo")));
                vehiculo.setFechaMatriculacion(LocalDate.parse(json.getString("fecha_matriculacion")));
                vehiculo.setActivo(json.getBoolean("activo"));
                return vehiculo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}