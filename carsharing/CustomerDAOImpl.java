package carsharing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    boolean tableCreated;

    @Override
    public List<Customer> getAllCustomers() {
        if (!tableCreated) createTable();
        try {
            List<Customer> customers = new LinkedList<>();

            ResultSet result = Database.runSelect("SELECT * FROM CUSTOMER ORDER BY ID");

            while (result.next()) {
                customers.add(new Customer(result.getInt("ID"), result.getString("NAME"), result.getInt("RENTED_CAR_ID")));
            }
            Database.closeConnection();
            return customers;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void createTable() {
        try {
            Database.connectToDB();
            String sql = "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                    "ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY" +
                    ", NAME VARCHAR(255) NOT NULL UNIQUE" +
                    ", RENTED_CAR_ID INTEGER " +
                    ", FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)" +
                    ");";
            String alter = "ALTER TABLE CUSTOMER ALTER COLUMN id RESTART WITH 1";

            Database.executeUpdate(sql);
            Database.executeUpdate(alter);
            Database.closeConnection();
            tableCreated = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addCustomer(Customer customer) {
        try {
            Database.runInsert("INSERT INTO CUSTOMER(NAME) VALUES ('" + customer.getName() + "');");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rentCar(int CustomerID, int CarID) {
        try {
            Database.runInsert("UPDATE CUSTOMER SET RENTED_CAR_ID = " + CarID + "WHERE ID = " + CustomerID + ";");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void returnCar(int CustomerID) {
        try {
            Database.runInsert("UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE CUSTOMER.ID = " + CustomerID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> getRentedCars(int CustomerID) {
        try {
            List<Car> rented = new LinkedList<>();

            ResultSet result = Database.runSelect(
                    "SELECT CAR.NAME AS NAME, COMPANY.NAME AS CNAME FROM CUSTOMER JOIN CAR ON " +
                            "CUSTOMER.RENTED_CAR_ID = CAR.ID JOIN COMPANY ON CAR.COMPANY_ID = " +
                            "COMPANY.ID WHERE CUSTOMER.ID = " + CustomerID);


            while (result.next()) {
                rented.add(new Car(result.getString("NAME"), result.getString("CNAME")));
            }
            Database.closeConnection();
            return rented;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}