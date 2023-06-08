package dao;

import model.Client;
import connection.ConnectionFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static connection.ConnectionFactory.getConnection;
import static start.ReflectionExample.retrieveColumnTitles;
import static start.ReflectionExample.retrieveProperties;

public class ClientDAO {

    protected static final Logger LOGGER = Logger.getLogger(ClientDAO.class.getName());
    private static final String insertStatementString = "INSERT INTO client (name,address,email)"
                                                        + " VALUES (?,?,?)";// Instructiunile SQL
    private final static String findStatementString = "SELECT * FROM client WHERE id = ?";
    private static final String deleteStatementString = "DELETE FROM client WHERE id = ?";
    private static final String updateStatementString = "UPDATE client SET name = ?, address = ?, email = ? WHERE id = ?";
    private static final String printStatementString = "SELECT * FROM client";

    public static Client findById(int id) {// gaseste Clientul care are ID ul egal cu cel dat ca parametru si il returneaza
        Client toReturn = null;
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement findStatement = null;
        ResultSet rs = null;
        try {
            findStatement = dbConnection.prepareStatement(findStatementString);
            findStatement.setLong(1, id);
            rs = findStatement.executeQuery();
            rs.next();
            String name = rs.getString("name");
            String address = rs.getString("address");
            String email = rs.getString("email");
            toReturn = new Client(id, name, address, email);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ClientDAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(findStatement);
            ConnectionFactory.close(dbConnection);
        }
        return toReturn;
    }

    public static int insert(Client client) {//exemplu de inserare in baza de date
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement insertStatement = null;
        int insertedId = -1;
        try {
            insertStatement = dbConnection.prepareStatement(insertStatementString, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, client.getName());
            insertStatement.setString(2, client.getAddress());
            insertStatement.setString(3, client.getEmail());
            insertStatement.executeUpdate();
            ResultSet rs = insertStatement.getGeneratedKeys();
            if (rs.next()) {
                insertedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ClientDAO:insert " + e.getMessage());
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
            LOGGER.log(Level.WARNING, "ClientDAO:deleteById " + e.getMessage());
        } finally {
            ConnectionFactory.close(deleteStatement);
            ConnectionFactory.close(dbConnection);
        }
    }

    public static void updateById(int id, String name, String address, String email) {
        Connection dbConnection = getConnection();
        PreparedStatement updateStatement = null;
        try {
            updateStatement = dbConnection.prepareStatement(updateStatementString);
            updateStatement.setString(1, name);
            updateStatement.setString(2, address);
            updateStatement.setString(3, email);
            updateStatement.setLong(4, id);
            updateStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ClientDAO:updateById " + e.getMessage());
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
            Client Arnold = new Client();
            retrieveColumnTitles(Arnold);
            while (rs.next()) {
                Client Dorel = new Client(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
                retrieveProperties(Dorel);
            }
            System.out.println("=========================================\n");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ClientDAO:printAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(printStatement);
            ConnectionFactory.close(dbConnection);
        }
    }

    public static ArrayList<Client> listAll() {
        ArrayList<Client> clnt = new ArrayList<Client>();
        Connection dbConnection = getConnection();
        PreparedStatement printStatement = null;
        ResultSet rs = null;
        try {
            printStatement = dbConnection.prepareStatement(printStatementString);
            rs = printStatement.executeQuery();
            Client Arnold = new Client();
            retrieveColumnTitles(Arnold);
            while (rs.next()) {
                Client Dorel = new Client(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
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

    public static void main(String[] args) {// debug purposes
        Client clark = new Client("Clark", "home3", "Clark@gmail.com");
        insert(clark);
//        ClientDAO dmp = new ClientDAO();
        printAll();
    }
}
