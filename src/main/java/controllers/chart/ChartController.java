package controllers.chart;

import dao.ApplicationDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import utils.ProductsUnitSoldHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class ChartController implements Initializable {
    private ApplicationDAO dao;
    @FXML
    PieChart chart;
    private String[] filter;

    public ChartController(ApplicationDAO dao, String[] filter) {
        this.dao = dao;
        this.filter = filter;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chart.setData(new ProductsUnitSoldHandler().handle(dao.findAllCarts(), dao.findAllProducts(), filter));
    }
}
