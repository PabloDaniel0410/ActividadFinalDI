package org.example.actividadfinaldi.dao;

import org.example.actividadfinaldi.model.Cliente;
import org.example.actividadfinaldi.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Cliente
 */
public class ClienteDAO {

    /**
     * Inserta un nuevo cliente en la BD
     * @param cliente cliente a insertar
     * @return true si se inserto correctamente
     */
    public boolean insertar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, apellidos, dni, fecha_nacimiento, activo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellidos());
            stmt.setString(3, cliente.getDni());
            stmt.setDate(4, Date.valueOf(cliente.getFechaNacimiento()));
            stmt.setBoolean(5, cliente.isActivo());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
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
        String sql = "SELECT * FROM clientes WHERE dni = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearCliente(rs);
            }
        } catch (SQLException e) {
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
        String sql = "SELECT * FROM clientes WHERE activo = true ORDER BY nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    /**
     * Actualiza un cliente existente
     * @param cliente cliente a actualizar
     * @return true si se actualizo correctamente
     */
    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre = ?, apellidos = ?, fecha_nacimiento = ?, activo = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellidos());
            stmt.setDate(3, Date.valueOf(cliente.getFechaNacimiento()));
            stmt.setBoolean(4, cliente.isActivo());
            stmt.setInt(5, cliente.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Mapea un ResultSet a un objeto Cliente
     * @param rs ResultSet con datos del cliente
     * @return objeto Cliente
     * @throws SQLException si falla el mapeo
     */
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setApellidos(rs.getString("apellidos"));
        cliente.setDni(rs.getString("dni"));
        cliente.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        cliente.setActivo(rs.getBoolean("activo"));
        return cliente;
    }
}
