package org.example.model;

import java.util.Objects;

public class Offer {
    private int offerID;
    private double offerPrice;
    private int byCustomerID;

    public int getByCustomerID() {
        return byCustomerID;
    }

    public void setByCustomerID(int byCustomerID) {
        this.byCustomerID = byCustomerID;
    }

    public double getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(double offerPrice) {
        this.offerPrice = offerPrice;
    }

    public int getOfferID() {
        return offerID;
    }

    public void setOfferID(int offerID) {
        this.offerID = offerID;
    }
    public String toString(){
        return this.offerID + "  |  " + this.offerPrice + "  |  " + this.byCustomerID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return offerID == offer.offerID && Double.compare(offer.offerPrice, offerPrice) == 0 && byCustomerID == offer.byCustomerID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(offerID, offerPrice, byCustomerID);
    }
}
