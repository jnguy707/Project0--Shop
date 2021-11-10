package org.example.repository;

import org.example.model.Customer;
import org.example.model.Item;
import org.example.model.Offer;

import java.util.List;

public interface ItemRepository {
    // Shared
    List<Item> findCatalog();
    // Customer
    List<Item> findItemsByID(Customer customer);
    double getDebit(Item item, Customer customer);
    void createOffer(Item item, Customer customer, double amount);
    void createPayment(Item item, Customer customer, double amount);

    // Employee
    List<Offer> findOffers(Item item);
    void acceptOffer(Item item, Offer offer);
    void createItem(Item item);
    void updateItem(Item item);
    void deleteItem(Item item);
    //void getWeeklyIncome();




}
