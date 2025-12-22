package com.hotelfx.controller;

import com.hotelfx.Main;
import com.hotelfx.model.Client;
import com.hotelfx.service.HotelService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class ClientController {

    private final HotelService hotelService = new HotelService();

    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, Long> colId;
    @FXML private TableColumn<Client, String> colNom;
    @FXML private TableColumn<Client, String> colEmail;
    @FXML private TableColumn<Client, String> colTelephone;

    @FXML private TextField txtNom;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelephone;
    @FXML private TextField txtSearch;
    @FXML private Button btnSave;

    private Client selectedClient;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        loadClients();

        clientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectClient(newSelection);
            }
        });
    }

    private void loadClients() {
        ObservableList<Client> clients = FXCollections.observableArrayList(hotelService.getAllClients());
        clientTable.setItems(clients);
    }

    @FXML
    private void search() {
        String kw = txtSearch.getText();
        if (kw == null || kw.isEmpty()) {
            loadClients();
        } else {
            ObservableList<Client> clients = FXCollections.observableArrayList(hotelService.searchClients(kw));
            clientTable.setItems(clients);
        }
    }

    private void selectClient(Client client) {
        selectedClient = client;
        txtNom.setText(client.getNom());
        txtEmail.setText(client.getEmail());
        txtTelephone.setText(client.getTelephone());
        btnSave.setText("Modifier");
    }

    @FXML
    private void saveClient() {
        try {
            String nom = txtNom.getText();
            String email = txtEmail.getText();
            String tel = txtTelephone.getText();

            if (selectedClient == null) {
                Client newClient = new Client(nom, email, tel);
                hotelService.saveClient(newClient);
            } else {
                selectedClient.setNom(nom);
                selectedClient.setEmail(email);
                selectedClient.setTelephone(tel);
                hotelService.saveClient(selectedClient);
            }

            clearForm();
            loadClients();
        } catch (Exception e) {
            showAlert("Erreur", "Données invalides : " + e.getMessage());
        }
    }

    @FXML
    private void deleteClient() {
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            hotelService.deleteClient(selected);
            loadClients();
            clearForm();
        }
    }

    @FXML
    private void clearForm() {
        selectedClient = null;
        txtNom.clear();
        txtEmail.clear();
        txtTelephone.clear();
        btnSave.setText("Ajouter");
    }
    
    @FXML
    private void backToMain() throws IOException {
        Main.setRoot("main_view");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
