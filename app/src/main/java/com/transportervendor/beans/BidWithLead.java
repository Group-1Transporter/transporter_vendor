package com.transportervendor.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BidWithLead {
    private Bid bid;

    private Leads leads;

    public BidWithLead() {
    }

    public BidWithLead(Bid bid, Leads leads) {
        this.bid = bid;
        this.leads = leads;
    }

    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }

    public Leads getLead() {
        return leads;
    }

    public void setLead(Leads leads) {
        this.leads = leads;
    }
}
