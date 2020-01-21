package controllers.login;

import dao.ApplicationDAO;
import dao.ApplicationDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import controllers.mainMenu.MainMenuController;

import java.io.IOException;

public class LoginController {
    public ApplicationDAO dao = new ApplicationDAOImpl();
    @FXML
    Stage primaryStage = new Stage();
    @FXML
    TextField usernameBox;
    @FXML
    PasswordField passwordBox;

    public LoginController() {
        dao.initSession();
    }


    public void loginAction(ActionEvent actionEvent) throws IOException {
        if (dao.login(usernameBox.getText(), passwordBox.getText()).equals("true")) {
            Stage stage = (Stage) usernameBox.getScene().getWindow();
            stage.close();

            MainMenuController mainMenuController = new MainMenuController(usernameBox.getText());
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("layouts/controllers.cashiermenu.mainMenu.fxml"));
            loader.setController(mainMenuController);
            Parent root = loader.load();
            primaryStage.setTitle("application.Main menu");

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            mainMenuController.initialize(loader.getLocation(), loader.getResources());
        }
    }

    public void registerAction(ActionEvent actionEvent) {
        dao.register(usernameBox.getText(), passwordBox.getText());
    }
}
