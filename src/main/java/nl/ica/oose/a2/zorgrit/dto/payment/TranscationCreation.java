package nl.ica.oose.a2.zorgrit.dto.payment;

import javax.xml.bind.annotation.XmlAttribute;

public class TranscationCreation {

    private String description;
    private String redirectUrl;
    private String webhookUrl;
    private Amount amount;

    public String getDescription() {
        return description;
    }

    @XmlAttribute(name = "description")
    public void setDescription(String description) {
        this.description = description;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    @XmlAttribute(name = "redirectUrl")
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    @XmlAttribute(name = "webhookUrl")
    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public Amount getAmount() {
        return amount;
    }

    @XmlAttribute(name = "amount")
    public void setAmount(Amount amount) {
        this.amount = amount;
    }
}
