package dao;

import bll.ProductBLL;
import bll.validators.StockValidator;
import connection.ConnectionFactory;
import model.Orders;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static connection.ConnectionFactory.getConnection;
import static start.ReflectionExample.retrieveColumnTitles;
import static start.ReflectionExample.retrieveProperties;

public class OrdersDAO {
    protected static final Logger LOGGER = Logger.getLogger(OrdersDAO.class.getName());
    private static final String insertStatementString = "INSERT INTO orders (prod_id,client_id,quantity)"
            + " VALUES (?,?,?)";// Instructiunile SQL
    private final static String findStatementString = "SELECT * FROM orders WHERE id = ?";
    private static final String deleteStatementString = "DELETE FROM orders WHERE id = ?";
    private static final String updateStatementString = "UPDATE orders SET prod_id = ?, client_id = ?, quantity = ? WHERE id = ?";
    private static final String updateQuantityString = "UPDATE product SET quantity = ? WHERE id = ?";
    private static final String printStatementString = "SELECT * FROM orders";

    public static Orders findById(int id) {// gaseste Comanda care are ID ul egal cu cel dat ca parametru si il returneaza
        Orders toReturn = null;

        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement findStatement = null;
        ResultSet rs = null;
        try {
            findStatement = dbConnection.prepareStatement(findStatementString);
            findStatement.setLong(1, id);
            rs = findStatement.executeQuery();
            rs.next();
            int prod_id = rs.getInt("prod_id");
            int client_id = rs.getInt("client_id");
            int quantity = rs.getInt("quantity");
            toReturn = new Orders(id, prod_id, client_id, quantity);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "OrdersDAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(findStatement);
            ConnectionFactory.close(dbConnection);
        }
        return toReturn;
    }

    public static int insert(Orders orders) {//exemplu de inserare in baza de date
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement insertStatement = null;
        PreparedStatement updateQuantity = null;

        int insertedId = -1;
        Product product = new Product();
//        StockValidator stockValidator = new StockValidator();
//        stockValidator.validate(orders);

        try {
            insertStatement = dbConnection.prepareStatement(insertStatementString, Statement.RETURN_GENERATED_KEYS);
            updateQuantity = dbConnection.prepareStatement(updateQuantityString, Statement.RETURN_GENERATED_KEYS);
            product = ProductDAO.findById(orders.getProd_id());
            if (product.getQuantity() - orders.getQuantity() >= 0) {

                updateQuantity.setInt(1, product.getQuantity() - orders.getQuantity());
                updateQuantity.setLong(2, orders.getProd_id());
                updateQuantity.executeUpdate();
                insertStatement.setInt(1, orders.getProd_id());
                insertStatement.setInt(2, orders.getClient_id());
                insertStatement.setInt(3, orders.getQuantity());
                insertStatement.executeUpdate();
                ResultSet rs = insertStatement.getGeneratedKeys();
                if (rs.next()) {
                    insertedId = rs.getInt(1);
                }
            }
            else{
                System.out.println("S-A INCERCAT O COMANDA ILEGALA!!");
                System.out.println("Comanda NU s-a efectuat!");
            }

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "OrdersDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(insertStatement);
            ConnectionFactory.close(dbConnection);
        }
        return insertedId;
    }

    public static void deleteById(int id) {// Sterge din baza de date un obiect care are ID ul egal cu cel dat ca parametru
        Connection dbConnection = getConnection();
        PreparedStatement deleteStatement = null;
        try {
            deleteStatement = dbConnection.prepareStatement(deleteStatementString);
            deleteStatement.setLong(1, id);
            deleteStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "OrdersDAO:deleteById " + e.getMessage());
        } finally {
            ConnectionFactory.close(deleteStatement);
            ConnectionFactory.close(dbConnection);
        }
    }

    public static void updateById(int id, String prod_id, String client_id, String quantity) {
        Connection dbConnection = getConnection();
        PreparedStatement updateStatement = null;
        try {
            if (Integer.parseInt(quantity) >= 0) {

                updateStatement = dbConnection.prepareStatement(updateStatementString);
                updateStatement.setString(1, prod_id);
                updateStatement.setString(2, client_id);
                updateStatement.setString(3, quantity);
                updateStatement.setLong(4, id);
                updateStatement.executeUpdate();
            }
            else{
                    System.out.println("S-a incercat modificarea ILEGALA a unei comenzi!!");
                    System.out.println("Modificarea Comenzii NU s-a efectuat!");
                }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "OrdersDAO:updateById " + e.getMessage());
        } finally {
            ConnectionFactory.close(updateStatement);
            ConnectionFactory.close(dbConnection);
        }
    }

    public static void printAll() {
        Connection dbConnection = getConnection();
        PreparedStatement printStatement = null;
        ResultSet rs = null;
        try {
            printStatement = dbConnection.prepareStatement(printStatementString);
            rs = printStatement.executeQuery();
            Orders Arnold = new Orders();
            retrieveColumnTitles(Arnold);
            while (rs.next()) {
                Orders Dorel = new Orders(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
                retrieveProperties(Dorel);
            }
            System.out.println("=========================================\n");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "OrdersDAO:printAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(printStatement);
            ConnectionFactory.close(dbConnection);
        }
    }

    public static ArrayList<Orders> listAll() {
        ArrayList<Orders> clnt = new ArrayList<Orders>();
        Connection dbConnection = getConnection();
        PreparedStatement printStatement = null;
        ResultSet rs = null;
        try {
            printStatement = dbConnection.prepareStatement(printStatementString);
            rs = printStatement.executeQuery();
            Orders Arnold = new Orders();
            retrieveColumnTitles(Arnold);
            while (rs.next()) {
                Orders Dorel = new Orders(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
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

    public static void main(String args[]) {// debug purposes
        Orders cmd1 = new Orders(1, 2, 50);
        insert(cmd1);
        OrdersDAO dmp = new OrdersDAO();
        printAll();
    }
}
