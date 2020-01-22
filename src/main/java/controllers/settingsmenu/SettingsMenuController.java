package controllers.settingsmenu;

import dao.ApplicationDAO;
import dao.ApplicationDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import sun.security.util.Resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SettingsMenuController implements Initializable {
    private ApplicationDAO dao = new ApplicationDAOImpl();
    private FileInputStream in;

    {
        try {
            in = new FileInputStream("src/main/resources/machineConfig.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private Properties props;

    @FXML
    ChoiceBox<String> currencyBox;
    @FXML
    TextField companyNameBox;
    @FXML
    TextField addressBox;
    @FXML
    TextField ticketNameBox;
    @FXML
    TextField taxesBox;
    @FXML
    TextField phoneNumberBox;


    public SettingsMenuController(ApplicationDAO dao) {
        this.dao = dao;
        loadConfig();
    }

    public void loadFields() {
        companyNameBox.setText(props.getProperty("companyName"));
        addressBox.setText(props.getProperty("address"));
        ticketNameBox.setText(props.getProperty("ticketName"));
        taxesBox.setText(props.getProperty("taxes"));
        phoneNumberBox.setText(props.getProperty("phoneNumber"));
    }

    public void loadChoiceBox() {
        String[] currenciesArray = props.getProperty("allCurrencies").split(",");
        currencyBox.getItems().addAll(currenciesArray);
    }

    public void loadConfig() {
        FileInputStream in = null;
        try {
            in = new FileInputStream("src/main/resources/machineConfig.properties");
            props = new Properties();
            props.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Cargar las configs

    public void changeSettings() {
        if (!companyNameBox.getText().equals(props.getProperty("companyName"))) {
            replaceValue("companyName", companyNameBox.getText());
        }
        if (!addressBox.getText().equals(props.getProperty("address"))) {
            replaceValue("address", addressBox.getText());
        }
        if (!ticketNameBox.getText().equals(props.getProperty("ticketName"))) {
            replaceValue("ticketName", ticketNameBox.getText());
        }
        if (!taxesBox.getText().equals(props.getProperty("taxes"))) {
            replaceValue("taxes", taxesBox.getText());
        }
        if (!phoneNumberBox.getText().equals(props.getProperty("phoneNumber"))) {
            replaceValue("phoneNumber", phoneNumberBox.getText());
        }

        if (!currencyBox.getSelectionModel().getSelectedItem().toString().equals(props.getProperty("currency"))) {
            replaceValue("currency", currencyBox.getSelectionModel().getSelectedItem().toString());
        }
    }


    public void replaceValue(String key, String value) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("src/main/resources/machineConfig.properties");
            props.setProperty(key, value);
            props.store(out, null);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Value with key " + key + " replaced with value " + value);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadChoiceBox();
        loadFields();

    }
}
