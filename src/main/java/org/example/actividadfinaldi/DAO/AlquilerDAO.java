package org.example.actividadfinaldi.dao;

import org.example.actividadfinaldi.model.Alquiler;
import org.example.actividadfinaldi.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Alquiler
 */
public class AlquilerDAO {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final VehiculoDAO vehiculoDAO = new VehiculoDAO();

    /**
     * Inserta un nuevo alquiler en la BD
     * @param alquiler alquiler a insertar
     * @return true si se inserto correctamente
     */
    public boolean insertar(Alquiler alquiler) {
        String sql = "INSERT INTO alquileres (cliente_id, vehiculo_id, fecha_inicio, fecha_fin, activo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, alquiler.getCliente().getId());
            stmt.setInt(2, alquiler.getVehiculo().getId());
            stmt.setDate(3, Date.valueOf(alquiler.getFechaInicio()));
            stmt.setDate(4, Date.valueOf(alquiler.getFechaFin()));
            stmt.setBoolean(5, alquiler.isActivo());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    alquiler.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
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
        String sql = "SELECT * FROM alquileres WHERE activo = true ORDER BY fecha_inicio DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                alquileres.add(mapearAlquiler(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alquileres;
    }

    /**
     * Obtiene alquileres de un cliente especifico
     * @param clienteId ID del cliente
     * @return lista de alquileres del cliente
     */
    public List<Alquiler> obtenerPorCliente(int clienteId) {
        List<Alquiler> alquileres = new ArrayList<>();
        String sql = "SELECT * FROM alquileres WHERE cliente_id = ? ORDER BY fecha_inicio DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                alquileres.add(mapearAlquiler(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alquileres;
    }

    /**
     * Mapea un ResultSet a un objeto Alquiler
     * @param rs ResultSet con datos del alquiler
     * @return objeto Alquiler
     * @throws SQLException si falla el mapeo
     */
    private Alquiler mapearAlquiler(ResultSet rs) throws SQLException {
        Alquiler alquiler = new Alquiler();
        alquiler.setId(rs.getInt("id"));
        alquiler.setCliente(clienteDAO.buscarPorDni(
                obtenerDniCliente(rs.getInt("cliente_id"))));
        alquiler.setVehiculo(vehiculoDAO.buscarPorMatricula(
                obtenerMatriculaVehiculo(rs.getInt("vehiculo_id"))));
        alquiler.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
        alquiler.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
        alquiler.setActivo(rs.getBoolean("activo"));
        return alquiler;
    }

    private String obtenerDniCliente(int clienteId) throws SQLException {
        String sql = "SELECT dni FROM clientes WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("dni");
            }
        }
        return null;
    }

    private String obtenerMatriculaVehiculo(int vehiculoId) throws SQLException {
        String sql = "SELECT matricula FROM vehiculos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehiculoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("matricula");
            }
        }
        return null;
    }
}
