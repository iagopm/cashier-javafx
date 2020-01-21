package controllers.cashiermenu;

import dao.ApplicationDAO;
import dao.ApplicationDAOImpl;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Cart;
import model.Product;
import model.TableRow;
import model.Ticket;
import sun.security.util.Resources;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CashierController {
    ApplicationDAO dao = new ApplicationDAOImpl();
    ResourceBundle resourceBundle = Resources.getBundle("machineConfig");

    @FXML
    TextField scanEmulatorBox;
    @FXML
    Label totalLabel;
    @FXML
    TableView<TableRow> monitorTable;
    @FXML
    Label payMonitor;
    @FXML
    Label changeLabel;
    @FXML
    Label clock;

    Integer cartId;
    Boolean isPaid = false;
    Boolean isEnded = false;
    String currency = resourceBundle.getString("currency");
    String username;
    List<Cart> cartOnBuild;

    List<Product> allProducts = dao.findAllProducts();
    List<Product> productsOnUse = new ArrayList<>();

    private void reset() {
        isPaid = false;
        isEnded = false;
        productsOnUse = new ArrayList<>();
        cartOnBuild = new ArrayList<>();
        totalLabel.setText("");
        changeLabel.setText("");
        payMonitor.setText("");
        refreshTable();
    }

    public CashierController(String username) {
        this.username = username;
    }

    public void initialize(URL location, ResourceBundle resources) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("layouts/scanEmulator.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        stage.setTitle("Scan emulator");
        stage.setScene(new Scene(root));
        stage.show();
        if (clock != null) {
            Platform.runLater(() -> {
                // Update UI here.
                Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e -> {
                    LocalTime currentTime = LocalTime.now();
                    clock.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());
                }),
                        new KeyFrame(Duration.seconds(1))
                );
                time.setCycleCount(Animation.INDEFINITE);
                time.play();
            });

        }
    }

    public void scan(ActionEvent actionEvent) {
        Long barcode = Long.parseLong(scanEmulatorBox.getText());
        if (cartOnBuild == null) {
            cartOnBuild = new ArrayList<>();
        }
        List<Cart> cartToCommitToOnBuild = new ArrayList<>();
        cartOnBuild.forEach(c -> cartToCommitToOnBuild.add(c));
        Cart cartNew = null;
        for (Product product : allProducts) {
            if (product.getBarcode().equals(barcode)) {
                if (cartOnBuild.isEmpty()) {
                    cartToCommitToOnBuild.add(new Cart(cartId, product.getBarcode(), 1));
                    productsOnUse.add(product);
                    break;
                }

                for (Cart cart : cartOnBuild) {
                    if (!productsOnUse.contains(product)) {
                        cartToCommitToOnBuild.add(new Cart(cartId, product.getBarcode(), 1));
                        productsOnUse.add(product);
                    }
                    if (cart.getProductId().equals(product.getBarcode())) {
                        cart.setQuantity(cart.quantity += 1);
                    }
                }
            }
        }
        //Remove duplicates
        cartOnBuild = new ArrayList<>(cartToCommitToOnBuild);
        calculateTotal();
        refreshTable();
    }

    private Float calculateTotal() {
        Float totalPrice = 0F;
        for (Cart c : cartOnBuild) {
            Float quantity = Float.parseFloat(c.getQuantity().toString());
            Float price = 0F;
            Float priceToAdd = 0F;
            for (Product product : allProducts) {
                if (product.getBarcode().equals(c.getProductId())) {
                    price = product.getPrice();
                }
            }
            priceToAdd = quantity * price;
            totalPrice += priceToAdd;
        }
        totalLabel.setText(totalPrice.toString() + currency);
        return totalPrice;
    }

    public void refreshTable() {
        List<TableRow> tableRows = new ArrayList<>();

        for (Cart cart : cartOnBuild) {
            String barcode;
            String name = null;
            String units;
            String unitPrice;
            String totalPrice;
            Float pricePerUnits = 0F;
            barcode = cart.getProductId().toString();
            for (Product product : allProducts) {
                if (product.getBarcode() == cart.getProductId()) {
                    name = product.getName();
                    pricePerUnits = product.getPrice();
                    unitPrice = product.getPrice().toString();
                }
            }
            units = cart.getQuantity().toString();
            Float totalPriceOfProduct = pricePerUnits * Float.parseFloat(cart.getQuantity().toString());
            totalPrice = totalPriceOfProduct.toString();
            tableRows.add(new TableRow(barcode, name, units, pricePerUnits.toString(), totalPrice));

        }
        ObservableList<TableRow> tables = FXCollections.observableArrayList();
        tables.addAll(tableRows);
        monitorTable.setItems(tables);
    }

    public void deleteOneItem(ActionEvent actionEvent) {
        TableRow tableRow = monitorTable.getSelectionModel().getSelectedItem();
        for (Cart cart : cartOnBuild) {
            if (cart.getProductId() ==
                    Long.parseLong(tableRow.getBarcode())) {
                if (cart.getQuantity() == 1) {
                    //delete from products in use and memory
                    for (Product product : productsOnUse) {
                        if (product.getBarcode() == cart.getProductId()) {
                            cartOnBuild.remove(tableRow);
                            productsOnUse.remove(product);
                        }
                    }
                } else {
                    cart.setQuantity(cart.quantity -= 1);
                }
            }

        }
        calculateTotal();
        refreshTable();
    }

    public void pay(ActionEvent actionEvent) {
        if (!cartOnBuild.isEmpty()) {
            isPaid = true;
        }
    }

    public void enter(ActionEvent actionEvent) throws ScriptException {
        if (isPaid) {
            try {
                Float total = calculateTotal();
                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByName("js");
                Object result = engine.eval(payMonitor.getText());
                Float paid = Float.parseFloat(result.toString());
                Float change = paid - total;
                changeLabel.setText(change.toString() + currency);
                persistTicket(cartOnBuild);
                if (isEnded) {
                    reset();
                }
                isEnded = true;
            } catch (Exception e) {
                e.printStackTrace();
                payMonitor.setText("");
            }
        }
    }


    private void persistTicket(List<Cart> cartOnBuild) {
        Ticket ticket = new Ticket(username);
        dao.persistTicket(ticket);
        System.out.println(cartOnBuild.size());
        cartOnBuild.stream().forEach(c -> c.setTicketId(ticket.getId()));
        dao.persistCart(cartOnBuild);
        dao.initSession();
    }

    public void openEmulator(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("layouts/scanEmulator.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        stage.setTitle("Scan emulator");

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void zero(ActionEvent actionEvent) {
        payMonitor.setText(payMonitor.getText() + 0);
    }

    public void one(ActionEvent actionEvent) {
        payMonitor.setText(payMonitor.getText() + 1);
    }

    public void two(ActionEvent actionEvent) {
        payMonitor.setText(payMonitor.getText() + 2);
    }

    public void three(ActionEvent actionEvent) {
        payMonitor.setText(payMonitor.getText() + 3);
    }

    public void four(ActionEvent actionEvent) {
        payMonitor.setText(payMonitor.getText() + 4);
    }

    public void five(ActionEvent actionEvent) {
        payMonitor.setText(payMonitor.getText() + 5);
    }

    public void six(ActionEvent actionEvent) {
        payMonitor.setText(payMonitor.getText() + 6);
    }

    public void seven(ActionEvent actionEvent) {
        payMonitor.setText(payMonitor.getText() + 7);
    }

    public void eight(ActionEvent actionEvent) {
        payMonitor.setText(payMonitor.getText() + 8);
    }

    public void nine(ActionEvent actionEvent) {
        payMonitor.setText(payMonitor.getText() + 9);
    }

    public void clear(ActionEvent actionEvent) {
        payMonitor.setText("");
    }

    public void plus(ActionEvent actionEvent) {
        payMonitor.setText(payMonitor.getText() + "+");
    }

    public void substract(ActionEvent actionEvent) {
        payMonitor.setText(payMonitor.getText() + "-");
    }

    public void multiply(ActionEvent actionEvent) {
        payMonitor.setText(payMonitor.getText() + "*");
    }

    public void slash(ActionEvent actionEvent) {
        payMonitor.setText(payMonitor.getText() + "/");
    }

    public void dot(ActionEvent actionEvent) {
        payMonitor.setText(payMonitor.getText() + ".");
    }

}
