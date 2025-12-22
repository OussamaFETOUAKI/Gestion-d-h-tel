package com.hotelfx.controller;

import com.hotelfx.Main;
import com.hotelfx.model.Chambre;
import com.hotelfx.service.HotelService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class RoomController {

    private final HotelService hotelService = new HotelService();

    @FXML private TableView<Chambre> roomTable;
    @FXML private TableColumn<Chambre, Long> colId;
    @FXML private TableColumn<Chambre, String> colNumero;
    @FXML private TableColumn<Chambre, String> colType;
    @FXML private TableColumn<Chambre, Double> colTarif;

    @FXML private TextField txtNumero;
    @FXML private ComboBox<Chambre.TypeChambre> cbType;
    @FXML private TextField txtTarif;
    @FXML private Button btnSave;

    private Chambre selectedChambre;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colTarif.setCellValueFactory(new PropertyValueFactory<>("tarif"));

        cbType.setItems(FXCollections.observableArrayList(Chambre.TypeChambre.values()));

        loadRooms();

        roomTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectRoom(newSelection);
            }
        });
    }

    private void loadRooms() {
        ObservableList<Chambre> rooms = FXCollections.observableArrayList(hotelService.getAllChambres());
        roomTable.setItems(rooms);
    }

    private void selectRoom(Chambre chambre) {
        selectedChambre = chambre;
        txtNumero.setText(chambre.getNumero());
        cbType.setValue(chambre.getType());
        txtTarif.setText(String.valueOf(chambre.getTarif()));
        btnSave.setText("Modifier");
    }

    @FXML
    private void saveRoom() {
        try {
            String numero = txtNumero.getText();
            Chambre.TypeChambre type = cbType.getValue();
            Double tarif = Double.parseDouble(txtTarif.getText());

            if (selectedChambre == null) {
                Chambre newRoom = new Chambre(numero, type, tarif);
                hotelService.saveChambre(newRoom);
            } else {
                selectedChambre.setNumero(numero);
                selectedChambre.setType(type);
                selectedChambre.setTarif(tarif);
                hotelService.saveChambre(selectedChambre);
            }

            clearForm();
            loadRooms();
        } catch (Exception e) {
            showAlert("Erreur", "Données invalides : " + e.getMessage());
        }
    }

    @FXML
    private void deleteRoom() {
        Chambre selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            hotelService.deleteChambre(selected);
            loadRooms();
            clearForm();
        }
    }

    @FXML
    private void clearForm() {
        selectedChambre = null;
        txtNumero.clear();
        cbType.getSelectionModel().clearSelection();
        txtTarif.clear();
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
