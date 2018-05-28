/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.NorthPointGlobal.Servlets;

import com.NorthPointGlobal.myCore.MyUser;
import com.NorthPointGlobal.myCore.WarrantData;
import com.NorthPointGlobal.myCore.WarrantObservation;
import com.NorthPointGlobal.myCore.WarrantSettings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "GetWarrantObservationServlet", urlPatterns = {"/GetWarrantObservationServlet"})
public class GetWarrantObservationServlet extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        String input = request.getReader().readLine();
        System.out.println("Warrant Observe"+input);
        WarrantSettings warrantSettingsInput = new Gson().fromJson(input, WarrantSettings.class);
        WarrantSettings warrantSettings = MyUser.allWarrantSettings.get(warrantSettingsInput.getWarrantCode());
        try {
            WarrantObservation.start(MyUser.allWarrantSettings.get(warrantSettings.getWarrantCode()));
            
        } catch (InterruptedException ex) {
            Logger.getLogger(GetWarrantObservationServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Sending information to client");
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonSend = new JsonObject();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        WarrantData[] warrantData = MyUser.allWarrantData.get(warrantSettings.getWarrantCode());
        
        for (WarrantData temp : warrantData) {
            if (temp != null) {
                JsonObject jsonObject= new JsonObject();
                jsonObject.addProperty("underlyingBid",temp.getUnderlyingBid());
                jsonObject.addProperty("underlyingAsk",temp.getUnderlyingAsk());
                jsonObject.addProperty("warrantBid",temp.getWarrantBid());
                jsonObject.addProperty("warrantAsk",temp.getWarrantAsk());
                jsonObject.addProperty("time",simpleDateFormat.format(temp.getTime().getTime()));
                jsonArray.add(jsonObject);
            }
        }
        jsonSend.add("data", jsonArray);
        jsonSend.addProperty("sensitivity", warrantSettings.getSensitivity());
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            out.println(jsonSend.toString());
        }
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
