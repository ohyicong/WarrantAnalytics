/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.NorthPointGlobal.Servlets;

import com.NorthPointGlobal.myCore.MyUser;
import com.NorthPointGlobal.myCore.WarrantSettings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author OHyic
 */
@WebServlet(name = "GetWarrantSettingsServlet", urlPatterns = {"/GetWarrantSettingsServlet"})
public class GetWarrantSettingsServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            JsonArray jsonSend = new JsonArray();
            for(WarrantSettings warrantSettings:MyUser.allWarrantSettings.values()){
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("warrantCode", warrantSettings.getWarrantCode());
                jsonObject.addProperty("warrantName",warrantSettings.getWarrantName());
                jsonObject.addProperty("sensitivity",warrantSettings.getSensitivity());
                jsonSend.add(jsonObject);
            }
            out.println(jsonSend.toString());
        }
        System.out.println("GetWarrantSettingsServlet Activated");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
