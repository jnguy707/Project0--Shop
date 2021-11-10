package org.example.service;

import org.example.model.Customer;
import org.example.model.Item;
import org.example.model.Offer;


import java.util.List;

public interface ItemService {
    // Shared Functions
    List<Item> viewAllItems();
    // Customer Exclusive
    List<Item> viewOwnedItems(Customer customer);
    void makeOffer(Item item, Customer customer, double amount);
    double viewDebit(Item item, Customer customer);
    void makePayment(Item item, Customer customer, double amount);
    // Employee Exclusive
    List<Offer> viewOffers(Item item);
    void finalizeOffer(Item item, Offer offer);
    void addItem(Item item);
    void editItem(Item item);
    void removeItem(Item item);
    // void weeklyIncome();



}
