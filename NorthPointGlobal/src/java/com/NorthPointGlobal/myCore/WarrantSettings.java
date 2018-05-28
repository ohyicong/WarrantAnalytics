/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.NorthPointGlobal.myCore;

/**
 *
 * @author OHyic
 */
public class WarrantSettings {
    private String warrantCode;
    private int sensitivityRowNumber,sensitivityColNumber;
    private String warrantName;

    public int getSensitivityColNumber() {
        return sensitivityColNumber;
    }

    public void setSensitivityColNumber(int sensitivityColNumber) {
        this.sensitivityColNumber = sensitivityColNumber;
    }
    private double sensitivity;
    public WarrantSettings(String warrantName,String warrantCode,double sensitivity){
        this.warrantCode=warrantCode;
        this.warrantName=warrantName;
        this.sensitivityRowNumber=0;
        this.sensitivityColNumber=0;
    }

    public int getSensitivityRowNumber() {
        return sensitivityRowNumber;
    }

    public void setSensitivityRowNumber(int sensitivityRowNumber) {
        this.sensitivityRowNumber = sensitivityRowNumber;
    }

    public double getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(double sensitivity) {
        this.sensitivity = sensitivity;
    }
    public String getWarrantName(){
        return warrantName;
    }
    public void setWarrantName(String warrantName){
        this.warrantName=warrantName;
    }
    public String getWarrantCode() {
        return warrantCode;
    }

    public void setWarrantCode(String warrantCode) {
        this.warrantCode = warrantCode;
    }
}
