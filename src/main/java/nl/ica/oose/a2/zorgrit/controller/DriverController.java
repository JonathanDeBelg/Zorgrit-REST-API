package nl.ica.oose.a2.zorgrit.controller;

import nl.ica.oose.a2.zorgrit.dto.ClientDTO;
import nl.ica.oose.a2.zorgrit.dto.DriverDTO;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.dto.UserDTO;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.filters.AuthenticationRequired;
import nl.ica.oose.a2.zorgrit.service.IDriverService;
import nl.ica.oose.a2.zorgrit.service.IUserService;
import nl.ica.oose.a2.zorgrit.service.ridematcher.DataCache.RideMatchProposedDriverCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * DriverDTO controller
 * REST endpoint for DriverDTO data
 */
@Path("/v0.2/driver/")
public class DriverController {
    @Inject
    private IDriverService driverService;

    @Inject
    private IUserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverController.class);

    @POST
    @Consumes({"application/json"})
    public Response createDriver(DriverDTO driverDTO) {
        this.driverService.createDriver(driverDTO);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("careInstitution/{careInstitution}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDiversCareInstitution(@PathParam("careInstitution") final int careInstitution) {
        List<DriverDTO> drivers = driverService.getAllDriversCareInstitution(careInstitution);
        return Response.status(Response.Status.OK).entity(drivers).build();
    }

    @GET
    @Path("township/{township}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDriversTownship(@PathParam("township") final String township) {
        List<DriverDTO> drivers = driverService.getAllDriversTownship(township);
        return Response.status(Response.Status.OK).entity(drivers).build();
    }

    @GET
    @Path("{driverId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDriver(@PathParam("driverId") final int driverId) {
        try {
            UserDTO userDTO = userService.getProfile(driverId);
            if (userDTO instanceof DriverDTO) {
                DriverDTO driverDTO = (DriverDTO) userDTO;
                driverDTO.setType("Driver");
                return Response.status(Response.Status.OK).entity(driverDTO).build();
            }
        } catch (DataUnreachableException e) {
            LOGGER.error("Drivercontroller getDriver: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDrivers() {
        List<DriverDTO> drivers = driverService.getAllDrivers();
        return Response.status(Response.Status.OK).entity(drivers).build();
    }

    @GET
    @AuthenticationRequired
    @Path("{driverId}/rides/future")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFutureRides(@PathParam("driverId") final int driverId) {
        try {
            ArrayList<RideDTO> rides = driverService.getAllFutureRides(driverId);
            return Response.status(Response.Status.OK).entity(rides).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("DriverController getAllFutureRides: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @AuthenticationRequired
    @Path("{driverId}/rides/completed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDrivenRides(@PathParam("driverId") final int driverId) {
        try {
            ArrayList<RideDTO> rides = driverService.getAllCompletedRides(driverId);
            return Response.status(Response.Status.OK).entity(rides).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("DriverController getAlDrivenRides: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @AuthenticationRequired
    @Path("{driverId}/rides/cancelled")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCancelledRides(@PathParam("driverId") final int driverId) {
        try {
            ArrayList<RideDTO> rides = driverService.getAllCancelledRides(driverId);
            return Response.status(Response.Status.OK).entity(rides).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("DriverController getAllCancelledRides: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @AuthenticationRequired
    @Path("{driverId}/rides/requests")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRequestRides(@PathParam("driverId") final int driverId) {
        try {
            ArrayList<RideDTO> rides = driverService.getAllRequestRides(driverId);
            return Response.status(Response.Status.OK).entity(rides).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("DriverController getAllRequestRides: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @AuthenticationRequired
    @Path("{driverId}/rides/{rideId}/accept")
    public Response acceptRide(@PathParam("driverId") final int userId, @PathParam("rideId") final int rideId) {
        try {
            driverService.acceptRide(userId, rideId);
            return Response.status(Response.Status.OK).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("DriverController acceptRide: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @AuthenticationRequired
    @Path("{driverId}/rides/{rideId}/cancel")
    public Response cancelOneRideByDriver(@PathParam("driverId") final int driverId, @PathParam("rideId") final int rideId) {
        try {
            RideMatchProposedDriverCache.Instance().rejectRide(rideId, driverId);
            driverService.cancelRide(driverId, rideId);
            return Response.status(Response.Status.OK).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("DriverController cancelride: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @AuthenticationRequired
    @Path("{driverId}/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProfile(@PathParam("driverId") final int driverId, final DriverDTO driverDTO) {
        try {
            driverService.updateProfile(driverId, driverDTO);
            return Response.status(Response.Status.OK).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("DriverController updateProfile: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @AuthenticationRequired
    @Path("{driverId}/clients")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllClientsForDriver(@PathParam("driverId") final int driverId) {
        try {
            List<ClientDTO> clients = driverService.getAllClientsForDriver(driverId);
            return Response.status(Response.Status.OK).entity(clients).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("DriverController getAllClientsCareInstitution: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("{driverId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editDriver(@PathParam("driverId") int driverId, DriverDTO driverDTO) {
        driverService.editDriver(driverId, driverDTO);
        return getDriver(driverId);
    }

    @DELETE
    @Path("{driverId}")
    public Response deleteDriver(@PathParam("driverId") int driverId) {
        driverService.deleteDriver(driverId);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("{clientId}/preferreddrivers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPreferredDrivers(@PathParam("clientId") final int clientId) {
        List<DriverDTO> drivers = driverService.getPreferredDriversToMatch(clientId);
        return Response.status(Response.Status.OK).entity(drivers).build();
    }
}
