package nl.ica.oose.a2.zorgrit.controller;

import nl.ica.oose.a2.zorgrit.dto.*;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.filters.AuthenticationRequired;
import nl.ica.oose.a2.zorgrit.service.IOAuthService;
import nl.ica.oose.a2.zorgrit.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/v0.2/user/")
public class UserController {

    @Inject
    private IUserService userService;

    @Inject
    private IOAuthService oauthService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @GET
    @AuthenticationRequired
    @Path("{userId}/profile")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfile(@PathParam("userId") final int userId) {

        try {
                UserDTO userDTO = userService.getProfile(userId);
                if (userDTO instanceof ClientDTO) {
                    ClientDTO client = (ClientDTO) userDTO;
                    client.setType("Client");
                    return Response.status(Response.Status.OK).entity(client).build();
                } else if (userDTO instanceof DriverDTO) {
                    DriverDTO driverDTO = (DriverDTO) userDTO;
                    driverDTO.setType("Driver");
                    return Response.status(Response.Status.OK).entity(driverDTO).build();
                }
        } catch (DataUnreachableException e) {
            LOGGER.error("UserController getProfile: ", e);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginAction(@HeaderParam("username") final String username, @HeaderParam("password") final String password) {
        try {
            OAuthClient token = oauthService.requestNewToken(username, password);
            return Response.status(Response.Status.OK).entity(token).build();
        } catch (DataUnreachableException e) {
            LOGGER.error("UserController login: ", e);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @AuthenticationRequired
    @Path("{userId}/userpreference")
    public Response getUserPreferences(@PathParam("userId") final int userId){
        try {
            ArrayList<UserPreference> list = userService.getUserPreferenceByUserId(userId);
            return Response.status(Response.Status.OK).entity(list).build();
        }catch (Exception e) {
            LOGGER.error("UserController userpreference: ", e);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @POST
    @AuthenticationRequired
    @Path("{userId}/userpreference")
    public Response setUserPreferences(@PathParam("userId") final int userId, @HeaderParam("preferenceKey") final String key, @HeaderParam("preferenceValue") final String value){
        try{
            userService.setUserPreferenceByUserIdAndKey(userId, key, value);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e){
            LOGGER.error("UserController setuserpreference: ", e);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @POST
    @AuthenticationRequired
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{userId}/userpreferences")
    public Response setUserPreferences(@PathParam("userId") final int userId, final List<UserPreference> userPreferenceList){
        try{
            userService.setUserPreferencesByUserId(userId, userPreferenceList);
            return Response.status(Response.Status.OK).build();
        } catch(Exception e){
            LOGGER.error("UserController setuserpreferences: ", e);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
