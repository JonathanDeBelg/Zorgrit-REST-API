package nl.ica.oose.a2.zorgrit.persistance;

import nl.ica.oose.a2.zorgrit.dto.CareInstitutionDTO;
import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableRuntimeException;
import nl.ica.oose.a2.zorgrit.persistance.util.Bind;
import nl.ica.oose.a2.zorgrit.persistance.util.DatabaseConnection;
import nl.ica.oose.a2.zorgrit.persistance.util.QueryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CareInstitutionDAO extends DatabaseConnection implements ICareInstitutionDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(CareInstitutionDAO.class);

    public ArrayList<DriverDTO> getAllDrivers(final int careInstitutionId) throws DataUnreachableException {
        PreparedStatement statementDrivers;
        Connection connection;

        ArrayList<DriverDTO> driverDTOS = new ArrayList<DriverDTO>();

        try {
            connection = openConnection();
            statementDrivers = connection.prepareStatement(queries.getQuery(QueryType.CARE_INSTITUTION_GET_ALL_DRIVERS));
            statementDrivers.setInt(1, careInstitutionId);
            ResultSet resultSet = statementDrivers.executeQuery();

            while (resultSet.next()) {
                DriverDTO driverDTO = new Bind().driver(resultSet);
                driverDTO.setVerification(resultSet.getBoolean("verification"));
                driverDTOS.add(driverDTO);
            }

            closeConnection(connection, statementDrivers);

            return driverDTOS;
        } catch (SQLException e) {
            LOGGER.error("CareInstitutionDAO getAllDrivers: ", e);
            throw new DataUnreachableException();
        }
    }

    @Override
    public CareInstitutionDTO getCareInstitution(int careInstitutionId) {
        CareInstitutionDTO careInstitution = null;
        try (
                Connection connection = openConnection();
                PreparedStatement statement = connection.prepareStatement(queries.getQuery(QueryType.CARE_INSTITUTION_GET))
        ) {
            statement.setInt(1, careInstitutionId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                careInstitution = new CareInstitutionDTO();
                careInstitution.setId(resultSet.getInt("id"));
                careInstitution.setName(resultSet.getString("name"));
            }
            closeConnection(connection, statement);

        } catch (SQLException e) {
            LOGGER.error("Database error: ", e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
        return careInstitution;
    }

    @Override
    public List<CareInstitutionDTO> getAllCareInstitutions() {
        List<CareInstitutionDTO> careInstitutionsList = new ArrayList<>();

        try (
                Connection connection = openConnection();
                PreparedStatement statement = connection.prepareStatement(queries.getQuery(QueryType.CARE_INSTITUTION_GET_ALL))
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                CareInstitutionDTO careInstitution = new CareInstitutionDTO();
                careInstitution.setId(resultSet.getInt("id"));
                careInstitution.setName(resultSet.getString("name"));
                careInstitutionsList.add(careInstitution);
            }
            closeConnection(connection, statement);
        } catch (SQLException e) {
            LOGGER.error("Database error: ", e);
            throw new DataUnreachableRuntimeException(e.toString());
        }
        return careInstitutionsList;
    }
}