package nl.ica.oose.a2.zorgrit.dto.payment;

import javax.xml.bind.annotation.XmlAttribute;

public class TransactionCreationResponse {

    private String id;
    private String mode;
    private String createdAt;
    private Amount amount;
    private String description;
    private String method;
    private String expiresAt;
    private Links links;

    public String getId() {
        return id;
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    @XmlAttribute(name = "mode")
    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @XmlAttribute(name = "createdAt")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Amount getAmount() {
        return amount;
    }

    @XmlAttribute(name = "amount")
    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    @XmlAttribute(name = "description")
    public void setDescription(String description) {
        this.description = description;
    }

    public String getMethod() {
        return method;
    }

    @XmlAttribute(name = "method")
    public void setMethod(String method) {
        this.method = method;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    @XmlAttribute(name = "expiresAt")
    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Links getLinks() {
        return links;
    }

    @XmlAttribute(name = "_links")
    public void setLinks(Links links) {
        this.links = links;
    }
}
