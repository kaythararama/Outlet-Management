/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icube.skilltest.outletmanagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author User
 */

public class Api extends HttpServlet {

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
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
                
        String contentType = request.getHeader("Content-Type");//contentType = "application/json";
        String authorization = request.getHeader("Authorization");//credentials = "Basic d2JhZG1pbjp3YmFkbWlu";
        InputStream inputStream = request.getInputStream( );
        
        try{
            
            String bodyData = readInputStream( inputStream, false );
            JSONParser parser = new JSONParser();
            JSONObject paramObj = (JSONObject) parser.parse( bodyData );
            
            JSONObject outObj = new CRUDProcess().process( paramObj );
            String output = outObj.toJSONString();
            
            response.setStatus( HttpServletResponse.SC_OK );
            response.flushBuffer( );
            out.print( output );
            
        }catch(Exception ex){
            response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
            ex.printStackTrace();
        }
        finally { out.close(); inputStream.close(); }
    }

    private String readInputStream( InputStream inputStream, boolean isClose ) throws IOException {
            StringBuffer sb = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8" ));
            String read;
            while((read = br.readLine()) != null) {
                sb.append(read + System.lineSeparator());
            }
            if( isClose ){
                br.close();
            }
            return sb.toString();
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
