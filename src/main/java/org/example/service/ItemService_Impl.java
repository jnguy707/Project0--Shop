package org.example.service;

import org.example.model.Customer;
import org.example.model.Item;
import org.example.model.Offer;
import org.example.repository.CustomerRepository;
import org.example.repository.EmployeeRepository;
import org.example.repository.ItemRepository;

import java.util.List;

public class ItemService_Impl implements ItemService{
    private ItemRepository itemRepo;



    // Constructor
    public ItemService_Impl(ItemRepository itemRepo) {
        this.itemRepo = itemRepo;

    }

    // TODO: Rename the methods of ItemRepository -> Rename the methods of ItemService -> Implement ItemService_Impl
    // TODO: Implement ConsoleUI -> Run the DriverApplication -> Write Test Code;

    // Methods
    @Override
    public List<Item> viewAllItems() {
        // calls List<Item> itemRepo.findCatalog();
        return itemRepo.findCatalog();
    }

    @Override
    public List<Item> viewOwnedItems(Customer customer) {
        // calls List<Item> findItemsByID(int customerID);
        return itemRepo.findItemsByID(customer);
    }

    @Override
    public void makeOffer(Item item, Customer customer, double amount) {
        // calls createOffer(int itemID, int customerID, double amount);
        itemRepo.createOffer(item,customer,amount);
    }

    @Override
    public double viewDebit(Item item, Customer customer) {
        // calls double getDebit(int itemID, int customerID);
        return itemRepo.getDebit(item,customer);
    }

    @Override
    public void makePayment(Item item, Customer customer, double amount) {
        // calls createPayment(int itemID, int customerID,double amount)
        itemRepo.createPayment(item,customer,amount);
    }

    @Override
    public List<Offer> viewOffers(Item item) {
        // calls List<Offer> findOffers(int itemID);
        return itemRepo.findOffers(item);
    }

    @Override
    public void finalizeOffer(Item item, Offer offer) {
        // calls acceptOffer(int itemID, int offerID);
        itemRepo.acceptOffer(item,offer);
    }

    @Override
    public void addItem(Item item) {
        // calls createItem(String itemName);
        itemRepo.createItem(item);
    }

    @Override
    public void editItem(Item item) {
        itemRepo.updateItem(item);
    }

    @Override
    public void removeItem(Item item) {
        itemRepo.deleteItem(item);
    }
}
