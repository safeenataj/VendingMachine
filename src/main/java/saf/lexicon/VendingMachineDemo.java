package saf.lexicon;

import java.util.Arrays;

    interface VendingMachine {
        void addCurrency(int amount);
        int getBalance();
        Product request(int id);
        int endSession();
        String getDescription(int id);
        String[] getProducts();
    }

    class VendingMachineImpl implements VendingMachine {
        private Product[] products;
        private int depositPool;

        public VendingMachineImpl(Product[] products) {
            this.products = products;
            this.depositPool = 0;
        }

        @Override
        public void addCurrency(int amount) {
            if (Arrays.asList(1, 2, 5, 10, 20, 50, 100, 200, 500, 1000).contains(amount)) {
                depositPool += amount;
            } else {
                System.out.println("Invalid amount. Please enter a valid amount.");
            }
        }

        @Override
        public int getBalance() {
            return depositPool;
        }

        @Override
        public Product request(int id) {
            Product requestedProduct = null;
            for (Product product : products) {
                if (product.getId() == id) {
                    requestedProduct = product;
                    break;
                }
            }

            if (requestedProduct != null && requestedProduct.getPrice() <= depositPool) {
                depositPool -= requestedProduct.getPrice();
                return requestedProduct;
            } else {
                System.out.println("Insufficient funds or product not found.");
                return null;
            }
        }

        @Override
        public int endSession() {
            int remainingBalance = depositPool;
            depositPool = 0;
            return remainingBalance;
        }

        @Override
        public String getDescription(int id) {
            for (Product product : products) {
                if (product.getId() == id) {
                    return product.examine();
                }
            }
            return "Product not found.";
        }

        @Override
        public String[] getProducts() {
            String[] productInfo = new String[products.length];
            for (int i = 0; i < products.length; i++) {
                productInfo[i] = products[i].getId() + " - " + products[i].getProductName() + " - SEK " + products[i].getPrice();
            }
            return productInfo;
        }
    }

    abstract class Product {
        private int id;
        private double price;
        public String productName;

        public Product(int id, double price, String productName) {
            this.id = id;
            this.price = price;
            this.productName = productName;
        }

        public int getId() {
            return id;
        }

        public double getPrice() {
            return price;
        }

        public String getProductName() {
            return productName;
        }

        public abstract String examine();

        public abstract String use();
    }

    class Candy extends Product {
        private String flavor;

        public Candy(int id, double price, String productName, String flavor) {
            super(id, price, productName);
            this.flavor = flavor;
        }

        @Override
        public String examine() {
            return "A delicious " + productName + " with " + flavor + " flavor.";
        }

        @Override
        public String use() {
            return "You enjoyed the sweet taste of the " + productName + ".";
        }
    }

    class Drink extends Product {
        private String type;

        public Drink(int id, double price, String productName, String type) {
            super(id, price, productName);
            this.type = type;
        }

        @Override
        public String examine() {
            return "A refreshing " + productName + " of " + type + " type.";
        }

        @Override
        public String use() {
            return "You quenched your thirst with the " + productName + ".";
        }
    }

    public class VendingMachineDemo {
        public static void main(String[] args) {
            Product[] products = {
                    new Candy(1, 5.00, "Chocolate Bar", "Dark Chocolate"),
                    new Drink(2, 11.50, "Soda", "Cola"),
                    new Candy(3, 1.00, "Gummy Bears", "Fruity"),
                    new Drink(4, 12.00, "Juice", "Orange")
            };

            VendingMachine vendingMachine = new VendingMachineImpl(products);

            System.out.println("Welcome to the Vending Machine!");
            System.out.println("Available Products:");
            for (String productInfo : vendingMachine.getProducts()) {
                System.out.println(productInfo);
            }

            // Add currency to the vending machine
            vendingMachine.addCurrency(100);
            System.out.println("Current balance: SEK " + vendingMachine.getBalance());

            // Request a product
            Product selectedProduct = vendingMachine.request(1);
            if (selectedProduct != null) {
                System.out.println("You selected: " + selectedProduct.examine());
                System.out.println("Remaining balance: SEK " + vendingMachine.getBalance());
                System.out.println(selectedProduct.use());
            }

            // End session and get the remaining balance
            int remainingBalance = vendingMachine.endSession();
            System.out.println("Session ended. Your remaining balance is: SEK " + remainingBalance);
        }
    }

