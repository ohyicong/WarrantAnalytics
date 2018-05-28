/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.NorthPointGlobal.myCore;

import com.google.gson.JsonObject;
import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

/**
 *
 * @author OHyic
 */
@Test
public class WarrantObservation {
    public static void start(WarrantSettings warrantSettings ) throws InterruptedException{
        System.out.println(warrantSettings.getWarrantName());
        JsonObject jsonSend = new JsonObject();
        //Setting up Selenium driver
        File src = new File("C:\\Users\\OHyic\\Documents\\NetBeansProjects\\NorthPointGlobal\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", src.getAbsolutePath());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-fullscreen");
        ChromeDriver driver = new ChromeDriver(options);
        //1. Find Sensitivity first
        driver.get("https://www.warrants.com.sg/tools/warrantsearch");
        //Wait for website to load
        TimeUnit.SECONDS.sleep(5);
        //if sensitivity 
        List elements = driver.findElementsByTagName("tr");
        //Select dropdown box and click submit
        if(warrantSettings.getSensitivityRowNumber()==0){
            for(int i=0;i<elements.size();i++){
                WebElement element = (WebElement) elements.get(i);
                if(element.getText().contains(warrantSettings.getWarrantCode())){
                    //Find warrant row
                    System.out.println("Matched Row:"+i);
                    warrantSettings.setSensitivityRowNumber(i);
                    String [] rowString = element.getText().split(" ");
                    for(int x=0;x<rowString.length;x++){
                        if(rowString[x].contains(warrantSettings.getSensitivity()+"")){
                            //Find warrant col
                            System.out.println("Matched Col:"+x);
                            warrantSettings.setSensitivityColNumber(x);
                            break;
                        }
                    }
                    break;
                }
            }
        }else{
            WebElement element = (WebElement) elements.get(warrantSettings.getSensitivityRowNumber());
            String [] rowString = element.getText().split(" ");
            if(!rowString[warrantSettings.getSensitivityColNumber()].contains(warrantSettings.getSensitivity()+"")){
                System.out.println("Sensitivity "+warrantSettings.getWarrantName()+" has changed");
                MyUser.allWarrantData.put(warrantSettings.getWarrantCode(), null);
            }else{
                System.out.println("Sensitivity "+warrantSettings.getWarrantName()+" still same");
            }
            warrantSettings.setSensitivity(Double.parseDouble(rowString[warrantSettings.getSensitivityColNumber()]));
        }
        //2. Find current warrant info 
        driver.get("https://www.warrants.com.sg/tools/livematrix/"+warrantSettings.getWarrantCode());
        //Wait for website to load
        TimeUnit.SECONDS.sleep(5);
        //
        elements= driver.findElementById("today_table").findElements(By.tagName("td"));
        int count=0;
        WarrantData [] warrantData = MyUser.allWarrantData.get(warrantSettings.getWarrantCode());
        //If there is no warrant data yet
        if(warrantData==null){
           System.out.println("Populating data "+warrantSettings.getWarrantName());
           warrantData=new WarrantData[100000];
           double array[]= new double[4];
           for(int i=0;i<elements.size();i++){
                WebElement element = (WebElement) elements.get(i);
                if(count>3){
                    //To prevent 0.000001 senarios
                    if(array[0]>=0.000001){
                        warrantData[(int)(array[0]*1000)]= new WarrantData(array[0],array[1],array[2],array[3],Calendar.getInstance());                   
                    }
                     count=0;
                }
                array[count]= Double.parseDouble(element.getText().trim());
                count++;               
            }   
            //Record last element
            warrantData[(int)(array[0]*1000)]= new WarrantData(array[0],array[1],array[2],array[3],Calendar.getInstance());
        }else{
            System.out.println("Updating data "+warrantSettings.getWarrantName());
            double array[]= new double[4];
            for(int i=0;i<elements.size();i++){
                WebElement element = (WebElement) elements.get(i);
                if(count>3){                    
                    warrantData[(int)(array[0]*1000)]= new WarrantData(array[0],array[1],array[2],array[3],Calendar.getInstance());                   
                    count=0;
                }
                array[count]= Double.parseDouble(element.getText().trim());
                count++;
            }
            //Record last element
            warrantData[(int)(array[0]*1000)]= new WarrantData(array[0],array[1],array[2],array[3],Calendar.getInstance());
        }
        //Allocate updated information back to hashmap
        MyUser.allWarrantData.put(warrantSettings.getWarrantCode(), warrantData);
        System.out.println("End");
        driver.close();
    }
}
