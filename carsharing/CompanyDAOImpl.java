package carsharing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAOImpl implements CompanyDAO {
    boolean tableCreated;

    public CompanyDAOImpl() {
    }

    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        if (!tableCreated) createTable();
        try {
            String sql = "SELECT * FROM Company";

            try (ResultSet rs = Database.runSelect(sql)) {
                while (rs.next()) {
                    // Retrieve by column name
                    Company company = new Company(rs.getInt("id"), rs.getString("name"));
                    companies.add(company);
                }
            }
            Database.closeConnection();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());

        }
        return companies;
    }

    @Override
    public Company getCompany(int rollNo) {
        return null;
    }

    @Override
    public void createCompany(String name) {
        try {
            if (tableCreated) {
                runInsert(name);
            } else {
                createTable();
                runInsert(name);
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
            String sql = "CREATE TABLE if not exists COMPANY" +
                    "(ID IDENTITY NOT NULL PRIMARY KEY," +
                    "NAME VARCHAR(70) UNIQUE not null)";
            String alter = "ALTER TABLE company ALTER COLUMN id RESTART WITH 1";
            Database.executeUpdate(sql);
            Database.executeUpdate(alter);
            Database.closeConnection();
            tableCreated = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void runInsert(String company) throws SQLException {
        String sql2 = "INSERT INTO COMPANY " +
                "VALUES(default,'" + company + "')";

        Database.runInsert(sql2);

    }

}