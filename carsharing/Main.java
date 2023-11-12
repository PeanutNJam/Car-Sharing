package carsharing;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // write your code here
        Menu menu = new Menu();
        menu.mainMenu();

    }
}