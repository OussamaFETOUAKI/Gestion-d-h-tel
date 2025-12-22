package com.hotelfx.controller;

import com.hotelfx.Main;
import com.hotelfx.model.Chambre;
import com.hotelfx.model.Client;
import com.hotelfx.model.Reservation;
import com.hotelfx.service.HotelService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;

public class ReservationController {

    private final HotelService hotelService = new HotelService();

    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Long> colId;
    @FXML private TableColumn<Reservation, Chambre> colChambre;
    @FXML private TableColumn<Reservation, Client> colClient;
    @FXML private TableColumn<Reservation, LocalDate> colDebut;
    @FXML private TableColumn<Reservation, LocalDate> colFin;
    @FXML private TableColumn<Reservation, Reservation.StatutReservation> colStatut;

    @FXML private ComboBox<Chambre> cbChambre;
    @FXML private ComboBox<Client> cbClient;
    @FXML private DatePicker dpDebut;
    @FXML private DatePicker dpFin;
    @FXML private Button btnSave;

    private Reservation selectedReservation;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colChambre.setCellValueFactory(new PropertyValueFactory<>("chambre"));
        colClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        colDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        setupComboBoxes();
        loadReservations();

        reservationTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectReservation(newVal);
            }
        });
    }

    private void setupComboBoxes() {
        cbChambre.setItems(FXCollections.observableArrayList(hotelService.getAllChambres()));
        cbClient.setItems(FXCollections.observableArrayList(hotelService.getAllClients()));

        // Display Converters
        cbChambre.setConverter(new StringConverter<>() {
            @Override
            public String toString(Chambre object) {
                return object == null ? "" : object.getNumero() + " (" + object.getType() + ")";
            }
            @Override
            public Chambre fromString(String string) { return null; }
        });

        cbClient.setConverter(new StringConverter<>() {
            @Override
            public String toString(Client object) {
                return object == null ? "" : object.getNom() + " (" + object.getEmail() + ")";
            }
            @Override
            public Client fromString(String string) { return null; }
        });
    }

    private void loadReservations() {
        ObservableList<Reservation> res = FXCollections.observableArrayList(hotelService.getAllReservations());
        reservationTable.setItems(res);
    }

    private void selectReservation(Reservation res) {
        selectedReservation = res;
        cbChambre.setValue(res.getChambre());
        cbClient.setValue(res.getClient());
        dpDebut.setValue(res.getDateDebut());
        dpFin.setValue(res.getDateFin());
        
        // Basic editing - usually we wouldn't allow full edit of confirmed reservations easily without availability check re-run
        // disabling edit of dates if confirmed might be safer, but for this prototype we implement basic mode.
        btnSave.setText("Modifier");
    }

    @FXML
    private void saveReservation() {
        try {
            Chambre ch = cbChambre.getValue();
            Client cl = cbClient.getValue();
            LocalDate start = dpDebut.getValue();
            LocalDate end = dpFin.getValue();

            if (ch == null || cl == null || start == null || end == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            if (selectedReservation == null) {
                hotelService.createReservation(ch, cl, start, end);
            } else {
                // Update logic - simplistic
                selectedReservation.setChambre(ch);
                selectedReservation.setClient(cl);
                selectedReservation.setDateDebut(start);
                selectedReservation.setDateFin(end);
                 // Note: Ideally re-check availability here
                hotelService.updateReservation(selectedReservation);
            }

            clearForm();
            loadReservations();
        } catch (IllegalArgumentException e) {
            showAlert("Erreur Business", e.getMessage());
        } catch (Exception e) {
            showAlert("Erreur", "Erreur système : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelReservation() {
        Reservation res = reservationTable.getSelectionModel().getSelectedItem();
        if (res != null) {
            hotelService.cancelReservation(res);
            loadReservations();
            clearForm();
        }
    }

    @FXML
    private void clearForm() {
        selectedReservation = null;
        cbChambre.getSelectionModel().clearSelection();
        cbClient.getSelectionModel().clearSelection();
        dpDebut.setValue(null);
        dpFin.setValue(null);
        btnSave.setText("Réserver");
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
