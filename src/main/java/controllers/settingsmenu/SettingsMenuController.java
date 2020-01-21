package controllers.settingsmenu;

import dao.ApplicationDAO;
import dao.ApplicationDAOImpl;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import sun.security.util.Resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

public class SettingsMenuController {
    private ApplicationDAO dao = new ApplicationDAOImpl();
    private ResourceBundle bundle = Resources.getBundle("machineConfig");
    private Properties props;
    private HashMap<String, String> configs = new HashMap();

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

    private void loadConfig() {
        configs.put("currency", bundle.getString("currency"));
        configs.put("company", bundle.getString("companyName"));
        configs.put("address", bundle.getString("address"));
        configs.put("ticketName", bundle.getString("ticketName"));
        configs.put("taxes", bundle.getString("taxes"));
        configs.put("phoneNumber", bundle.getString("phoneNumber"));
        FileInputStream in = null;
        try {
            in = new FileInputStream("machineConfig.properties");
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

    }


    public void replaceValue(String key, String value) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("machineConfig.properties");
            props.setProperty(key, value);
            props.store(out, null);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
