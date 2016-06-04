package mescomptes.ovh.stock.data;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by mythrys on 05-06-16.
 */
public class Product {
    private String name;
    private double quantity;

    public Product(HashMap<String, Object> productData) {
        this.name = (String) productData.get("name");
        this.quantity = (Double) productData.get("quantity");
    }

    @Override
    public String toString() {
        return this.name + " (" + this.quantity + ")";
    }

    public double getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public static ArrayList<Product> parseProductData(ArrayList<HashMap> productDataList) {
        ArrayList<Product> productList = new ArrayList<Product>();
        for(HashMap<String, Object> productData : productDataList) {
            productList.add(new Product(productData));
        }
        return productList;
    }
}
