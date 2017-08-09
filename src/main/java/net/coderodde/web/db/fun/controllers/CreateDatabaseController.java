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
 * This controller creates the database if it is not yet created.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Aug 8, 2017)
 */
@WebServlet(name = "CreateDatabaseController", urlPatterns = {"/create"})
public class CreateDatabaseController extends HttpServlet {

    /**
     * Creates a new database if not already created.
     */
    private static final String CREATE_DATABASE_SQL = 
            "CREATE DATABASE IF NOT EXISTS funny_db;";
    
    /**
     * Switches to 'funny_db'.
     */
    private static final String USE_DATABASE_SQL = "USE funny_db";
    
    /**
     * Creates the table if not already created.
     */
    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS funny_persons (\n" +
                "id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,\n" +
                "first_name VARCHAR(40) NOT NULL,\n" +
                "last_name VARCHAR(40) NOT NULL,\n" +
                "email VARCHAR(50) NOT NULL,\n" +
                "created TIMESTAMP);";
    
    /**
     * If not yet created, this request creates the database and the table.
     *
     * @param request  the servlet request.
     * @param response the servlet response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            MysqlDataSource mysql = DefaultDataSourceCreator.create();
            Connection connection = null;
            PreparedStatement statement = null;
            boolean error = false;
            
            try {
                connection = mysql.getConnection();
                statement = connection.prepareStatement(CREATE_DATABASE_SQL);
                statement.execute();
                
                statement = connection.prepareStatement(USE_DATABASE_SQL);
                statement.execute();
                
                statement = connection.prepareStatement(CREATE_TABLE_SQL);
                statement.execute();
            } catch (SQLException ex) {
                error = true;
                out.println("Error: " + ex.getMessage());
            } finally {
                close(null, statement, connection);
            }
            
            if (!error) {
                out.println("Database 'funny_db' is created!");
            }
        }
    }

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
        processRequest(request, response);
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description.
     */
    @Override
    public String getServletInfo() {
        return "Creates the database and the table.";
    }
}
