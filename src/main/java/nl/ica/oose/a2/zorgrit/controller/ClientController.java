package nl.ica.oose.a2.zorgrit.controller;

import nl.ica.oose.a2.zorgrit.dto.*;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.InvalidDateFormatException;
import nl.ica.oose.a2.zorgrit.filters.AuthenticationRequired;
import nl.ica.oose.a2.zorgrit.service.IClientService;
import nl.ica.oose.a2.zorgrit.service.IDriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Client controller
 * REST endpoint for Client data
 */
@Path("/v0.2/client/")
public class ClientController {

    @Inject
    private IClientService clientService;

    @Inject
    private IDriverService driverService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverController.class);

    @GET
    @AuthenticationRequired
    @Path("{clientId}/rides/future")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFutureRides(@PathParam("clientId") final int userId) {
        try {
            ArrayList<RideDTO> rides = clientService.getAllFutureRides(userId);
            return Response.status(Response.Status.OK).entity(rides).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("ClientController getAllFutureRides: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @AuthenticationRequired
    @Path("{clientId}/rides/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRide(@PathParam("clientId") final int userId, final RideDTO ride) {
        try {
            clientService.addRide(ride);
            return Response.status(Response.Status.OK).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("ClientController addRide: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InvalidDateFormatException e) {
            LOGGER.error("ClientController addRide: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @AuthenticationRequired
    @Path("{clientId}/rides/{rideId}/cancel")
    public Response cancelRide(@PathParam("rideId") final int rideId) {
        try {
            clientService.cancelRide(rideId);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            LOGGER.error("ClientController cancelRide: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @AuthenticationRequired
    @Path("{clientId}/rides/repeating/{repeatingRideId}/cancel")
    public Response cancelRepeatingRides(@PathParam("clientId") final int clientId, @PathParam("repeatingRideId") final int repeatingRideId) {
        try {
            clientService.cancelRepeatingRides(repeatingRideId);
            return Response.status(Response.Status.OK).build();
        } catch (InvalidDateFormatException e) {
            LOGGER.error("ClientController cancelRepeatingRides InvalidDate: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("ClientController cancelRepeatingRides DataUnreachable: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @AuthenticationRequired
    @Path("{clientId}/rides/cancelled")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getClientAllCancelledRides(@PathParam("clientId") final int clientId) {
        try {
            ArrayList<RideDTO> rides = clientService.getClientAllCancelledRides(clientId);
            return Response.status(Response.Status.OK).entity(rides).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("ClientController getClientAllCancelledRides: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @AuthenticationRequired
    @Path("{clientId}/rides/completed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientAllCompletedRides(@PathParam("clientId") final int clientId) {
        try {
            ArrayList<RideDTO> rides = clientService.getClientAllCompletedRides(clientId);
            return Response.status(Response.Status.OK).entity(rides).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("ClientController getClientAllCompletedRides: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @AuthenticationRequired
    @Path("{clientId}/careInstitutions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCareInstitutions(@PathParam("clientId") final int userId) {
        try {
            ArrayList<CareInstitutionDTO> careInstitutions = clientService.getAllCareInstitutions(userId);
            return Response.status(Response.Status.OK).entity(careInstitutions).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("ClientController getAllCareInstitions: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @AuthenticationRequired
    @Path("{clientId}/drivers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDrivers(@PathParam("clientId") final int userId) {
        try {
            return Response.status(Response.Status.OK).entity(driverService.getAllDriversForClient(userId)).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("ClientController getAllDriveres: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @AuthenticationRequired
    @Path("{clientId}/profile")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfile(@PathParam("clientId") final int clientId) {
        return getClientProfile(clientId);
    }

    @POST
    @AuthenticationRequired
    @Path("{clientId}/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProfile(@PathParam("clientId") final int userId, final ClientDTO client) {
        try {
            clientService.updateProfile(userId, client);
            return getProfile(userId);
        } catch (DataUnreachableException e) {
            LOGGER.error("ClientController updateProfile: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("careInstitution/{careInstitution}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllClientsCareInstitution(@PathParam("careInstitution") final int careInstitution) {
        List<ClientDTO> clients = clientService.getAllClientsCareInstitution(careInstitution);
        return Response.status(Response.Status.OK).entity(clients).build();
    }

    //Deze get client is voor de beheeromgeving, hiervoor is geen authenticationRequired nodig.
    @GET
    @Path("{clientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClient(@PathParam("clientId") final int userId) {
        return getClientProfile(userId);
    }

    private Response getClientProfile(int userId) {
        try {
            UserDTO client = clientService.getProfile(userId);
            return Response.status(Response.Status.OK).entity(client).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("ClientController getProfile:", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("{clientId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editClient(@PathParam("clientId") int userId, ClientDTO client) {
        clientService.editClient(userId, client);
        return getClient(userId);
    }

    @GET
    @Path("/limitations")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLimitations() {
        List<LimitationDTO> limitations = clientService.getAllLimitations();
        return Response.status(Response.Status.OK).entity(limitations).build();
    }

    @GET
    @Path("/{clientId}/utilities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientUtilities(@PathParam("clientId") int userId) {
        List<UtilityDTO> utilities = clientService.getClientUtilities(userId);
        return Response.status(Response.Status.OK).entity(utilities).build();
    }


    @GET
    @Path("/utilities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUtilities() {
        List<UtilityDTO> utilities = clientService.getAllUtilities();
        return Response.status(Response.Status.OK).entity(utilities).build();
    }

    @DELETE
    @Path("{clientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteClient(@PathParam("clientId") final int userId) {
        clientService.deleteClient(userId);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addClient(ClientDTO client) {
        clientService.createClient(client);
        return Response.status(Response.Status.OK).build();
    }
}
