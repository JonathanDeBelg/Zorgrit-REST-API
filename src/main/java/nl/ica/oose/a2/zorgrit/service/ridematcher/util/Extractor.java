package nl.ica.oose.a2.zorgrit.service.ridematcher.util;

public class Extractor implements IExtractor {

    public int zipCodeDigitsFromAddress(final String address) {
        String[] addressParts = address.split(",");
        String[] addressParts2 = addressParts[1].split(" ");
        String zipCodeDigits = addressParts2[1];
        return Integer.parseInt(zipCodeDigits);
    }

    public int zipCodeDigits(final String zipCode) {
        String zipCodeDigits = zipCode.substring(0, Math.min(zipCode.length(), 4));
        return Integer.parseInt(zipCodeDigits);
    }

    public int getFirstNDigits(final int number, final int NDigits) {
        return Integer.parseInt(("" + number).substring(0, NDigits));
    }
}
