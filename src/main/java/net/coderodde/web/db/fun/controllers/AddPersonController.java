package net.coderodde.web.db.fun.controllers;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static net.coderodde.web.db.fun.controllers.DBUtils.close;

/**
 * This controller is responsible for creating new persons.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Aug 8, 2017)
 */
@WebServlet(name = "AddPersonController", urlPatterns = {"/add_person"})
public class AddPersonController extends HttpServlet {

    /**
     * The SQL command for inserting a person.
     */
    private static final String INSERT_PERSON_SQL = 
            "INSERT INTO funny_persons (first_name, last_name, email) VALUES " +
            "(?, ?, ?);";
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  the servlet request.
     * @param response the servlet response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("Please use the POST method!");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  the servlet request.
     * @param response the servlet response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            String firstName = request.getParameter("first_name");
            String lastName = request.getParameter("last_name");
            String email = request.getParameter("email");
            
            if (firstName.isEmpty()) {
                out.println("The first name is empty.");
                return;
            }
            
            if (lastName.isEmpty()) {
                out.println("The last name is empty.");
                return;
            }
            
            if (email.isEmpty()) {
                out.println("The email is empty.");
                return;
            }
            
            MysqlDataSource mysql = DefaultDataSourceCreator.create();
            Connection connection = null;
            PreparedStatement statement = null;
            
            try {
                connection = mysql.getConnection();
                statement = connection.prepareStatement(INSERT_PERSON_SQL);
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, email);
                statement.executeUpdate();
            } catch (SQLException ex) {
                out.println("Error: " + ex.getMessage());
            } finally {
                close(null, statement, connection);
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
