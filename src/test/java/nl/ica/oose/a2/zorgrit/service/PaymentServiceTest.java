package nl.ica.oose.a2.zorgrit.service;

import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.dto.payment.Transaction;
import nl.ica.oose.a2.zorgrit.dto.payment.TransactionCreationResponse;
import nl.ica.oose.a2.zorgrit.dto.payment.TransactionStatusResponse;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.persistance.ITransactionDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PaymentServiceTest {

    @Mock
    private ITransactionDAO transactionDAO;

    @InjectMocks
    private PaymentService sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addTransactionCallsDAO() throws DataUnreachableException {
        TransactionCreationResponse transaction = new TransactionCreationResponse();
        RideDTO ride = new RideDTO();

        sut.addTransaction(transaction, ride);

        verify(transactionDAO).addTransaction(transaction, ride);
    }

    @Test
    public void rideIsPaidCallsDAO() throws DataUnreachableException {
        sut.rideIsPaid(1);

        verify(transactionDAO).rideIsPaid(1);
    }

    @Test
    public void paymentInProgressCallsDAO() throws DataUnreachableException {
        sut.paymentInProgress(1);

        verify(transactionDAO).paymentInProgress(1);
    }

    @Test
    public void returnExistingPaymentUrlCallsDAO() throws DataUnreachableException {
        Transaction transaction = new Transaction();
        when(transactionDAO.getTransactionByRide(1)).thenReturn(transaction);

        sut.returnExistingPaymentUrl(1);

        verify(transactionDAO).getTransactionByRide(1);
    }

    @Test
    public void updateTransactionStatusCallsDAO() throws DataUnreachableException {
        TransactionStatusResponse response = new TransactionStatusResponse();

        sut.updateTransactionStatus(response);

        verify(transactionDAO).updateTransactionStatus(response);
    }
}
