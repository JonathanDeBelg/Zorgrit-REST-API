package nl.ica.oose.a2.zorgrit.service.ridematcher.util;

public interface IExtractor {

    /**
     * Get the zip code digits from a Google Maps formatted address
     *
     * @param address A Google formatted
     * @return
     */
    int zipCodeDigitsFromAddress(final String address);

    /**
     * Get the digits from a zip code
     *
     * @param zipCode The zip code to get the digits from
     * @return
     */
    int zipCodeDigits(final String zipCode);

    /**
     * Get the first N digits from a int
     *
     * @param number The int to get the first N numbers from
     * @param NDigits The N digits to get
     * @return
     */
    int getFirstNDigits(final int number, final int NDigits);
}
