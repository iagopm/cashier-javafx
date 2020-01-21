package model;

public class TableRow {
    String barcode;
    String name;
    String units;
    String unitPrice;
    String totalPrice;


    public TableRow(String barcode, String name, String units, String unitPrice, String totalPrice) {
        this.barcode = barcode;
        this.name = name;
        this.units = units;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
