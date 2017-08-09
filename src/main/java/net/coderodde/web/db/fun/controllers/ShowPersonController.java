package net.coderodde.web.db.fun.controllers;

import com.google.gson.Gson;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.web.db.fun.model.FunnyPerson;

import static net.coderodde.web.db.fun.controllers.DBUtils.close;

/**
 * This controller is responsible for viewing persons.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Aug 8, 2017)
 */
@WebServlet(name = "ShowPersonController", urlPatterns = {"/show/*"})
public class ShowPersonController extends HttpServlet {

    private static final String GET_USER_BY_ID_SQL = 
            "SELECT * FROM funny_persons WHERE id = ?;";
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
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
            String path = request.getPathInfo();
            
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            
            String[] tokens = path.split("/");
            
            if (tokens.length == 0) {
                out.println("At least the user ID is required.");
                return;
            }
            
            String idString = tokens[0];
            int id = -1;
            
            try {
                id = Integer.parseInt(idString);
            } catch (NumberFormatException ex) {
                out.println(idString + ": not an integer.");
                return;
            }
            
            FunnyPerson person = new FunnyPerson();
            
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            
            try {
                connection = mysql.getConnection();
                statement = connection.prepareStatement(GET_USER_BY_ID_SQL);
                statement.setInt(1, id);
                resultSet = statement.executeQuery();
                
                if (!resultSet.next()) {
                    close(resultSet, statement, connection);
                    out.println("{\"status\": \"error\"}");
                    return;
                }
                
                String matchFirstName = null;
                
                if (tokens.length == 2) {
                    matchFirstName = tokens[1];
                }
                
                if (!matchFirstName.equals(resultSet.getString("first_name"))) {
                    MyFilter myFilter =
                            new MyFilter(id, resultSet.getString("first_name"));
                    
                    myFilter.doFilter(request, response, null);
                    return;
                }
                
                person.setId(resultSet.getInt("id"));
                person.setFirstName(resultSet.getString("first_name"));
                person.setLastName(resultSet.getString("last_name"));
                person.setEmail(resultSet.getString("email"));
                person.setCreated(resultSet.getDate("created"));
                
                Gson gson = new Gson();
                out.println(gson.toJson(person));
            } catch (SQLException ex) {
                throw new RuntimeException("SQLException thrown.", ex);
            } finally {
                close(resultSet, statement, connection);
            }
        }
    }
    
    private void handleInvalidRequest(HttpServletRequest request,
                                      HttpServletResponse response) {
        
    }

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
        return "Shows the user info via ID/first_name";
    }
}
