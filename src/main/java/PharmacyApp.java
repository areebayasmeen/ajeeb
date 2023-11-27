package com.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class PharmacyApp extends Application {

    private User user;
    private ObservableList<Medicine> pharmacyInventory;
    private TableView<Medicine> medicineTableView;
    private ObservableList<Order> orderHistory = FXCollections.observableArrayList();

    private User loggedInUser;

    private ObservableList<Medicine> cart = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a stage for user authentication
        Stage authStage = new Stage();
        authStage.setTitle("User Authentication");

        GridPane authGridPane = createAuthGridPane(authStage);

        // Create a scene for user authentication
        Scene authScene = new Scene(authGridPane, 400, 200);

        // Set the scene on the authentication stage
        authStage.setScene(authScene);

        // Show the authentication stage
        authStage.show();
    }

    private GridPane createAuthGridPane(Stage authStage) {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(15);
        gridPane.setHgap(15);
        gridPane.setPadding(new Insets(20, 20, 20, 20));

        Text title = new Text("Welcome to Pharmacy App");
        title.setFont(new Font("Times new roman", 20));
        Label userLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");

        TextField usernameTextField = new TextField();
        PasswordField passwordField = new PasswordField();

        gridPane.add(title, 0, 0, 2, 1);

        Button loginButton = new Button("Login");
        Button exitButton = new Button("Exit");
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().add(loginButton);
        hBox.getChildren().add(exitButton);

        gridPane.add(userLabel, 0, 1);
        gridPane.add(usernameTextField, 1, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(hBox, 1, 4);

        exitButton.setOnAction(e-> System.out.println("Exit button clicked"));
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                authStage.close();
                Platform.exit();
            }
        });

        loginButton.setOnAction(e -> {
            // Check authentication (for simplicity, assuming correct username and password)
            loggedInUser = new User("John Doe", "123 Main St", "555-1234");
            authStage.close(); // Close the authentication stage
            showMedicineListStage(); // Show the medicine list stage
        });

        return gridPane;
    }

    private void showMedicineListStage() {
        // Create a stage for medicine list and search
        Stage medicineListStage = new Stage();
        TableView<Medicine> cartTableView = new TableView<>();
        user = loggedInUser;
        medicineListStage.setTitle("Medicine List and Search");

        // Populate the pharmacyInventory (assuming it is not null)
        pharmacyInventory = FXCollections.observableArrayList(
                new Medicine("Panadol", 01, "Fever and Pain Relief", 50.0, 100),
                new Medicine("Neurofen", 02, "Headache and Inflammation", 120.0, 50),
                new Medicine("Amoxilin", 03, "Antibiotic", 80.0, 30),
                new Medicine("Ventolin", 04, "Asthma Inhaler", 150.0, 20),
                new Medicine("Zyrtec", 05, "Allergy Relief", 60.0, 40),
                new Medicine("Aspirin", 06, "Pain and Inflammation", 30.0, 90),
                new Medicine("Lipitor", 7, "Cholesterol Control", 80.0, 25),
                new Medicine("Prozac", 8, "Antidepressant", 75.0, 35),
                new Medicine("Nexium", 9, "Acid Reflux Relief", 110.0, 15),
                new Medicine("Synthroid", 10, "Thyroid Medication", 40.0, 50)

        );

        GridPane gridPane = new GridPane();
        gridPane.setVgap(15);
        gridPane.setHgap(15);
        gridPane.setPadding(new Insets(20, 20, 20, 20));

        // Create a TableView to display medicine names, prices, and quantities
        medicineTableView = new TableView<>();

        TableColumn<Medicine, Integer> idColumn= new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Medicine, String> nameColumn = new TableColumn<>("Medicine Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Medicine, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Medicine, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Medicine, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        medicineTableView.getColumns().addAll(idColumn, nameColumn, descriptionColumn, priceColumn, quantityColumn);
        medicineTableView.setItems(pharmacyInventory);

        // Set preferred widths for each column
        idColumn.setPrefWidth(0.1 * 600); // 20% of the stage width
        nameColumn.setPrefWidth(0.2 * 600); // 40% of the stage width
        descriptionColumn.setPrefWidth(0.3 * 600); // 40% of the stage width
        priceColumn.setPrefWidth(0.2 * 600); // 20% of the stage width
        quantityColumn.setPrefWidth(0.2 * 600); // 20% of the stage width

        // Set the preferred width for the TableView itself
        medicineTableView.setPrefWidth(600);
        medicineTableView.setPrefHeight(300); // Set your preferred height

        // Create a ListView to display medicine names, prices, and quantities
        ListView<String> medicineListView = new ListView<>();
        for (Medicine medicine : pharmacyInventory) {
            String medicineInfo = String.format("%-10d %-10s %-10s Rs.%-15.1f %-15d",
                    medicine.getId(), medicine.getName(), medicine.getDescription(), medicine.getPrice(), medicine.getQuantity());
            medicineListView.getItems().add(medicineInfo);
        }

        // Create a TextField for searching medicines
        TextField searchTextField = new TextField();
        searchTextField.setPromptText("Search Medicine");

        Button searchButton= new Button("Search");
        searchButton.setOnAction(e -> {
            String searchText = searchTextField.getText().trim().toLowerCase();

            if (!searchText.isEmpty()) {
                ObservableList<Medicine> searchResult = FXCollections.observableArrayList();

                for (Medicine medicine : pharmacyInventory) {
                    if (medicine.getName().toLowerCase().contains(searchText)) {
                        searchResult.add(medicine);
                    }
                }

                medicineTableView.setItems(searchResult);
            } else {
                // If the search text is empty, show the entire inventory
                medicineTableView.setItems(pharmacyInventory);
            }
        });


        // Create a button to add selected medicine to the cart
        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setOnAction(e -> {
            Medicine selectedMedicine = medicineTableView.getSelectionModel().getSelectedItem();
            if (selectedMedicine != null) {
                cart.add(selectedMedicine);
                System.out.println("Added to cart: " + selectedMedicine.getName());
                // Update the cartTableView to reflect the changes in the cart
                cartTableView.setItems(cart);
            }
        });



        // Create a button to proceed to the next stage (Cart and Order Placement)
        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> {
            System.out.println("Next button clicked");
            medicineListStage.close();
            showCartAndOrderPlacementStage();
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            medicineListStage.close();
            // Optionally, you can show the authentication stage again
            Stage authStage = new Stage();
            authStage.setTitle("User Authentication");
            GridPane authGridPane = createAuthGridPane(authStage);
            Scene authScene = new Scene(authGridPane, 400, 200);
            authStage.setScene(authScene);
            authStage.show();
        });

        HBox hBox = new HBox();
        hBox.setSpacing(10);
//        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().add(searchTextField);
        hBox.getChildren().add(searchButton);
        hBox.getChildren().add(addToCartButton);
        hBox.getChildren().add(nextButton);
        hBox.getChildren().add(backButton);

        // Add UI elements to the gridPane
        gridPane.add(new Label("Medicine Inventory"), 0, 0, 2, 1); // Heading
        // Add the TableView to the gridPane
        gridPane.add(medicineTableView, 0, 1, 2, 1);
//        gridPane.add(medicineListView, 0, 0, 2, 1);
        gridPane.add(hBox, 1, 2);

        // Create a scene for medicine list and search
        Scene medicineListScene = new Scene(gridPane, 700, 500);

        // Set the scene on the medicine list stage
        medicineListStage.setScene(medicineListScene);

        // Show the medicine list stage
        medicineListStage.show();
    }

    private void showCartAndOrderPlacementStage() {
        // Create a stage for cart and order placement
        Stage cartStage = new Stage();
        cartStage.setTitle("Cart and Order Placement");

        GridPane gridPane = new GridPane();
        gridPane.setVgap(15);
        gridPane.setHgap(15);
        gridPane.setPadding(new Insets(20, 20, 20, 20));

        // Create a TableView to display items in the cart with prices
        TableView<Medicine> cartTableView = new TableView<>();

        TableColumn<Medicine, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Medicine, String> nameColumn = new TableColumn<>("Medicine Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Medicine, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        cartTableView.getColumns().addAll(idColumn, nameColumn, priceColumn);
        cartTableView.setItems(FXCollections.observableArrayList(cart));

        // Set preferred widths for each column
        idColumn.setPrefWidth(0.3 * 300);
        nameColumn.setPrefWidth(0.4 * 300);
        priceColumn.setPrefWidth(0.3 * 300);

        cartTableView.setPrefWidth(300);
        cartTableView.setPrefHeight(500);

        // Create a label to display the total cart price
        Label totalLabel = new Label("Total Price: Rs." + calculateTotalCartPrice());

        Button placeOrderButton = new Button("Place Order");
        placeOrderButton.setOnAction(e -> {
            user.placeOrder();
            cart.clear();
            System.out.println("Order placed!");
            cartStage.close();
            // Optionally, show any other stages or dialogs related to order placement
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            cartStage.close();
            // Optionally, go back to the previous stage
            // For example, show the medicine list stage again
            showMedicineListStage();
        });

        gridPane.add(new Label("Cart Summary"), 0, 0, 2, 1); // Heading
        // Add the TableView to the gridPane
        gridPane.add(cartTableView, 0, 1, 2, 1);
        gridPane.add(totalLabel, 0, 2, 2, 1);
        gridPane.add(placeOrderButton, 1, 3);
        gridPane.add(backButton, 0, 3);

        // Create a scene for cart and order placement
        Scene cartScene = new Scene(gridPane, 600, 400);

        // Set the scene on the cart stage
        cartStage.setScene(cartScene);

        // Show the cart stage
        cartStage.show();
        cartStage.setTitle("Cart and Order Placement - User: " + loggedInUser.getName());
    }
    private double calculateTotalCartPrice() {
        double totalCartPrice = 0.0;
        for (Medicine medicine : cart) {
            totalCartPrice += medicine.getPrice();
        }
        return totalCartPrice;
    }


    private void showOrderHistoryStage() {
        // Create a stage for order history
        Stage orderHistoryStage = new Stage();
        orderHistoryStage.setTitle("Order History");

        GridPane gridPane = new GridPane();
        gridPane.setVgap(15);
        gridPane.setHgap(15);
        gridPane.setPadding(new Insets(20, 20, 20, 20));

        // Create a TableView to display order history
        TableView<Order> orderTableView = new TableView<>();

        TableColumn<Order, Integer> orderIdColumn = new TableColumn<>("Order ID");
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));

        TableColumn<Order, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Order, Double> totalAmountColumn = new TableColumn<>("Total Amount");
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        orderTableView.getColumns().addAll(orderIdColumn, dateColumn, totalAmountColumn);
        orderTableView.setItems(orderHistory);

        // Set preferred widths for each column
        orderIdColumn.setPrefWidth(0.3 * 400);
        dateColumn.setPrefWidth(0.4 * 400);
        totalAmountColumn.setPrefWidth(0.3 * 400);

        orderTableView.setPrefWidth(400);
        orderTableView.setPrefHeight(300);

        // Create a button to go back to the medicine list stage
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            orderHistoryStage.close();
            showMedicineListStage();
        });

        gridPane.add(new Label("Order History"), 0, 0, 2, 1); // Heading
        // Add the TableView to the gridPane
        gridPane.add(orderTableView, 0, 1, 2, 1);
        gridPane.add(backButton, 1, 2);

        // Create a scene for order history
        Scene orderHistoryScene = new Scene(gridPane, 500, 400);

        // Set the scene on the order history stage
        orderHistoryStage.setScene(orderHistoryScene);

        // Show the order history stage
        orderHistoryStage.show();
    }

}
