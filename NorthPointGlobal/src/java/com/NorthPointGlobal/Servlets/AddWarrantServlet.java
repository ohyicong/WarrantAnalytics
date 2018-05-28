/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.NorthPointGlobal.Servlets;

import com.NorthPointGlobal.myCore.MyUser;
import com.NorthPointGlobal.myCore.WarrantObservation;
import com.NorthPointGlobal.myCore.WarrantSettings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author OHyic
 */
@WebServlet(name = "AddWarrantServlet", urlPatterns = {"/AddWarrantServlet"})
public class AddWarrantServlet extends HttpServlet {

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
        String input = request.getReader().readLine();
        System.out.println(input);
        try{
            
            WarrantSettings warrantSettings = new Gson().fromJson(input,WarrantSettings.class);
            MyUser.allWarrantSettings.put(warrantSettings.getWarrantCode(),warrantSettings); 
            WarrantObservation.start(MyUser.allWarrantSettings.get(warrantSettings.getWarrantCode()));
            if(MyUser.allWarrantSettings.get(warrantSettings.getWarrantCode()).getSensitivityColNumber()==0){
                //Remove from hashmap, because cannot find sensitivity val :(
                MyUser.allWarrantSettings.remove(warrantSettings.getWarrantCode());
                MyUser.allWarrantData.remove(warrantSettings.getWarrantCode());
            }
        }catch(JsonSyntaxException e){
            System.out.println(e);
        } catch (InterruptedException ex) {
            Logger.getLogger(AddWarrantServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Warrant Servlet activated");
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
