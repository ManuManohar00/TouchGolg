package e.myhmanohar.mygoldapp.Model;

public class Products
{
    private String pname,weight,quality,image,pid,date,time,category;
    public Products()
    {

    }

    public Products(String pname, String weight, String quality, String image, String pid, String date, String time, String category) {
        this.pname = pname;
        this.weight = weight;
        this.quality = quality;
        this.image = image;
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.category = category;
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

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
