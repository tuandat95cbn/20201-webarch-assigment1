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
@WebServlet(name = "LoginServlet", urlPatterns = {"/Login"})
public class LoginServlet extends HttpServlet {

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
     * Return login form first time
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
        if (username != null && !username.isEmpty()) {
            response.sendRedirect(response.encodeURL(request.getContextPath()));
        } else {
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                request.getRequestDispatcher("/Header.html")
                        .include(request, response);
                out.println("<h2>Sign in</h2>");
                request.getRequestDispatcher("/login/LoginForm.html")
                        .include(request, response);

                request.getRequestDispatcher("/Footer.html")
                        .include(request, response);
            }
        }
    }

    /**
     * Process when user login
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String msg = null;
        HttpSession session = request.getSession();
        boolean isAuthenticated = false;
        if (username.isEmpty() || password.isEmpty()) {
            msg = "Username and password can not be empty!! Please re-enter them";
        } else {
            try {
                Statement statement = conn.createStatement();
                String sql = "SELECT username, password FROM user_login WHERE username='" + username + "'";
                ResultSet results = statement.executeQuery(sql);
                if (results.next()) {
                    if (password.equals(results.getString(2))) {
                        synchronized (session) {
                            session.setAttribute("username", username);
                        }
                        isAuthenticated = true;
                    } else {
                        msg = "The Password is incorrect. Please re-enter it!!";
                    }
                } else {
                    msg = "Username doesn't exist!! Please re-enter username and password";
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                msg = ex.toString();
            }
        }
        if (isAuthenticated) {
            response.sendRedirect(response.encodeURL(request.getContextPath()));

        } else {
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                request.getRequestDispatcher("/Header.html")
                        .include(request, response);
                out.println("<h2>Sign in</h2>");
                Utils.pMessageError(out, msg);
                request.getRequestDispatcher("/login/LoginForm.html")
                        .include(request, response);

                request.getRequestDispatcher("/Footer.html")
                        .include(request, response);
            }
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
