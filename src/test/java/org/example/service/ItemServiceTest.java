package org.example.service;

import org.example.model.Customer;
import org.example.model.Item;
import org.example.model.Offer;
import org.example.repository.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class ItemServiceTest {

    private ItemRepository itemRepo;
    private ItemService itemService;
    private Customer customer;
    private Item item;
    private Offer offer;

    @BeforeEach
    public void setup() {
        customer = Mockito.mock(Customer.class);
        item = Mockito.mock(Item.class);
        offer = Mockito.mock(Offer.class);
        itemRepo = Mockito.mock(ItemRepository.class);
        itemService = new ItemService_Impl(itemRepo);
    }

    @Test
    public void viewAllItemsTest() {
        // Expected
        Mockito.when(itemRepo.findCatalog()).thenReturn(
                List.of(
                        item,
                        item
                )
        );
        // Actual
        List<Item> items = itemService.viewAllItems();
        // Assert
        Assertions.assertEquals(2, items.size());
    }

    @Test
    public void viewOwnedItems() {
        // Expected
        Mockito.when(itemRepo.findItemsByID(customer)).thenReturn(
                List.of(
                        item,
                        item
                )
        );

        // Actual
        List<Item> items = itemService.viewOwnedItems(customer);

        // Assert
        Assertions.assertEquals(2, items.size());
    }

    @Test
    public void viewOffersTest() {
        // mock expected
        Mockito.when(itemRepo.findOffers(item)).thenReturn(
                List.of(
                        offer,
                        offer,
                        offer
                )
        );
        // actual
        List<Offer> offers = itemService.viewOffers(item);

        // assert
        Assertions.assertEquals(3, offers.size());
    }

    @Test
    public void viewDebit() {
        // mock expected
        Mockito.when(itemRepo.getDebit(item, customer)).thenReturn(100.00);
        // actual
        double amount = itemService.viewDebit(item, customer);

        // assert
        Assertions.assertEquals(100.00, amount);
    }

    @Test
    public void addItemTest() {
        itemService.addItem(item);
        itemService.addItem(item);
        Mockito.verify(itemRepo, Mockito.times(2)).createItem(item);
    }

    @Test
    public void removeItemTest() {
        itemService.removeItem(item);
        itemService.removeItem(item);
        Mockito.verify(itemRepo, Mockito.times(2)).deleteItem(item);
    }

    @Test
    public void editItemTest() {
        itemService.editItem(item);
        Mockito.verify(itemRepo, Mockito.times(1)).updateItem(item);
    }

    @Test
    public void makeOfferTest() {
        itemService.makeOffer(item, customer, offer.getOfferPrice());
        itemService.makeOffer(item, customer, offer.getOfferPrice());
        Mockito.verify(itemRepo, Mockito.times(2))
                .createOffer(item, customer, offer.getOfferPrice());
    }

    @Test
    public void finalizeOfferTest(){
        itemService.finalizeOffer(item, offer);
        Mockito.verify(itemRepo, Mockito.times(1))
                .acceptOffer(item, offer);
    }

    @Test
    public void makePaymentTest(){
        itemService.makePayment(item,customer,100.00);
        itemService.makePayment(item,customer,100.00);

        Mockito.verify(itemRepo, Mockito.times(2))
                .createPayment(item,customer,100.00);
    }
}

