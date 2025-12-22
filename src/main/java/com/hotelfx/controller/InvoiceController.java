package com.hotelfx.controller;

import com.hotelfx.Main;
import com.hotelfx.model.Facture;
import com.hotelfx.model.Reservation;
import com.hotelfx.service.HotelService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.stream.Collectors;

public class InvoiceController {

    private final HotelService hotelService = new HotelService();

    @FXML private ComboBox<Reservation> cbReservation;
    @FXML private Label lblTotal;
    @FXML private Button btnGenerate;

    @FXML
    public void initialize() {
        loadPendingReservations();
        
        cbReservation.setConverter(new StringConverter<>() {
            @Override
            public String toString(Reservation r) {
                return r == null ? "" : "Res #" + r.getId() + " - " + r.getClient().getNom() + " - " + r.getChambre().getNumero();
            }

            @Override
            public Reservation fromString(String string) { return null; }
        });

        cbReservation.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                double amount = hotelService.calculateTotalAmount(newVal);
                lblTotal.setText(String.format("%.2f €", amount));
                btnGenerate.setDisable(false);
            } else {
                lblTotal.setText("0.00 €");
                btnGenerate.setDisable(true);
            }
        });
    }

    private void loadPendingReservations() {
        // Filter only confirmed reservations that aren't yet billed (facturee)
        // Simplification: In a real app we would have a specific service method for this
        ObservableList<Reservation> pending = FXCollections.observableArrayList(
                hotelService.getAllReservations().stream()
                        .filter(r -> r.getStatut() == Reservation.StatutReservation.CONFIRMEE)
                        .collect(Collectors.toList())
        );
        cbReservation.setItems(pending);
    }

    @FXML
    private void generateInvoice() {
        Reservation res = cbReservation.getValue();
        if (res != null) {
            Facture f = hotelService.generateFacture(res);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Facture générée");
            alert.setHeaderText("Facture #" + f.getId() + " créée avec succès");
            alert.setContentText("Montant total : " + f.getTotal() + " €");
            alert.showAndWait();
            
            // Refresh
            loadPendingReservations();
            lblTotal.setText("0.00 €");
        }
    }
    
    @FXML
    private void backToMain() throws IOException {
        Main.setRoot("main_view");
    }
}
