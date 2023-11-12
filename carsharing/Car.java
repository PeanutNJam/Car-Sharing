package carsharing;

public class Car {
    private int ID;
    private String name;
    private String companyName;

    public Car(String _name) {
        this.name = _name;
    }

    public Car(String name, String companyName) {
        this.name = name;
        this.companyName = companyName;
    }

    public Car(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getCompanyName() {
        return companyName;
    }
}
