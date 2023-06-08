package dao;

import connection.ConnectionFactory;
import model.Client;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static connection.ConnectionFactory.getConnection;
import static start.ReflectionExample.retrieveColumnTitles;
import static start.ReflectionExample.retrieveProperties;

public class ProductDAO {
    protected static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());
    private static final String insertStatementString = "INSERT INTO product (name,price,quantity)"
            + " VALUES (?,?,?)";// Instructiunile SQL
    private final static String findStatementString = "SELECT * FROM product WHERE id = ?";
    private static final String deleteStatementString = "DELETE FROM product WHERE id = ?";
    private static final String updateStatementString = "UPDATE product SET name = ?, price = ?, quantity = ? WHERE id = ?";
    private static final String printStatementString = "SELECT * FROM product";

    public static Product findById(int id){// gaseste Produsul care are ID ul egal cu cel dat ca parametru si il returneaza
        Product toReturn = null;

        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement findStatement = null;
        ResultSet rs = null;
        try {
            findStatement = dbConnection.prepareStatement(findStatementString);
            findStatement.setLong(1, id);
            rs = findStatement.executeQuery();
            rs.next();
            String name = rs.getString("name");
            int price = rs.getInt("price");
            int quantity = rs.getInt("quantity");
            toReturn = new Product(id, name, price, quantity);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,"ProductDAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(findStatement);
            ConnectionFactory.close(dbConnection);
        }
        return toReturn;
    }

    public static int insert(Product product){//exemplu de inserare in baza de date
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement insertStatement = null;
        int insertedId = -1;
        try {
            if(product.getQuantity()>=0) {

                insertStatement = dbConnection.prepareStatement(insertStatementString, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, product.getName());
            insertStatement.setInt(2, product.getPrice());
            insertStatement.setInt(3, product.getQuantity());
            insertStatement.executeUpdate();
            ResultSet rs = insertStatement.getGeneratedKeys();
            if (rs.next()) {
                insertedId = rs.getInt(1);
            }
            }else System.out.println("Please insert a POSITIVE quantity!");

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(insertStatement);
            ConnectionFactory.close(dbConnection);
        }
        return insertedId;
    }
    public static void deleteById(int id){// Sterge din baza de date un obiect care are ID ul egal cu cel dat ca parametru
        Connection dbConnection = getConnection();
        PreparedStatement deleteStatement = null;
        try {
            deleteStatement = dbConnection.prepareStatement(deleteStatementString);
            deleteStatement.setLong(1, id);
            deleteStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDAO:deleteById " + e.getMessage());
        } finally {
            ConnectionFactory.close(deleteStatement);
            ConnectionFactory.close(dbConnection);
        }
    }

    public static void updateById(int id,String name,String price,String quantity){
        Connection dbConnection = getConnection();
        PreparedStatement updateStatement = null;
        try {
            if(Integer.parseInt(quantity)>=0) {

                updateStatement = dbConnection.prepareStatement(updateStatementString);
                updateStatement.setString(1, name);
                updateStatement.setString(2, price);
                updateStatement.setLong(4, id);
                updateStatement.setString(3, quantity);
                updateStatement.executeUpdate();
            }else System.out.println("Please insert a POSITIVE quantity!");

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDAO:updateById " + e.getMessage());
        } finally {
            ConnectionFactory.close(updateStatement);
            ConnectionFactory.close(dbConnection);
        }
    }
    public static void printAll(){
        Connection dbConnection = getConnection();
        PreparedStatement printStatement = null;
        ResultSet rs = null;
        try {
            printStatement = dbConnection.prepareStatement(printStatementString);
            rs = printStatement.executeQuery();
            Product Arnold = new Product();
            retrieveColumnTitles(Arnold);
            while (rs.next()) {
                Product Dorel = new Product(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4));
                retrieveProperties(Dorel);
            }
            System.out.println("=========================================\n");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDAO:printAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(printStatement);
            ConnectionFactory.close(dbConnection);
        }
    }

    public static ArrayList<Product> listAll(){
        ArrayList<Product> clnt = new ArrayList<Product>();
        Connection dbConnection = getConnection();
        PreparedStatement printStatement = null;
        ResultSet rs = null;
        try {
            printStatement = dbConnection.prepareStatement(printStatementString);
            rs = printStatement.executeQuery();
            Product Arnold = new Product();
            retrieveColumnTitles(Arnold);
            while (rs.next()) {
                Product Dorel = new Product(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4));
                retrieveProperties(Dorel);
                clnt.add(Dorel);
            }
            System.out.println("=========================================\n");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ClientDAO:printAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(printStatement);
            ConnectionFactory.close(dbConnection);
        }
        return clnt;
    }
    public static void main(String[] args){// debug purposes
        Product prod = new Product("Branza", 3, 5);
        insert(prod);
        ProductDAO dmp = new ProductDAO();
        printAll();
    }
}
