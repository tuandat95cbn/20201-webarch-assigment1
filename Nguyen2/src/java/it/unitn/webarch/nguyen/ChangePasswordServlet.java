/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webarch.nguyen;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author tuandat95cbn
 */
@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/ChangePassword"})
public class ChangePasswordServlet extends HttpServlet {

    private String dbURL = "jdbc:derby://localhost:1527/DELIVERY2DB";
    private String dbUser = "username";
    private String dbPassword = "pw";
    Connection conn = null;

    @Override
    public void init() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        try {
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Return Change Password site
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String username = null;
        synchronized (session) {
            username = (String) session.getAttribute("username");
        }

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            request.getRequestDispatcher("/Header.html")
                    .include(request, response);
            out.println("<h2>Change Password user " + username + "</h2>");
            out.println("<input type = \"hidden\" name = \"sessionid\" value = \""+session.getId()+"\">");
            request.getRequestDispatcher("/login/ChangePasswordForm.jsp")
                    .include(request, response);

            request.getRequestDispatcher("/Footer.html")
                    .include(request, response);
        }
    }

    /**
     * Handles change password form submit.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String currentPassword = request.getParameter("CurrentPassword");
        String newPassword = request.getParameter("NewPassword");
        boolean checkConfirmPassword = false;
        boolean isUpdated = false;
        String username = null;
        String msg = "";
        HttpSession session = request.getSession();
        synchronized (session) {
            username = (String) session.getAttribute("username");
        }
        try {// check current password is correct
            Statement statement = conn.createStatement();
            String sql = "SELECT username, password FROM user_login WHERE username='" + username + "'";
            ResultSet results = statement.executeQuery(sql);
            if (results.next()) {
                if (currentPassword.equals(results.getString(2))) {
                    synchronized (session) {
                        session.setAttribute("username", username);
                    }
                    checkConfirmPassword = true;
                } else {
                    checkConfirmPassword = false;
                    msg = "The Current Password is incorrect. Please re-enter it!!";
                }
            }
            // update new password
            if (checkConfirmPassword) {
                statement = conn.createStatement();
                sql = "UPDATE user_login SET password='" + newPassword + "' WHERE username='" + username + "'";
                int res = statement.executeUpdate(sql);

                isUpdated = true;

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            msg = ex.toString();
        }

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            request.getRequestDispatcher("/Header.html")
                    .include(request, response);
            if (isUpdated) {
                out.println("Your Password have been updated !<br>");
                out.println("Please go back Home page!" + Utils.buildATag(request.getContextPath(), "Home", response));
            } else {

                out.println("<h2>Change Password user " + username + "</h2>");
                Utils.pMessageError(out, msg);
                request.getRequestDispatcher("/login/ChangePasswordForm.jsp")
                        .include(request, response);
            }
            request.getRequestDispatcher("/Footer.html")
                    .include(request, response);
        }
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
