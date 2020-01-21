package utils;


import com.itextpdf.text.DocumentException;
import model.Cart;
import org.apache.commons.io.FileUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.ResourceBundle;

public class GenerateInform {

    public GenerateInform(List<ParseCartToInform.ToInform> toInformList) throws IOException, DocumentException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("machineConfig");
        String spacer="<p>###################</p>";
        File htmlTemplateFile = new File("src/main/resources/templates/template.html");
        String htmlString = FileUtils.readFileToString(htmlTemplateFile);
        String title = "<p>Company : " + resourceBundle.getString("companyName") + "</p>"
                + "<p>" + resourceBundle.getString("ticketName") + "</p>"
                + "<p> Phone number : " + resourceBundle.getString("phoneNumber") + "</p>"
                + "<p> Address : " + resourceBundle.getString("address") + "</p>";
        String body = "";
        for (ParseCartToInform.ToInform toInform : toInformList) {
            body += spacer+"<p> Ticket ID :" + toInform.id + "</p>"
                    + "<p>Date : " + toInform.date + "</p>"
                    + "<p>User :" + toInform.user + "</p>";
            for (Cart cart : toInform.cart) {
                body += "<p> Barcode : " + cart.getProductId() + "</p>"
                        + "<p> Quantity : " + cart.getQuantity() + "</p>";
            }
        }
        htmlString = htmlString.replace("$title", title);
        htmlString = htmlString.replace("$body", body);
        File newHtmlFile = new File("path/new.html");
        FileUtils.writeStringToFile(newHtmlFile, htmlString);


        String url = new File(newHtmlFile.getAbsolutePath()).toURI().toURL().toString();


        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            System.out.println(selectedFile.getAbsolutePath());
        }
        String outputFile = jfc.getSelectedFile().getAbsolutePath();
        OutputStream os = new FileOutputStream(outputFile);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(url);
        renderer.layout();
        renderer.createPDF(os);

        os.close();
    }

}
