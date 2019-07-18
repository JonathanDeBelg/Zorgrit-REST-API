package nl.ica.oose.a2.zorgrit.dto;

public class UserPreference{
    public String preferenceKey;
    public String preferenceValue;

    public String getPreferenceKey() {
        return preferenceKey;
    }

    public void setPreferenceKey(String preference) {
        this.preferenceKey = preference;
    }

    public String getPreferenceValue() {
        return preferenceValue;
    }

    public void setPreferenceValue(String preferenceValue) {
        this.preferenceValue = preferenceValue;
    }
}
