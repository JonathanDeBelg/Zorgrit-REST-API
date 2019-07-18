package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.dto.payment.Link;
import nl.ica.oose.a2.zorgrit.dto.payment.TransactionCreationResponse;
import nl.ica.oose.a2.zorgrit.dto.payment.TransactionStatusResponse;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;

public interface IPaymentService {

    int addTransaction(final TransactionCreationResponse transaction, final RideDTO ride) throws DataUnreachableException;

    boolean rideIsPaid(int rideId) throws DataUnreachableException;

    boolean paymentInProgress(int rideId) throws DataUnreachableException;

    Link returnExistingPaymentUrl(int rideId) throws DataUnreachableException;

    void updateTransactionStatus(TransactionStatusResponse response) throws DataUnreachableException;
}
