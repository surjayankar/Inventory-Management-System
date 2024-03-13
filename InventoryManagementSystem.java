
import java.io.*;
import java.util.*;

class Main {
    public static void main(String[] args) {
        InventoryManagementSystem ims = new InventoryManagementSystem();

        // Seller adds products
        ims.addProduct("Laptop", 999.99, 10);
        ims.addProduct("Phone", 599.99, 20);
        ims.addProduct("Headphones", 99.99, 30);

        // Seller updates product details
        ims.updateProduct("1", "Laptop", 1099.99, 8);

        // Customer views available products
        ims.viewAvailableProducts();

        // Customer purchases a product
        ims.purchaseProduct("2", "John Doe", 2);

        // Administrator views purchase history
        ims.viewPurchaseHistory();
    }
}

class Product {
    private String id;
    private String name;
    private double price;
    private int stock;

    public Product(String id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void updateDetails(String name, double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}

class Purchase {
    private String productId;
    private String customerName;
    private Date date;
    private int quantity;

    public Purchase(String productId, String customerName, int quantity) {
        this.productId = productId;
        this.customerName = customerName;
        this.date = new Date();
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Date getDate() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }
}

class InventoryManagementSystem {
    private List<Product> products;
    private List<Purchase> purchases;

    public InventoryManagementSystem() {
        this.products = new ArrayList<>();
        this.purchases = new ArrayList<>();
        loadProductsFromFile();
        loadPurchasesFromFile();
    }

    private void loadProductsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("products.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Product product = new Product(data[0], data[1], Double.parseDouble(data[2]), Integer.parseInt(data[3]));
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPurchasesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("purchases.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Purchase purchase = new Purchase(data[0], data[1], Integer.parseInt(data[2]));
                purchases.add(purchase);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveProductsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt", false))) {
            for (Product product : products) {
                writer.write(product.getId() + "," + product.getName() + "," + product.getPrice() + "," + product.getStock());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePurchasesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("purchases.txt", true))) {
            Purchase purchase = purchases.get(purchases.size() - 1);
            writer.write(purchase.getProductId() + "," + purchase.getCustomerName() + "," + purchase.getQuantity());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addProduct(String name, double price, int stock) {
        String id = generateUniqueId();
        Product newProduct = new Product(id, name, price, stock);
        products.add(newProduct);
        saveProductsToFile();
    }

    public void updateProduct(String id, String name, double price, int stock) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                product.updateDetails(name, price, stock);
                saveProductsToFile();
                break;
            }
        }
    }

    public void viewAvailableProducts() {
        for (Product product : products) {
            if (product.getStock() > 0) {
                System.out.println("ID: " + product.getId() + ", Name: " + product.getName() + ", Price: $" + product.getPrice() + ", Stock: " + product.getStock());
            }
        }
    }

    public void purchaseProduct(String productId, String customerName, int quantity) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                if (product.getStock() >= quantity) {
                    product.updateDetails(product.getName(), product.getPrice(), product.getStock() - quantity);
                    Purchase purchase = new Purchase(productId, customerName, quantity);
                    purchases.add(purchase);
                    savePurchasesToFile();
                } else {
                    System.out.println("Insufficient stock!");
                }
                break;
            }
        }
    }

    public void viewPurchaseHistory() {
        for (Purchase purchase : purchases) {
            System.out.println("Product ID: " + purchase.getProductId() + ", Customer: " + purchase.getCustomerName() + ", Quantity: " + purchase.getQuantity() + ", Date: " + purchase.getDate());
        }
    }

    private String generateUniqueId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
