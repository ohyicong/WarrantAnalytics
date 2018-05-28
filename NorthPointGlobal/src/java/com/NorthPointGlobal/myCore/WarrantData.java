/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.NorthPointGlobal.myCore;

import java.util.Calendar;

/**
 *
 * @author OHyic
 */
public class WarrantData {
    private double  underlyingBid, underlyingAsk, warrantBid, warrantAsk;
    private Calendar time;
    public WarrantData(double underlyingBid, double warrantBid,double warrantAsk,double underlyingAsk, Calendar time  ){
        this.underlyingBid=underlyingBid;
        this.underlyingAsk=underlyingAsk;
        this.warrantAsk=warrantAsk;
        this.warrantBid =warrantBid;
        this.time=time;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public double getUnderlyingBid() {
        return underlyingBid;
    }

    public void setUnderlyingBid(double underlyingBid) {
        this.underlyingBid = underlyingBid;
    }

    public double getUnderlyingAsk() {
        return underlyingAsk;
    }

    public void setUnderlyingAsk(double underlyingAsk) {
        this.underlyingAsk = underlyingAsk;
    }

    public double getWarrantBid() {
        return warrantBid;
    }

    public void setWarrantBid(double warrantBid) {
        this.warrantBid = warrantBid;
    }

    public double getWarrantAsk() {
        return warrantAsk;
    }

    public void setWarrantAsk(double warrantAsk) {
        this.warrantAsk = warrantAsk;
    }
    
}
