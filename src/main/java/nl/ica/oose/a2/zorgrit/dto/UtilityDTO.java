package nl.ica.oose.a2.zorgrit.dto;

public class UtilityDTO {
    private String name;
    //Priority (if you can fit a mobilityscooter (3), than you can also fit a walker (1)
    private int rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
