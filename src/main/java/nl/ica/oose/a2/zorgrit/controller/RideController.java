package nl.ica.oose.a2.zorgrit.controller;

import nl.ica.oose.a2.zorgrit.dto.*;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.exceptions.InvalidDateFormatException;
import nl.ica.oose.a2.zorgrit.filters.AuthenticationRequired;
import nl.ica.oose.a2.zorgrit.service.IRideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/v0.2/ride/")
public class RideController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RideController.class);
    @Inject
    private IRideService rideService;

    @GET
    @Path("/notmatched/careInstitution/{careInstitutionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllNotMatchedRides(@PathParam("careInstitutionId") final int careInstitutionId) {
        RidesInformationDTO ridesInformationDTO = rideService.getAllNotMatchedRides(careInstitutionId);
        return Response.status(Response.Status.OK).entity(ridesInformationDTO).build();
    }

    @GET
    @Path("/matched/careInstitution/{careInstitutionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMatchedRides(@PathParam("careInstitutionId") final int careInstitutionId) {
        RidesInformationDTO ridesInformationDTO = rideService.getAllMatchedRides(careInstitutionId);
        return Response.status(Response.Status.OK).entity(ridesInformationDTO).build();
    }

    @GET
    @Path("/info/careInstitution/{careInstitutionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRidesInfo(@PathParam("careInstitutionId") final int careInstitutionId) {
        RidesInformationDTO ridesInformationDTO = rideService.getAllRidesInfo(careInstitutionId);
        return Response.status(Response.Status.OK).entity(ridesInformationDTO).build();
    }

    @GET
    @Path("notmatched/{rideId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRideToMatch(@PathParam("rideId") final int rideId) {
        RideInformationDTO ride = rideService.getRideToMatch(rideId);
        return Response.status(Response.Status.OK).entity(ride).build();
    }

    @PUT
    @Path("notmatched/{rideId}")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response matchRideToDriver(@PathParam("rideId") final int rideId, final int driverId) {
        rideService.matchRideToDriver(rideId, driverId);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("{rideId}")
    @AuthenticationRequired
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRide(@PathParam("rideId") final int rideId) {
        try {
            RideDTO ride = rideService.getRide(rideId);
            return Response.status(Response.Status.OK).entity(ride).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("RideController getRide: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("{clientId}/current")
    @AuthenticationRequired
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentRide(@PathParam("clientId") final int clientId) {
        try {
            RideDTO ride = rideService.getCurrentRide(clientId);
            return Response.status(Response.Status.OK).entity(ride).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("RideController getCurrentRide: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("{rideId}/start")
    @AuthenticationRequired
    public Response postStartRide(@PathParam("rideId") final int rideId) {
        try {
            rideService.startRide(rideId);
            return Response.status(Response.Status.OK).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("RideController start ride: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


    @POST
    @Path("{rideId}/update")
    @AuthenticationRequired
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRide(@PathParam("rideId") final int rideId, final RideDTO ride) {
        try {
            rideService.updateRide(ride);
            return Response.status(Response.Status.OK).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("RideController updateRide: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InvalidDateFormatException e) {
            LOGGER.error("RideController updateRide: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("{rideId}/rating")
    @AuthenticationRequired
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRideRating(@PathParam("rideId") final int rideId) {
        try {
            RideRating rating = rideService.getRideRating(rideId);
            return Response.status(Response.Status.OK).entity(rating).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("Ridecontroller getRideRating: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("{rideId}/rating")
    @AuthenticationRequired
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRideRating(@PathParam("rideId") final int rideId, final RideRating rating) {
        try {
            rideService.addRideRating(rideId, rating);
            return Response.status(Response.Status.OK).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("Ridecontroller addRideRating: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("allRatings/{driverId}")
    @AuthenticationRequired
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRatingsByDriver(@PathParam("driverId") final int driverId) {
        try {
            ArrayList<RideRating> ratings = rideService.getAllRatingsByDriver(driverId);
            return Response.status(Response.Status.OK).entity(ratings).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("Ridecontroller addRideRating: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("{rideId}/rating")
    @AuthenticationRequired
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putRideRating(@PathParam("rideId") final int rideId, final RideRating rating) {
        try {
            rideService.putRideRating(rideId, rating);
            return Response.status(Response.Status.OK).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("RideController putRideRating: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("{rideId}/rating")
    @AuthenticationRequired
    public Response deleteRideRating(@PathParam("rideId") final int rideId) {
        try {
            rideService.deleteRideRating(rideId);
            return Response.status(Response.Status.OK).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("RideController deleteRideRating: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRides() {
        RidesDTO rides = rideService.getAllRides();
        return Response.status(Response.Status.OK).entity(rides).build();
    }

    @GET
    @Path("/township/{township}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRidesTownship(@PathParam("township") String township) {
        RidesDTO rides = rideService.getAllRidesTownship(township);
        return Response.status(Response.Status.OK).entity(rides).build();
    }

    @GET
    @Path("/careInstitution/{careInstitutionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRidesCareInstitution(@PathParam("careInstitutionId") int careInstitutionId) {
        RidesDTO rides = rideService.getAllRidesCareInstitution(careInstitutionId);
        return Response.status(Response.Status.OK).entity(rides).build();

    }
}
