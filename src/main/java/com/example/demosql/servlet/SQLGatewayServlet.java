package com.example.demosql.servlet;

import com.example.demosql.SQL.SQLUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;

public class SQLGatewayServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String sqlStatement = request.getParameter("sqlStatement");
        String sqlResult = "";
        try {
            // load the driver
            Class.forName("com.mysql.cj.jdbc.Driver");

// get a connection
            String dbURL = "jdbc:mysql://localhost:3306/demo_web?useSSL=false&serverTimezone=UTC";
            String username = "root";
            String password = "123123@";
            Connection connection = DriverManager.getConnection(dbURL, username, password);

            // parse the SQL string
            sqlStatement = sqlStatement.trim();

            if (sqlStatement.length() >= 6) {
                String sqlType = sqlStatement.substring(0, 6);

                if (sqlType.equalsIgnoreCase("select")) {
                    // create the HTML for the result set
                    PreparedStatement ps = connection.prepareStatement(sqlStatement);
                    ResultSet resultSet = ps.executeQuery();
                    sqlResult = SQLUtil.getHtmlTable(resultSet);
                    resultSet.close();
                    ps.close();
                } else {
                    PreparedStatement ps = connection.prepareStatement(sqlStatement);
                    int i = ps.executeUpdate();
                    if (i == 0) { // a DDL statement
                        sqlResult =
                                "<p>The statement executed successfully.</p>";
                    } else { // an INSERT, UPDATE, or DELETE statement
                        sqlResult =
                                "<p>The statement executed successfully.<br>"
                                        + i + " row(s) affected.</p>";
                    }
                    ps.close();
                }
            }

            connection.close();
        } catch (ClassNotFoundException e) {
            sqlResult = "<p>Error loading the database driver: <br>"
                    + e.getMessage() + "</p>";
        } catch (SQLException e) {
            sqlResult = "<p>Error executing the SQL statement: <br>"
                    + e.getMessage() + "</p>";
        }

        HttpSession session = request.getSession();
        session.setAttribute("sqlResult", sqlResult);
        session.setAttribute("sqlStatement", sqlStatement);

        String url = "/index.jsp";
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }
}
