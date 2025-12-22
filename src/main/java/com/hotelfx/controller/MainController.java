package com.hotelfx.controller;

import com.hotelfx.Main;
import javafx.fxml.FXML;
import java.io.IOException;

public class MainController {

    @FXML
    private void showRooms() throws IOException {
        Main.setRoot("room_view");
    }

    @FXML
    private void showClients() throws IOException {
        Main.setRoot("client_view");
    }

    @FXML
    private void showReservations() throws IOException {
        Main.setRoot("reservation_view");
    }

    @FXML
    private void showInvoices() throws IOException {
        Main.setRoot("invoice_view");
    }
}
