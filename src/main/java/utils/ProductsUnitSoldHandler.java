package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import model.Cart;
import model.Product;
import model.ProductsUnitSoldForGraph;

import java.util.ArrayList;
import java.util.List;

public class ProductsUnitSoldHandler {
    public ObservableList<PieChart.Data> handle(List<Cart> allCarts, List<Product> allProducts) {
        List<ProductsUnitSoldForGraph> list = new ArrayList<>();
        for (Product product : allProducts) {
            list.add(new ProductsUnitSoldForGraph(product.getBarcode(), 0));
        }

        for (ProductsUnitSoldForGraph productsUnitSoldForGraph : list) {
            for (Cart cart : allCarts) {
                if (cart.getProductId().longValue() == productsUnitSoldForGraph.getBarcode().longValue()) {
                    productsUnitSoldForGraph.unitsSold += cart.quantity;
                }
            }
        }
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

        for (ProductsUnitSoldForGraph productsUnitSoldForGraph : list) {
            PieChart.Data unit = new PieChart.Data(String.valueOf(productsUnitSoldForGraph.getBarcode()), productsUnitSoldForGraph.unitsSold);
            data.add(unit);
        }
        return data;
    }
}
