package nl.ica.oose.a2.zorgrit.service.ridematcher.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExtractorTest {

    @Test
    public void testZipCodeDigitsFromAddress() {
        IExtractor extractor = new Extractor();

        int digits = extractor.zipCodeDigitsFromAddress("De Raetsingel 37, 5831 KC Boxmer");

        assertEquals(5831, digits);
    }

    @Test
    public void testZipCodeDigits() {
        IExtractor extractor = new Extractor();

        int digits = extractor.zipCodeDigits("2456RJ");

        assertEquals(2456, digits);
    }

    @Test
    public void getFirstNDigitsTree() {
        IExtractor extractor = new Extractor();

        int digits = extractor.getFirstNDigits(8543, 3);

        assertEquals(854, digits);
    }

    @Test
    public void getFirstNDigitsTwo() {
        IExtractor extractor = new Extractor();

        int digits = extractor.getFirstNDigits(8543, 2);

        assertEquals(85, digits);
    }
}
