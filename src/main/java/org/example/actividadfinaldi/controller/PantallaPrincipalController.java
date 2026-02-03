package org.example.actividadfinaldi.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.actividadfinaldi.dao.AlquilerDAO;
import org.example.actividadfinaldi.dao.ClienteDAO;
import org.example.actividadfinaldi.dao.VehiculoDAO;
import org.example.actividadfinaldi.model.Alquiler;
import org.example.actividadfinaldi.model.Cliente;
import org.example.actividadfinaldi.model.TipoVehiculo;
import org.example.actividadfinaldi.model.Vehiculo;
import org.example.actividadfinaldi.service.AlquilerFileService;

import java.time.LocalDate;

/**
 * Controlador de la pantalla principal
 */
public class PantallaPrincipalController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtDni;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private Button btnRegistrarCliente;

    @FXML private TextField txtMatricula;
    @FXML private TextField txtPoliza;
    @FXML private ComboBox<TipoVehiculo> cmbTipoVehiculo;
    @FXML private DatePicker dpFechaMatriculacion;
    @FXML private Button btnRegistrarVehiculo;

    @FXML private TableView<Cliente> tableClientesTab;
    @FXML private TableColumn<Cliente, String> colNombreClienteTab;
    @FXML private TableColumn<Cliente, String> colApellidosClienteTab;
    @FXML private TableColumn<Cliente, String> colDniClienteTab;
    @FXML private TableColumn<Cliente, Integer> colEdadClienteTab;

    @FXML private TableView<Vehiculo> tableVehiculosTab;
    @FXML private TableColumn<Vehiculo, String> colMatriculaTab;
    @FXML private TableColumn<Vehiculo, String> colPolizaTab;
    @FXML private TableColumn<Vehiculo, TipoVehiculo> colTipoTab;
    @FXML private TableColumn<Vehiculo, Integer> colAñosUsoTab;

    @FXML private TableView<Cliente> tableClientes;
    @FXML private TableColumn<Cliente, String> colNombreCliente;
    @FXML private TableColumn<Cliente, String> colApellidosCliente;
    @FXML private TableColumn<Cliente, String> colDniCliente;
    @FXML private TableColumn<Cliente, Integer> colEdadCliente;

    @FXML private TableView<Vehiculo> tableVehiculos;
    @FXML private TableColumn<Vehiculo, String> colMatricula;
    @FXML private TableColumn<Vehiculo, String> colPoliza;
    @FXML private TableColumn<Vehiculo, TipoVehiculo> colTipo;
    @FXML private TableColumn<Vehiculo, Integer> colAñosUso;

    @FXML private ComboBox<TipoVehiculo> cmbFiltroTipo;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private Button btnAlquilar;
    @FXML private TextArea txtAreaAlquileres;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final VehiculoDAO vehiculoDAO = new VehiculoDAO();
    private final AlquilerDAO alquilerDAO = new AlquilerDAO();
    private final AlquilerFileService fileService = new AlquilerFileService();

    private ObservableList<Cliente> clientesData = FXCollections.observableArrayList();
    private ObservableList<Vehiculo> vehiculosData = FXCollections.observableArrayList();
    private ObservableList<Vehiculo> vehiculosDataTab = FXCollections.observableArrayList();

    /**
     * Inicializa el controlador
     */
    @FXML
    public void initialize() {
        configurarTablaClientesTab();
        configurarTablaVehiculosTab();
        configurarTablaClientes();
        configurarTablaVehiculos();
        configurarComboBoxes();
        cargarDatos();
        configurarListeners();
    }


    private void configurarTablaClientesTab() {
        colNombreClienteTab.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidosClienteTab.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colDniClienteTab.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colEdadClienteTab.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getEdad()).asObject());

        tableClientesTab.setItems(clientesData);
    }

    private void configurarTablaVehiculosTab() {
        colMatriculaTab.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colPolizaTab.setCellValueFactory(new PropertyValueFactory<>("polizaSeguro"));
        colTipoTab.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colAñosUsoTab.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getAñosUso()).asObject());

        tableVehiculosTab.setItems(vehiculosDataTab);
    }

    private void configurarTablaClientes() {
        colNombreCliente.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidosCliente.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colDniCliente.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colEdadCliente.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getEdad()).asObject());

        tableClientes.setItems(clientesData);
    }

    private void configurarTablaVehiculos() {
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colPoliza.setCellValueFactory(new PropertyValueFactory<>("polizaSeguro"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colAñosUso.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getAñosUso()).asObject());

        tableVehiculos.setItems(vehiculosData);
    }

    private void configurarComboBoxes() {
        cmbTipoVehiculo.setItems(FXCollections.observableArrayList(TipoVehiculo.values()));
        cmbFiltroTipo.setItems(FXCollections.observableArrayList(TipoVehiculo.values()));
    }

    private void configurarListeners() {
        tableClientes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> verificarActivarBotonAlquilar());

        tableVehiculos.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> verificarActivarBotonAlquilar());

        dpFechaInicio.valueProperty().addListener(
                (obs, oldVal, newVal) -> verificarActivarBotonAlquilar());

        dpFechaFin.valueProperty().addListener(
                (obs, oldVal, newVal) -> verificarActivarBotonAlquilar());
    }

    private void cargarDatos() {
        clientesData.setAll(clienteDAO.obtenerActivos());
        vehiculosData.setAll(vehiculoDAO.obtenerActivos());
        vehiculosDataTab.setAll(vehiculoDAO.obtenerActivos());
        cargarAlquileres();
    }

    /**
     * Registra un nuevo cliente
     */
    @FXML
    private void handleRegistrarCliente() {
        try {
            String nombre = txtNombre.getText().trim();
            String apellidos = txtApellidos.getText().trim();
            String dni = txtDni.getText().trim();
            LocalDate fechaNac = dpFechaNacimiento.getValue();

            if (nombre.isEmpty() || apellidos.isEmpty() || dni.isEmpty() || fechaNac == null) {
                mostrarAlerta("Error", "Todos los campos son obligatorios", Alert.AlertType.ERROR);
                return;
            }

            if (clienteDAO.buscarPorDni(dni) != null) {
                mostrarAlerta("Error", "Ya existe un cliente con ese DNI", Alert.AlertType.ERROR);
                return;
            }

            Cliente cliente = new Cliente(nombre, apellidos, dni, fechaNac);

            if (!cliente.esMayorDe25()) {
                mostrarAlerta("Error", "El cliente debe ser mayor de 25 años", Alert.AlertType.ERROR);
                return;
            }

            if (clienteDAO.insertar(cliente)) {
                mostrarAlerta("Exito", "Cliente registrado correctamente", Alert.AlertType.INFORMATION);
                limpiarFormularioCliente();
                cargarDatos();
            } else {
                mostrarAlerta("Error", "No se pudo registrar el cliente", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al registrar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Registra un nuevo vehiculo
     */
    @FXML
    private void handleRegistrarVehiculo() {
        try {
            String matricula = txtMatricula.getText().trim();
            String poliza = txtPoliza.getText().trim();
            TipoVehiculo tipo = cmbTipoVehiculo.getValue();
            LocalDate fechaMat = dpFechaMatriculacion.getValue();

            if (matricula.isEmpty() || poliza.isEmpty() || tipo == null || fechaMat == null) {
                mostrarAlerta("Error", "Todos los campos son obligatorios", Alert.AlertType.ERROR);
                return;
            }

            if (vehiculoDAO.buscarPorMatricula(matricula) != null) {
                mostrarAlerta("Error", "Ya existe un vehiculo con esa matricula", Alert.AlertType.ERROR);
                return;
            }

            Vehiculo vehiculo = new Vehiculo(matricula, poliza, tipo, fechaMat);

            if (vehiculo.debeSerDadoDeBaja()) {
                mostrarAlerta("Advertencia", "El vehiculo tiene mas de 10 años", Alert.AlertType.WARNING);
            }

            if (vehiculoDAO.insertar(vehiculo)) {
                mostrarAlerta("Exito", "Vehiculo registrado correctamente", Alert.AlertType.INFORMATION);
                limpiarFormularioVehiculo();
                cargarDatos();
            } else {
                mostrarAlerta("Error", "No se pudo registrar el vehiculo", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al registrar vehiculo: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Filtra vehiculos por tipo
     */
    @FXML
    private void handleFiltrarVehiculos() {
        TipoVehiculo tipo = cmbFiltroTipo.getValue();
        if (tipo == null) {
            vehiculosDataTab.setAll(vehiculoDAO.obtenerActivos());
        } else {
            vehiculosDataTab.setAll(vehiculoDAO.obtenerPorTipo(tipo));
        }
    }

    /**
     * Realiza un alquiler
     */
    @FXML
    private void handleAlquilar() {
        try {
            Cliente cliente = tableClientes.getSelectionModel().getSelectedItem();
            Vehiculo vehiculo = tableVehiculos.getSelectionModel().getSelectedItem();
            LocalDate fechaInicio = dpFechaInicio.getValue();
            LocalDate fechaFin = dpFechaFin.getValue();

            Alquiler alquiler = new Alquiler(cliente, vehiculo, fechaInicio, fechaFin);

            if (!alquiler.fechasValidas()) {
                mostrarAlerta("Error", "La fecha de fin debe ser posterior a la de inicio", Alert.AlertType.ERROR);
                return;
            }

            if (alquilerDAO.insertar(alquiler)) {
                fileService.guardarAlquiler(alquiler);
                mostrarAlerta("Exito", "Alquiler realizado correctamente", Alert.AlertType.INFORMATION);
                limpiarFormularioAlquiler();
                cargarAlquileres();
            } else {
                mostrarAlerta("Error", "No se pudo realizar el alquiler", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al realizar alquiler: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void verificarActivarBotonAlquilar() {
        boolean activar = tableClientes.getSelectionModel().getSelectedItem() != null
                && tableVehiculos.getSelectionModel().getSelectedItem() != null
                && dpFechaInicio.getValue() != null
                && dpFechaFin.getValue() != null;
        btnAlquilar.setDisable(!activar);
    }

    private void cargarAlquileres() {
        txtAreaAlquileres.clear();
        for (Alquiler alquiler : alquilerDAO.obtenerActivos()) {
            txtAreaAlquileres.appendText(alquiler.toString() + "\n");
        }
    }

    private void limpiarFormularioCliente() {
        txtNombre.clear();
        txtApellidos.clear();
        txtDni.clear();
        dpFechaNacimiento.setValue(null);
    }

    private void limpiarFormularioVehiculo() {
        txtMatricula.clear();
        txtPoliza.clear();
        cmbTipoVehiculo.setValue(null);
        dpFechaMatriculacion.setValue(null);
    }

    private void limpiarFormularioAlquiler() {
        tableClientes.getSelectionModel().clearSelection();
        tableVehiculos.getSelectionModel().clearSelection();
        dpFechaInicio.setValue(null);
        dpFechaFin.setValue(null);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}