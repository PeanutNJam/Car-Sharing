package carsharing;

@SuppressWarnings("unused")
public class Customer {
    private int id;
    private String name;
    private int rentedCarID;

    public Customer(String name) {
        this.name = name;
    }

    public Customer(int id, String name, int rentedCarID) {
        this.id = id;
        this.name = name;
        this.rentedCarID = rentedCarID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRentedCarID() {
        return rentedCarID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRentedCarID(int rentedCarID) {
        this.rentedCarID = rentedCarID;
    }
}