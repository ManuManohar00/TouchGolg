package e.myhmanohar.mygoldapp.Model;

public class Cart {
    private String pid,pname,weight,quantity;
    public Cart()
    {

    }

    public Cart(String pid, String pname, String weight, String quantity) {
        this.pid = pid;
        this.pname = pname;
        this.weight = weight;
        this.quantity = quantity;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
