package e.myhmanohar.mygoldapp.Model;

public class AdminOrders {
    String name, address, time, date, phnoe,city,state;
    public AdminOrders()
    {

    }

    public AdminOrders(String name, String address, String time, String date, String phnoe, String city, String state) {
        this.name = name;
        this.address = address;
        this.time = time;
        this.date = date;
        this.phnoe = phnoe;
        this.city = city;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhnoe() {
        return phnoe;
    }

    public void setPhnoe(String phnoe) {
        this.phnoe = phnoe;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
