package nl.ica.oose.a2.zorgrit.dto.payment;

public class PaymentStatus {

    private PaymentStatus() {
    }

    public static int PAID = 1;
    public static int OPEN = 0;
    public static int CANCELLED = -1;
    public static int EXPIRED = -2;
    public static int FAILED = -3;
}
