package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.dto.payment.Link;
import nl.ica.oose.a2.zorgrit.dto.payment.TransactionCreationResponse;
import nl.ica.oose.a2.zorgrit.dto.payment.TransactionStatusResponse;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.persistance.ITransactionDAO;

import javax.inject.Inject;

public class PaymentService implements IPaymentService {

    @Inject
    ITransactionDAO transactionDAO;

    @Override
    public int addTransaction(TransactionCreationResponse transaction, RideDTO ride) throws DataUnreachableException {
        return transactionDAO.addTransaction(transaction, ride);
    }

    @Override
    public boolean rideIsPaid(int rideId) throws DataUnreachableException {
        return transactionDAO.rideIsPaid(rideId);
    }

    @Override
    public boolean paymentInProgress(int rideId) throws DataUnreachableException {
        return transactionDAO.paymentInProgress(rideId);
    }

    @Override
    public Link returnExistingPaymentUrl(int rideId) throws DataUnreachableException {
        return new Link(transactionDAO.getTransactionByRide(rideId).getCheckoutUrl(), "text/html");
    }

    @Override
    public void updateTransactionStatus(TransactionStatusResponse response) throws DataUnreachableException {
        transactionDAO.updateTransactionStatus(response);
    }
}
