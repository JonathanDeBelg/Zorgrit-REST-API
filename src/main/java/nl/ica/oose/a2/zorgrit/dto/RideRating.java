package nl.ica.oose.a2.zorgrit.dto;

import javax.xml.bind.annotation.XmlAttribute;

public class RideRating {
    private int rideId;
    private int rating;
    private String message;
    private String timestamp;

    public int getRideId() {
        return rideId;
    }

    @XmlAttribute(name = "rideId")
    public void setRideId(int rideId) {
        this.rideId = rideId;
    }

    public int getRating() {
        return rating;
    }

    @XmlAttribute(name = "rating")
    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    @XmlAttribute(name = "message")
    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @XmlAttribute(name = "timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
