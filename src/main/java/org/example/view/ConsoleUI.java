package org.example.view;

import org.example.exceptions.InvalidCredentialsException;
import org.example.exceptions.UserNotFoundException;
import org.example.model.Customer;
import org.example.model.Employee;
import org.example.model.Item;
import org.example.model.Offer;
import org.example.repository.*;
import org.example.service.*;

import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.util.logging.Logger;

public class ConsoleUI {
    // Log4j
    private static final Logger logger = Logger.getLogger("shop0"); // singleton

    // Repo Interfaces
    private static CustomerRepository customerRepo = new CustomerRepositoryJDBC();
    private static EmployeeRepository employeeRepo = new EmployeeRepoistoryJDBC();
    private static ItemRepository itemRepo = new ItemRepositoryJDBC();

    // Service Interfaces
    private static CustomerService customerService = new CustomerService_Impl(customerRepo);
    private static EmployeeService employeeService = new EmployeeService_Impl(employeeRepo);
    private static ItemService itemService = new ItemService_Impl(itemRepo);

    // Data Containers
    private static Employee currentEmployee = null;
    private static Customer currentCustomer = null;

    // Static Classes and Variables
    public static Scanner sc = new Scanner(System.in);
    public static boolean isLoggedIn = false;
    public static boolean quit = false;

    // MAIN
    public static void main(String[] args) {
        while(!quit){
            while (!isLoggedIn && !quit){
                viewLogin();
            }
            // Customer Interface
            if (currentCustomer != null)
                viewMenu();
            if (currentEmployee != null)
                viewEmployeeMenu();
        }
    }


    // Login Menu + Functions

    public static void viewLogin() {
        prompt("1 - Login as a  Customer");
        prompt("2 - Login as an Employee");
        prompt("3 - Register as a Customer");
        prompt("0 - Quit");

        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                loginCustomer();
                break;
            case 2:
                loginEmployee();
                break;
            case 3:
                registerCustomer();
                break;
            case 0:
                quit = true;
                break;
            default:
                prompt("Invalid Choice");
        }
    }

    private static void loginCustomer() {
        sc.nextLine();
        prompt("Enter username: ");
        String username = sc.nextLine();
        prompt("Enter password: ");
        String password = sc.nextLine();

        // Login Customer
        try {
            currentCustomer = customerService.login(username,password);
            prompt("Login Successful");
            prompt("Welcome " + currentCustomer.getUsername());
            isLoggedIn = true;
        } catch (UserNotFoundException | InvalidCredentialsException e) {
            System.out.println("Exception:" + e.getMessage());
        }
    }

    private static void loginEmployee() {
        sc.nextLine();
        prompt("Enter username: ");
        String username = sc.nextLine();
        prompt("Enter password: ");
        String password = sc.nextLine();

        // Login Employee
        try {
            currentEmployee = employeeService.login(username,password);
            prompt("Login Successful");
            prompt("Welcome" + currentEmployee.getUsername());
            isLoggedIn = true;
        } catch (UserNotFoundException | InvalidCredentialsException e) {
            System.out.println("Exception:" + e.getMessage());
        }

    }

    private static void registerCustomer(){
        sc.nextLine();

        prompt("Enter username: ");
        String username = sc.nextLine();

        prompt("Enter password: ");
        String password = sc.nextLine();

        // Customer
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(password);

        // Login Customer
        customerService.register(customer);
    }

    private static void registerEmployee(){
        sc.nextLine();

        if (currentEmployee.isManager()) {
            prompt("Enter Employee's username: ");
            String username = sc.nextLine();
            prompt("Enter Employee's password: ");
            String password = sc.nextLine();

            // Employee
            Employee employee = new Employee();
            employee.setUsername(username);
            employee.setPassword(password);

            // Login Employee
            employeeService.register(employee);
        } else {
            logger.warning("To register an employee account, current employee user has to be a manager!");
        }
    }

    public static void prompt(String p) {
        System.out.println(p);
    }



    // Post Customer Login
    public static void viewMenu() {
        // this point currentCustomer != null;
        // Login Success
        prompt("----------------------------------------------------");
        prompt("1 - View Shop Catalog");
        prompt("2 - View Owned Items");
        prompt("3 - Make an Offer (item_id required)");
        prompt("4 - View Debit owned on an item (item_id required)");
        prompt("5 - Make a Payment on an item (item_id required)");
        prompt("9 - Logout");
        prompt("0 - Logout & Quit");
        int choice = sc.nextInt();
        List<Item> itemList;
        double amount;
        switch (choice) {
            case 1 -> {
                itemList = itemService.viewAllItems();
                if (itemList.isEmpty()) {
                    prompt("There are current no items available in the shop.");
                } else {
                    prompt("Take note of item_id");
                    prompt("");
                    prompt("item_id" + "  |  " + "item_name" + "  |  " + "available");
                    itemList
                            .stream()
                            .filter(Item::isAvailable)
                            .forEach(System.out::println);

                }
            }
            case 2 -> {
                itemList = itemService.viewOwnedItems(currentCustomer);
                if (itemList.isEmpty()) {
                    prompt("No owned items found.");
                } else {
                    prompt("Take note of item_id");
                    prompt("");
                    prompt("item_id" + "  |  " + "item_name");
                    itemList
                            .stream()
                            .forEach(System.out::println);
                }
            }
            case 3 -> {
                sc.nextLine();
                Item item = new Item();
                prompt("Enter item_id: ");
                if (sc.hasNextInt()) {
                    item.setId(sc.nextInt());
                } else {
                    prompt("Invalid ID input");
                    break;
                }
                prompt("Enter offer amount: ");
                if (sc.hasNextDouble()) {
                    amount = sc.nextDouble();
                } else {
                    prompt("Invalid offer amount");
                    break;
                }
                itemService.makeOffer(item, currentCustomer, amount);
            }
            case 4 -> {
                sc.nextLine();
                Item item = new Item();
                double debitAmount;
                prompt("Enter item_id: ");
                if (sc.hasNextInt()) {
                    item.setId(sc.nextInt());
                } else {
                    prompt("Invalid ID input");
                    break;
                }
                debitAmount = itemService.viewDebit(item, currentCustomer);
                prompt("The debit owed is $" + debitAmount + " for item #" + item.getId());
            }
            case 5 -> {
                sc.nextLine();
                Item item = new Item();
                prompt("Enter item_id: ");
                if (sc.hasNextInt()) {
                    item.setId(sc.nextInt());
                } else {
                    prompt("Invalid ID input");
                    break;
                }
                prompt("Enter payment amount:");
                if (sc.hasNextDouble()) {
                    amount = sc.nextDouble();
                } else {
                    prompt("Invalid offer amount");
                    break;
                }
                itemService.makePayment(item, currentCustomer, amount);
            }
            case 9 -> {
                currentCustomer = null;
                currentEmployee = null;
                isLoggedIn = false;
            }
            case 0 -> {
                currentCustomer = null;
                currentEmployee = null;
                isLoggedIn = false;
                quit = true;
            }
            default -> prompt("Invalid Choice");
        }
    }

    // Post Employee Login
    public static void viewEmployeeMenu(){
        // this point currentEmployee != null;
        // Login Success
        prompt("----------------------------------------------------");
        prompt("1 - View Entire Shop Catalog");
        prompt("2 - View Offers on Item (item_id required)");
        prompt("3 - Finalize offer (item_id and offer_id required)");
        prompt("4 - Add Item");
        prompt("5 - Edit Item (item_id required)");
        prompt("6 - Remove Item (item_id required)");
        prompt("7 - ");
        prompt("8 - Register an Employee account");

        prompt("9 - Logout");
        prompt("0 - Logout & Quit");

        // Local Variables to be used in switch-case
        int choice = sc.nextInt();
        List<Item> itemList;
        List<Offer> offerList;
        Item item = new Item();
        Offer offer = new Offer();
        double amount;

        switch (choice) {
            case 1 -> {

                itemList = itemService.viewAllItems();
                prompt("Take note of item_id");
                prompt("");
                prompt("item_id" + "  |  " + "item_name" + "  |  " + "available");
                itemList
                        .forEach(System.out::println);
            }
            case 2 -> {
                sc.nextLine();
                prompt("Enter item's id to view offers on item: ");
                if (sc.hasNextInt()) {
                    item.setId(sc.nextInt());
                } else {
                    prompt("Invalid ID input");
                }
                offerList = itemService.viewOffers(item);
                if (!offerList.isEmpty()){
                    prompt("To finalize offer, take note of offer_id");
                    prompt("The offer(s) for item #" + item.getId());
                    prompt("offer_id" + "  |  " + "amount" + "  |  " + "customer_id");
                    offerList
                            .forEach(System.out::println);
                } else {
                    prompt("Either item #" +item.getId() +" has no offers");
                    prompt("or does not exist");
                }

            }
            case 3 -> {
                sc.nextLine();

                prompt("Enter item_id :");
                if (sc.hasNextInt()) {
                    item.setId(sc.nextInt());
                } else {
                    prompt("Invalid ID input");
                    break;
                }
                prompt("Enter offer_id :");
                if (sc.hasNextInt()) {
                    offer.setOfferID(sc.nextInt());
                } else {
                    prompt("Invalid ID input");
                    break;
                }
                itemService.finalizeOffer(item,offer);
            }
            case 4 -> {
                sc.nextLine();
                prompt("Enter item name :");
                    item.setName(sc.nextLine());
                itemService.addItem(item);
            }
            case 5 -> {
                // TODO: LEFT OFF HERE
                // EDIT FUNCTION
                sc.nextLine();
                prompt("Enter item_id of to-be editted:");
                if (sc.hasNextInt()) {
                    item.setId(sc.nextInt());
                } else {
                    prompt("Invalid ID input");
                    break;
                }
                prompt("Enter new item name:");
                item.setName(sc.nextLine());
                itemService.editItem(item);
            }
            case 6 -> {
                sc.nextLine();
                // REMOVE FUNCTION
                prompt("Enter item_id of to-be removed:");
                if (sc.hasNextInt()) {
                    item.setId(sc.nextInt());
                } else {
                    prompt("Invalid ID input");
                    break;
                }
                itemService.removeItem(item);
            }
            case 8 -> {
                registerEmployee();
            }
            case 9 -> {
                currentCustomer = null;
                currentEmployee = null;
                isLoggedIn = false;
            }
            case 0 -> {
                currentCustomer = null;
                currentEmployee = null;
                isLoggedIn = false;
                quit = true;
            }
            default -> {
                prompt("Invalid Choice");
            }
        }
    }
}

/*
 *  + SQLFactory Modularity
 *  - Log4j
 *  - weekly payment function requires some date & time tracking
 *  - lets look into JUnit and Mockito and lectures about Unit testing
 *  - transaction management
 *  - multi-threading
 */
