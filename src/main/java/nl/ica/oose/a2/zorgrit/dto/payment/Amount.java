package nl.ica.oose.a2.zorgrit.dto.payment;

import javax.xml.bind.annotation.XmlAttribute;

public class Amount {

    private String currency;
    private String value;

    public String getCurrency() {
        return currency;
    }

    @XmlAttribute(name = "currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getValue() {
        return value;
    }

    @XmlAttribute(name = "value")
    public void setValue(String value) {
        this.value = value;
    }
}
