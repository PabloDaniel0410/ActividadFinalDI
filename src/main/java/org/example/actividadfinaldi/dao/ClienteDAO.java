package org.example.actividadfinaldi.dao;

import org.example.actividadfinaldi.model.Cliente;
import org.example.actividadfinaldi.util.DatabaseConnection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Cliente usando Supabase REST API
 */
public class ClienteDAO {

    /**
     * Inserta un nuevo cliente en la BD
     * @param cliente cliente a insertar
     * @return true si se insertó correctamente
     */
    public boolean insertar(Cliente cliente) {
        try {
            JSONObject datos = new JSONObject();
            datos.put("nombre", cliente.getNombre());
            datos.put("apellidos", cliente.getApellidos());
            datos.put("dni", cliente.getDni());
            datos.put("fecha_nacimiento", cliente.getFechaNacimiento().toString());
            datos.put("activo", cliente.isActivo());

            JSONObject resultado = DatabaseConnection.post("clientes", datos);

            if (resultado != null && resultado.has("id")) {
                cliente.setId(resultado.getInt("id"));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Busca un cliente por DNI
     * @param dni DNI del cliente
     * @return cliente encontrado o null
     */
    public Cliente buscarPorDni(String dni) {
        try {
            String filtro = "dni=eq." + DatabaseConnection.encode(dni);
            JSONArray resultados = DatabaseConnection.get("clientes", filtro);

            if (resultados.length() > 0) {
                return mapearCliente(resultados.getJSONObject(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtiene todos los clientes activos
     * @return lista de clientes activos
     */
    public List<Cliente> obtenerActivos() {
        List<Cliente> clientes = new ArrayList<>();
        try {
            String filtro = "activo=eq.true&order=nombre.asc";
            JSONArray resultados = DatabaseConnection.get("clientes", filtro);

            for (int i = 0; i < resultados.length(); i++) {
                clientes.add(mapearCliente(resultados.getJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientes;
    }

    /**
     * Actualiza un cliente existente
     * @param cliente cliente a actualizar
     * @return true si se actualizó correctamente
     */
    public boolean actualizar(Cliente cliente) {
        try {
            JSONObject datos = new JSONObject();
            datos.put("nombre", cliente.getNombre());
            datos.put("apellidos", cliente.getApellidos());
            datos.put("fecha_nacimiento", cliente.getFechaNacimiento().toString());
            datos.put("activo", cliente.isActivo());

            String filtro = "id=eq." + cliente.getId();
            JSONObject resultado = DatabaseConnection.patch("clientes", filtro, datos);

            return resultado != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Mapea un JSONObject a un objeto Cliente
     * @param json JSONObject con datos del cliente
     * @return objeto Cliente
     */
    private Cliente mapearCliente(JSONObject json) {
        Cliente cliente = new Cliente();
        cliente.setId(json.getInt("id"));
        cliente.setNombre(json.getString("nombre"));
        cliente.setApellidos(json.getString("apellidos"));
        cliente.setDni(json.getString("dni"));
        cliente.setFechaNacimiento(LocalDate.parse(json.getString("fecha_nacimiento")));
        cliente.setActivo(json.getBoolean("activo"));
        return cliente;
    }
}