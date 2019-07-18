package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.dto.payment.PaymentStatus;
import nl.ica.oose.a2.zorgrit.dto.payment.Transaction;
import nl.ica.oose.a2.zorgrit.dto.payment.TransactionCreationResponse;
import nl.ica.oose.a2.zorgrit.dto.payment.TransactionStatusResponse;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.persistance.util.Bind;
import nl.ica.oose.a2.zorgrit.persistance.util.DatabaseConnection;
import nl.ica.oose.a2.zorgrit.persistance.util.QueryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionDAO extends DatabaseConnection implements ITransactionDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionDAO.class);

    @Override
    public int addTransaction(TransactionCreationResponse transaction, RideDTO ride) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;
        int key;

        try {
            connection = openConnection();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            Date createdAt = format.parse(transaction.getCreatedAt());
            Date expiresAt = format.parse(transaction.getExpiresAt());

            statement = connection.prepareStatement(queries.getQuery(QueryType.TRANSACTION_CREATE), Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, transaction.getId());
            statement.setInt(2, ride.getClientId());
            statement.setInt(3, ride.getDriverId());
            statement.setInt(4, ride.getId());
            statement.setDouble(5, Double.parseDouble(transaction.getAmount().getValue()));
            statement.setTimestamp(6, new Timestamp(createdAt.getTime()));
            statement.setTimestamp(7, new Timestamp(expiresAt.getTime()));
            statement.setInt(8, 0);
            statement.setString(9, transaction.getLinks().getCheckout().getHref());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();

            if (keys.next()) {
                key = keys.getInt(1);
            } else {
                throw new DataUnreachableException();
            }

            return key;
        } catch (SQLException e) {
            LOGGER.error("SQL Error", e);
        } catch (ParseException e) {
            LOGGER.error("SQL Error", e);
        }
        return -1;
    }

    @Override
    public boolean rideIsPaid(final int rideId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;
        int count = 0;

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery(QueryType.CHECK_RIDE_PAYMENT_STATUS));
            statement.setInt(1, PaymentStatus.PAID);
            statement.setInt(2, rideId);

            ResultSet keys = statement.executeQuery();

            if(keys.next()) {
                count = keys.getInt(1);
            }

            return count > 0;
        } catch (SQLException e) {
            LOGGER.error("SQL Error", e);
        }

        return false;
    }

    @Override
    public boolean paymentInProgress(final int rideId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;
        int count = 0;

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery(QueryType.CHECK_RIDE_PAYMENT_STATUS));
            statement.setInt(1, PaymentStatus.OPEN);
            statement.setInt(2, rideId);

            ResultSet keys = statement.executeQuery();

            if(keys.next()) {
                count = keys.getInt(1);
            }

            return count > 0;
        } catch (SQLException e) {
            LOGGER.error("SQL Error", e);
        }
        return false;
    }

    @Override
    public Transaction getTransactionByRide(final int rideId) throws DataUnreachableException {
        Connection connection;
        PreparedStatement statement;

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery(QueryType.GET_RIDE_PAYMENT_BY_ID));
            statement.setInt(1, rideId);
            ResultSet result = statement.executeQuery();

            if(result.next()) {
                return new Bind().transaction(result);
            }

        } catch (SQLException e) {
            LOGGER.error("SQL Error", e);
        }
        return null;
    }

    @Override
    public void updateTransactionStatus(TransactionStatusResponse response) {
        Connection connection;
        PreparedStatement statement;

        try {
            connection = openConnection();
            statement = connection.prepareStatement(queries.getQuery(QueryType.UPDATE_RIDE_PAYMENT_STATUS));
            statement.setInt(1, parseStatusStringToInt(response.getStatus()));
            statement.setString(2, response.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
        }
    }

    private int parseStatusStringToInt(String status) {
        switch (status) {
            case "open":
                return PaymentStatus.OPEN;
            case "expired":
                return PaymentStatus.EXPIRED;
            case "paid":
                return PaymentStatus.PAID;
            case "cancelled":
                return PaymentStatus.CANCELLED;
            case "failed":
                return PaymentStatus.FAILED;
        }
        return Integer.MIN_VALUE;
    }
}
