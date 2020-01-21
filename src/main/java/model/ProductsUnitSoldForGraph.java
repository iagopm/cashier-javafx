package model;

public class ProductsUnitSoldForGraph {
    private Long barcode;
    public Integer unitsSold;

    public ProductsUnitSoldForGraph(Long barcode, Integer unitsSold) {
        this.barcode = barcode;
        this.unitsSold = unitsSold;
    }

    public Long getBarcode() {
        return barcode;
    }

    public void setBarcode(Long barcode) {
        this.barcode = barcode;
    }

    public Integer getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(Integer unitsSold) {
        this.unitsSold = unitsSold;
    }
}
