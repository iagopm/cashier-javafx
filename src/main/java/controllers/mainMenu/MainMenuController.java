package controllers.mainMenu;

import controllers.cashiermenu.CashierController;
import com.itextpdf.text.DocumentException;
import dao.ApplicationDAO;
import dao.ApplicationDAOImpl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.GenerateInform;
import utils.ParseCartToInform;
import utils.ProductsUnitSoldHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController {
    ApplicationDAO dao = new ApplicationDAOImpl();


    String username;
    @FXML
    Label welcomeLabel;
    @FXML
    Stage primaryStage = new Stage();
    @FXML
    PieChart chart;

    public MainMenuController(String username) {
        this.username = username;
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        welcomeLabel.setText("Welcome: " + username);
    }

    public void openGraph(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("layouts/chart.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        stage.setTitle("Chart");

        stage.setScene(new Scene(root));
        stage.show();
        chart.setData(new ProductsUnitSoldHandler().handle(dao.findAllCarts(), dao.findAllProducts()));
    }


    public void generateInform(ActionEvent actionEvent) {
        dao.initSession();
        try {
            new GenerateInform(new ParseCartToInform().parse(dao.findAllCarts(), dao.findAllTickets()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    public void settings(ActionEvent actionEvent) {
        File file = new File("src/main/resources/machineConfig.properties");



        try {
            Runtime.getRuntime().exec("Notepad " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    public void openCartManager(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.close();

        CashierController cashierController = new CashierController(username);
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("layouts/cashierMenu.fxml"));
        loader.setController(cashierController);
        Parent root = loader.load();
        primaryStage.setTitle("Cashier Menu");

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                MainMenuController mainMenuController = new MainMenuController(username);
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("layouts/controllers.cashiermenu.mainMenu.fxml"));
                loader.setController(mainMenuController);
                Parent root = null;
                Stage stage1 = new Stage();
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage1.setTitle("application.Main menu");

                stage1.setScene(new Scene(root));
                stage1.show();
                mainMenuController.initialize(loader.getLocation(), loader.getResources());
            }
        });
        cashierController.initialize(loader.getLocation(), loader.getResources());
    }
}
