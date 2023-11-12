package carsharing;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private final CompanyDAOImpl companyDao;
    private final Scanner scanner;
    private final CarDAOImpl carDAO;
    private final CustomerDAO customerDAO;

    private List<Company> companies;
    private List<Customer> customers;


    public Menu() {
        scanner = new Scanner(System.in);
        companyDao = new CompanyDAOImpl();
        carDAO = new CarDAOImpl();
        customerDAO = new CustomerDAOImpl();
        companyDao.createTable();
        carDAO.createTable();
        customerDAO.createTable();

    }

    public void mainMenu() {
        System.out.println("""
                1. Log in as a manager
                2. Log in as a customer
                3. Create a customer
                0. Exit""");
        int choice = scanner.nextInt();
        if (choice == 1) {
            companyMenu();
        } else if (choice == 2) {
            customerMenu();
        } else if (choice == 3) {
            createCustomer();
        } else if (choice == 0) {
            System.exit(0);
        } else {
            mainMenu();
        }
    }

    private void createCustomer() {
        System.out.println();
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        if (name.isEmpty()) {
            name = scanner.nextLine();
        }
        customerDAO.addCustomer(new Customer(name));
        System.out.println("The customer was added!");
        mainMenu();
    }

    private void customerMenu() {
        customerList();
        int choice = scanner.nextInt();
        if (choice == 0) {
            mainMenu();
        } else {
            customerSubMenu(choice);
        }
    }

    private void customerSubMenu(int customerID) {
        int customerId = customers.get(customerID - 1).getId();
        System.out.println("""
                1. Rent a car
                2. Return a rented car
                3. My rented car
                0. Back""");
        int choice = scanner.nextInt();
        if (choice == 1) {
            rentCar(customerId);
        } else if (choice == 2) {
            returnCar(customerId);
        } else if (choice == 3) {
            getRentedCars(customerId);
        } else if (choice == 0) {
            mainMenu();
        }
    }

    private void getRentedCars(int customerId) {
        List<Car> cars = customerDAO.getRentedCars(customerId);
        if (cars.isEmpty()) {
            System.out.println("You didn't rent a car!");

        } else {
            for (Car car : cars) {
                System.out.println("Your rented car:\n" + car.getName() +
                        "\nCompany:\n" + car.getCompanyName());
            }
        }
        customerSubMenu(customerId);
    }

    private void returnCar(int customerId) {
        List<Car> cars = customerDAO.getRentedCars(customerId);
        if (cars.isEmpty()) {
            System.out.println("You didn't rent a car!");
            customerSubMenu(customerId);
        } else {
            customerDAO.returnCar(customerId);
            System.out.println("You've returned a rented car!");
        }
        customerSubMenu(customerId);
    }

    private void rentCar(int customerId) {
        List<Car> cars = customerDAO.getRentedCars(customerId);
        if (!cars.isEmpty()) {
            System.out.println("You've already rented a car!");
            customerSubMenu(customerId);
        } else {
            companyList();
            int choice = scanner.nextInt();
            if (choice == 0) {
                customerSubMenu(customerId);
            }

            System.out.println("Choose a Car:");

            int counter = 1;
            List<Car> freeCars = carDAO.getAllNotRentedCars(companies.get(choice - 1).getId());
            for (Car car : freeCars) {
                System.out.println(counter++ + ". " + car.getName());
            }
            System.out.println("0. Back");
            int choice1 = scanner.nextInt();
            if (choice1 == 0) {
                customerSubMenu(customerId);
            }
            System.out.printf("You rented '%s'", freeCars.get(choice1 - 1).getName());
            customerDAO.rentCar(customerId, freeCars.get(choice1 - 1).getID());
            customerSubMenu(customerId);

        }
    }

    private void customerList() {
        System.out.println();
        customers = customerDAO.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            mainMenu();
        } else {
            System.out.println("Customer List:");
            int counter = 1;
            for (Customer customer : customers) {
                System.out.println(counter++ + ". " + customer.getName());
            }
            System.out.println("0. Back");

        }
    }

    private void companyMenu() {
        System.out.println();
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
        int choice = scanner.nextInt();
        if (choice == 1) {
            companySubMenu();
        } else if (choice == 2) {
            createCompany();
        } else if (choice == 0) {
            mainMenu();
        } else {
            companyMenu();
        }
    }

    private void companySubMenu() {
        companyList();
        int choice = scanner.nextInt();
        if (choice == 0) {
            companyMenu();
        } else {
            System.out.printf("'%s' company\n", companies.get(choice - 1).getName());
            carMenu(choice);
        }

    }

    private void companyList() {
        System.out.println();
        companies = companyDao.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            companyMenu();
        } else {
            System.out.println("Choose the company:");
            int counter = 1;
            for (Company company : companies) {
                System.out.println(counter++ + ". " + company.getName());
            }
            System.out.println("0. Back");

        }
    }

    private void createCompany() {
        System.out.println();
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        if (name.isEmpty()) {
            name = scanner.nextLine();
        }
        companyDao.createCompany(name);
        System.out.println("The company was created!");
        companyMenu();
    }

    private void carMenu(int choice) {
        System.out.println();
        System.out.println("""
                1. Car list
                2. Create a car
                0. Back""");

        int choice1 = scanner.nextInt();
        if (choice1 == 1) {
            carList(choice);
        } else if (choice1 == 2) {
            createCar(choice);
        } else if (choice1 == 0) {
            companySubMenu();
        }

    }

    private void createCar(int choice) {
        int companyId = companies.get(choice - 1).getId();
        System.out.println();
        System.out.println("Enter the car name:");
        String name = scanner.nextLine();
        if (name.isEmpty()) {
            name = scanner.nextLine();
        }
        carDAO.createCar(name, companyId);
        System.out.println("The car was added!");
        carMenu(choice);

    }

    private void carList(int choice) {
        List<Car> cars = carDAO.getAllCars(choice);
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
            carMenu(choice);
        } else {
            System.out.println("Car list:");
            int counter = 1;
            for (Car car : cars) {
                System.out.println(counter++ + ". " + car.getName());
            }
            companySubMenu();
        }
    }
}