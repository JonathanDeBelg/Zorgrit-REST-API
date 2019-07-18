package nl.ica.oose.a2.zorgrit.dto.payment;

import javax.xml.bind.annotation.XmlAttribute;

public class Links {

    private Link self;
    private Link checkout;
    private Link documentation;

    public Link getSelf() {
        return self;
    }

    @XmlAttribute(name = "self")
    public void setSelf(Link self) {
        this.self = self;
    }

    public Link getCheckout() {
        return checkout;
    }

    @XmlAttribute(name = "checkout")
    public void setCheckout(Link checkout) {
        this.checkout = checkout;
    }

    public Link getDocumentation() {
        return documentation;
    }

    @XmlAttribute(name = "documentation")
    public void setDocumentation(Link documentation) {
        this.documentation = documentation;
    }
}
