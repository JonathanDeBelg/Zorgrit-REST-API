package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.dto.payment.Transaction;
import nl.ica.oose.a2.zorgrit.dto.payment.TransactionCreationResponse;
import nl.ica.oose.a2.zorgrit.dto.payment.TransactionStatusResponse;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;

public interface ITransactionDAO {

    int addTransaction(final TransactionCreationResponse transaction, final RideDTO ride) throws DataUnreachableException;

    boolean rideIsPaid(int rideId) throws DataUnreachableException;

    boolean paymentInProgress(int rideId) throws DataUnreachableException;

    Transaction getTransactionByRide(int rideId) throws DataUnreachableException;

    void updateTransactionStatus(TransactionStatusResponse response);
}
