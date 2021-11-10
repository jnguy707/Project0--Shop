package org.example.repository;

import org.example.db.DatabaseConnectionFactory;
import org.example.model.Customer;

import java.sql.*;

public class CustomerRepositoryJDBC implements CustomerRepository{
    @Override
    public void saveCustomer(Customer customer) {
        Connection connection = null;
        try {
            connection = DatabaseConnectionFactory.getConnection();
            // step 3 : Create SQL statements with JDBC
            String insertQ = "INSERT INTO customers(username,password) VALUES (?,?)";
            PreparedStatement ps = connection.prepareStatement(insertQ);
            ps.setString(1, customer.getUsername());
            ps.setString(2, customer.getPassword());

            // step 4 : execute JDBC-statements & process results
            int rowCount = ps.executeUpdate();
            if (rowCount == 1)
                System.out.println("Customer has been registered to database");

        } catch (SQLException e) {
            e.printStackTrace(); // print exception details in console
        } finally {
            // step-7 : close / release connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Customer findByUsername(String username) {
        Connection connection = null;
        Customer customer = null;
        try {
            connection = DatabaseConnectionFactory.getConnection();
            // step 3 : Create SQL statements with JDBC
            String selectQ = "SELECT * FROM customers WHERE username = ?";
            PreparedStatement ps = connection.prepareStatement(selectQ);
            ps.setString(1,username);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                customer = new Customer();
                customer.setCustomerID(rs.getInt("id"));
                customer.setUsername(rs.getString("username"));
                customer.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // print exception details in console
        } finally {
            // step-7 : close / release connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return customer;
    }
}
