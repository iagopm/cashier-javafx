package controllers.chartMenu;

import controllers.chart.ChartController;
import dao.ApplicationDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChartMenuController implements Initializable {
    public ApplicationDAO dao;
    @FXML
    TextArea codesArea;
    private String[] filter;

    public ChartMenuController(ApplicationDAO dao) {
        this.dao = dao;
    }

    public void generateGraph() {
        Boolean verifier = true;
        String unparsedBarcodes = codesArea.getText();
        String[] parsed = unparsedBarcodes.split(",");

        for (String s : parsed) {
            if (s.length() != 13) {
                System.out.println("#####");
                verifier = false;
            }
        }
        if (unparsedBarcodes.equals("")) {
            verifier = false;
        }
        if (verifier) {
            this.filter = parsed;
            loadChart();
        }

    }

    private void loadChart() {
        Stage stage = new Stage();
        Parent root = null;
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("layouts/chart.fxml"));
            loader.setController(new ChartController(dao, filter));

            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Chart");

        stage.setScene(new Scene(root));
        stage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
