package nl.ica.oose.a2.zorgrit.dto;

import java.util.List;

public class DriverDTO extends UserDTO {

    private CareInstitutionDTO careInstitution;
    private boolean verification;
    private UtilityDTO utility;
    private List<ClientDTO> preferredClients;

    private int numberOfPassengers;
    private String numberPlate;
    private float kmDrivenThisMonth;
    private float kmDrivenThisYear;
    private float totalEarnedThisMonth;
    private float totalEarnedThisYear;
    private int peopleHelpedThisMonth;

    public CareInstitutionDTO getCareInstitution() {
        return careInstitution;
    }

    public void setCareInstitution(CareInstitutionDTO careInstitution) {
        this.careInstitution = careInstitution;
    }

    public boolean isVerified() {
        return verification;
    }

    public void setVerification(boolean verification) {
        this.verification = verification;
    }

    public UtilityDTO getUtility() {
        return utility;
    }

    public void setUtility(UtilityDTO utility) {
        this.utility = utility;
    }

    public List<ClientDTO> getPreferredClients() {
        return preferredClients;
    }

    public void setPreferredClients(List<ClientDTO> preferredClients) {
        this.preferredClients = preferredClients;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public float getKmDrivenThisMonth() {
        return kmDrivenThisMonth;
    }

    public void setKmDrivenThisMonth(float kmDrivenThisMonth) {
        this.kmDrivenThisMonth = kmDrivenThisMonth;
    }

    public float getKmDrivenThisYear() {
        return kmDrivenThisYear;
    }

    public float getTotalEarnedThisMonth() {
        return totalEarnedThisMonth;
    }

    public void setTotalEarnedThisMonth(float totalEarnedThisMonth) {
        this.totalEarnedThisMonth = totalEarnedThisMonth;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public void setKmDrivenThisYear(float kmDrivenThisYear) {
        this.kmDrivenThisYear = kmDrivenThisYear;
    }

    public float getTotalEarnedThisYear() {
        return totalEarnedThisYear;
    }

    public void setTotalEarnedThisYear(float totalEarnedThisYear) {
        this.totalEarnedThisYear = totalEarnedThisYear;
    }

    public int getPeopleHelpedThisMonth() {
        return peopleHelpedThisMonth;
    }

    public void setPeopleHelpedThisMonth(int peopleHelpedThisMonth) {
        this.peopleHelpedThisMonth = peopleHelpedThisMonth;
    }
}
