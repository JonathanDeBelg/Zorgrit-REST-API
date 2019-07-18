package nl.ica.oose.a2.zorgrit.dto;

import javax.xml.bind.annotation.XmlAttribute;

public class RideDTO {
    private int id;
    private int clientId;
    private int driverId;
    private int preferedDriver;
    private int preferedCareInstitution;
    private String pickUpDateTime;
    private String pickUpLocation;
    private String dropOffLocation;
    private int duration;
    private int distance;
    private int numberOfCompanions;
    private int numberOfLuggage;
    private boolean returnRide;
    private boolean callService;
    private String utility;
    private int repeatingRideId;
    private boolean cancelledByClient;
    private boolean executed;
    private boolean started;
    private RideRating rating;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    @XmlAttribute(name="clientId")
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getPreferedDriver() {
        return preferedDriver;
    }

    @XmlAttribute(name="preferedDriver")
    public void setPreferedDriver(int preferedDriver) {
        this.preferedDriver = preferedDriver;
    }

    public int getPreferedCareInstitution() {
        return preferedCareInstitution;
    }

    @XmlAttribute(name="preferedCareInstitution")
    public void setPreferedCareInstitution(int preferedCareInstitution) {
        this.preferedCareInstitution = preferedCareInstitution;
    }

    public String getPickUpDateTime() {
        return pickUpDateTime;
    }

    @XmlAttribute(name="pickUpDateTime")
    public void setPickUpDateTime(String pickUpDateTime) {
        this.pickUpDateTime = pickUpDateTime;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    @XmlAttribute(name="pickUpLocation")
    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public String getDropOffLocation() {
        return dropOffLocation;
    }

    @XmlAttribute(name="dropOffLocation")
    public void setDropOffLocation(String dropOffLocation) {
        this.dropOffLocation = dropOffLocation;
    }

    public int getNumberOfCompanions() {
        return numberOfCompanions;
    }

    @XmlAttribute(name="numberOfCompanions")
    public void setNumberOfCompanions(int numberOfCompanion) {
        this.numberOfCompanions = numberOfCompanion;
    }

    public int getNumberOfLuggage() {
        return numberOfLuggage;
    }

    @XmlAttribute(name="numberOfLuggage")
    public void setNumberOfLuggage(int numberOfLuggage) {
        this.numberOfLuggage = numberOfLuggage;
    }

    public boolean isReturnRide() {
        return returnRide;
    }

    @XmlAttribute(name="returnRide")
    public void setReturnRide(boolean returnRide) {
        this.returnRide = returnRide;
    }

    public boolean isCallService() {
        return callService;
    }

    @XmlAttribute(name="callService")
    public void setCallService(boolean callService) {
        this.callService = callService;
    }

    public String getUtility() {
        return utility;
    }

    @XmlAttribute(name="utility")
    public void setUtility(String utility) {
        this.utility = utility;
    }

    public int getRepeatingRideId() {
        return repeatingRideId;
    }

    @XmlAttribute(name="repeatingRideId")
    public void setRepeatingRideId(int repeatingRideId) {
        this.repeatingRideId = repeatingRideId;
    }

    public int getDriverId() {
        return driverId;
    }

    @XmlAttribute(name="driverId")
    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public boolean isCancelledByClient() {
        return cancelledByClient;
    }

    @XmlAttribute(name="cancelledByClient")
    public void setCancelledByClient(boolean cancelledByClient) {
        this.cancelledByClient = cancelledByClient;
    }

    public int getDuration() {
        return duration;
    }

    @XmlAttribute(name="duration")
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isExecuted() {
        return executed;
    }

    public int getDistance() {
        return distance;
    }

    @XmlAttribute(name="distance")
    public void setDistance(int distance) {
        this.distance = distance;
    }

    @XmlAttribute(name="executed")
    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public boolean isStarted() {
        return started;
    }

    @XmlAttribute(name = "started")
    public void setStarted(boolean started) {
        this.started = started;
    }

    public RideRating getRating() {
        return rating;
    }

    @XmlAttribute(name="rating")
    public void setRating(RideRating rating) {
        this.rating = rating;
    }
}
