package controllers.webView;

import dao.ApplicationDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class WebViewController implements Initializable {

    private ApplicationDAO dao;

    @FXML
    WebView web;
    @FXML
    TextField addressBar;

    private String latestUrl;

    public WebViewController(ApplicationDAO dao) {
        this.dao = dao;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String homeUrl = "http://google.com";
        web.getEngine().load(homeUrl);
        refreshBar(homeUrl);
        latestUrl = homeUrl;
    }

    public void loadUrl() {
        String url = addressBar.getText();
        web.getEngine().load(url);
        refreshBar(url);
    }

    public void refreshBar(String url) {
        addressBar.setText(url);
        latestUrl = url;
    }

    public void isNewUrl() {
        String currentUrl = web.getEngine().getLocation();
        if (!currentUrl.equals(latestUrl)) {
            refreshBar(currentUrl);
        }
    }
}
