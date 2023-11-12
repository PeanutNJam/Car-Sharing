package carsharing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class CarDAOImpl implements CarDAO {
    boolean tableCreated;

    public CarDAOImpl() {
    }

    public List<Car> getCars(String query) {
        List<Car> cars = new LinkedList<>();
        if (!tableCreated) createTable();
        try {
            ResultSet result = Database.runSelect(query);

            while (result.next()) {
                cars.add(new Car(result.getInt("ID"), result.getString("NAME")));
            }
            Database.closeConnection();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println(cars);
        return cars;

    }

    @Override
    public List<Car> getAllCars(int owner) {
        String query2 = "SELECT * FROM CAR WHERE COMPANY_ID = " + owner + " ORDER BY ID";
        return getCars(query2);
    }

    public List<Car> getAvailableCars() {
        String query = "SELECT car.id, car.name, car.company_id \n" +
                "                    FROM car LEFT JOIN customer \n" +
                "                    ON car.id = customer.rented_car_id \n" +
                "                    WHERE customer.name IS NULL;";
        return getCars(query);
    }

    public List<Car> getAllNotRentedCars(int owner) {
        String sql = "SELECT CAR.* FROM CAR WHERE CAR.COMPANY_ID = " + owner + "AND CAR.ID NOT IN " +
                " (SELECT CUSTOMER.RENTED_CAR_ID FROM CUSTOMER WHERE CUSTOMER.RENTED_CAR_ID IS NOT NULL) ORDER BY CAR.ID";

        return getCars(sql);
    }

    @Override
    public Car getCar(int rollNo) {
        return null;
    }

    @Override
    public void createCar(String name, int companyId) {
        try {
            if (tableCreated) {
                runInsert(name, companyId);
            } else {
                createTable();
                runInsert(name, companyId);
                tableCreated = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public void createTable() {
        try {
            Database.connectToDB();
            String sql = "CREATE TABLE if not exists CAR" +
                    "(ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "NAME VARCHAR(255) UNIQUE not null," +
                    "COMPANY_ID INTEGER NOT NULL," +
                    "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID));";
            String alter = "ALTER TABLE car ALTER COLUMN id RESTART WITH 1";
            Database.executeUpdate(sql);
            Database.executeUpdate(alter);
            Database.closeConnection();
            tableCreated = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void runInsert(String car, int companyId) throws SQLException {
        String sql2 = "INSERT INTO CAR " +
                "VALUES(default,'" + car + "'," + companyId + ")";

        Database.runInsert(sql2);

    }

}