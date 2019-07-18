package nl.ica.oose.a2.zorgrit.dto;

import java.util.List;

public class ClientDTO extends UserDTO {

    private List<DriverDTO> preferredDrivers;
    private List<LimitationDTO> limitations;
    private CareInstitutionDTO careInstitution;
    private UtilityDTO utility;
    private boolean driverPreferenceForced;
    private String companion;
    public List<UserPreference> preferences;

    public List<UserPreference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<UserPreference> preferences) {
        this.preferences = preferences;
    }

    public List<DriverDTO> getPreferredDrivers() {
        return preferredDrivers;
    }

    public void setPreferredDrivers(List<DriverDTO> preferredDrivers) {
        this.preferredDrivers = preferredDrivers;
    }

    public List<LimitationDTO> getLimitations() {
        return limitations;
    }

    public void setLimitations(List<LimitationDTO> limitations) {
        this.limitations = limitations;
    }

    public CareInstitutionDTO getCareInstitution() {
        return careInstitution;
    }

    public void setCareInstitution(CareInstitutionDTO careInstitution) {
        this.careInstitution = careInstitution;
    }

    public UtilityDTO getUtility() {
        return utility;
    }

    public void setUtility(UtilityDTO utility) {
        this.utility = utility;
    }

    public boolean isDriverPreferenceForced() {
        return driverPreferenceForced;
    }

    public void setDriverPreferenceForced(boolean driverPreferenceForced) {
        this.driverPreferenceForced = driverPreferenceForced;
    }

    public String getCompanion() {
        return companion;
    }

    public void setCompanion(String companion) {
        this.companion = companion;
    }


}
