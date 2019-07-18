package nl.ica.oose.a2.zorgrit.dto.payment;

import javax.xml.bind.annotation.XmlAttribute;

public class Link {
    private String href;
    private String type;

    public Link(String href, String type) {
        this.href = href;
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    @XmlAttribute(name = "href")
    public void setHref(String href) {
        this.href = href;
    }

    public String getType() {
        return type;
    }

    @XmlAttribute(name = "type")
    public void setType(String type) {
        this.type = type;
    }
}
