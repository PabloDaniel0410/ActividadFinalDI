package org.example.actividadfinaldi.dao;

import org.example.actividadfinaldi.model.TipoVehiculo;
import org.example.actividadfinaldi.model.Vehiculo;
import org.example.actividadfinaldi.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Vehiculo
 */
public class VehiculoDAO {

    /**
     * Inserta un nuevo vehiculo en la BD
     * @param vehiculo vehiculo a insertar
     * @return true si se inserto correctamente
     */
    public boolean insertar(Vehiculo vehiculo) {
        String sql = "INSERT INTO vehiculos (matricula, poliza_seguro, tipo, fecha_matriculacion, activo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, vehiculo.getMatricula());
            stmt.setString(2, vehiculo.getPolizaSeguro());
            stmt.setString(3, vehiculo.getTipo().name());
            stmt.setDate(4, Date.valueOf(vehiculo.getFechaMatriculacion()));
            stmt.setBoolean(5, vehiculo.isActivo());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    vehiculo.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
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
        String sql = "SELECT * FROM vehiculos WHERE matricula = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matricula);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearVehiculo(rs);
            }
        } catch (SQLException e) {
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
        String sql = "SELECT * FROM vehiculos WHERE activo = true ORDER BY matricula";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vehiculos.add(mapearVehiculo(rs));
            }
        } catch (SQLException e) {
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
        String sql = "SELECT * FROM vehiculos WHERE activo = true AND tipo = ? ORDER BY matricula";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                vehiculos.add(mapearVehiculo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehiculos;
    }

    /**
     * Actualiza un vehiculo existente
     * @param vehiculo vehiculo a actualizar
     * @return true si se actualizo correctamente
     */
    public boolean actualizar(Vehiculo vehiculo) {
        String sql = "UPDATE vehiculos SET poliza_seguro = ?, tipo = ?, fecha_matriculacion = ?, activo = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehiculo.getPolizaSeguro());
            stmt.setString(2, vehiculo.getTipo().name());
            stmt.setDate(3, Date.valueOf(vehiculo.getFechaMatriculacion()));
            stmt.setBoolean(4, vehiculo.isActivo());
            stmt.setInt(5, vehiculo.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Mapea un ResultSet a un objeto Vehiculo
     * @param rs ResultSet con datos del vehiculo
     * @return objeto Vehiculo
     * @throws SQLException si falla el mapeo
     */
    private Vehiculo mapearVehiculo(ResultSet rs) throws SQLException {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(rs.getInt("id"));
        vehiculo.setMatricula(rs.getString("matricula"));
        vehiculo.setPolizaSeguro(rs.getString("poliza_seguro"));
        vehiculo.setTipo(TipoVehiculo.valueOf(rs.getString("tipo")));
        vehiculo.setFechaMatriculacion(rs.getDate("fecha_matriculacion").toLocalDate());
        vehiculo.setActivo(rs.getBoolean("activo"));
        return vehiculo;
    }
}
