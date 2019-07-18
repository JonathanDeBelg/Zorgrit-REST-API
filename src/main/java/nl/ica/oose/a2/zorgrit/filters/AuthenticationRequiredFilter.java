package nl.ica.oose.a2.zorgrit.filters;

import nl.ica.oose.a2.zorgrit.controller.UserController;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.service.IOAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

public class AuthenticationRequiredFilter implements ContainerRequestFilter {

    private IOAuthService service;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public AuthenticationRequiredFilter(IOAuthService service) {
        this.service = service;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String token = containerRequestContext.getHeaderString("Authorization");


        int userId = Integer.MIN_VALUE;
        try{
            userId = Integer.parseInt(containerRequestContext.getHeaderString("User-Id"));
        } catch (NumberFormatException e) {
            throw new NotAuthorizedException("User-Id is missing");
        }

        if(token == null) {
            throw new NotAuthorizedException("Token missing");
        }

        try {
            if(!service.validateToken(token, userId)) {
                throw new NotAuthorizedException("Token invalid or expired");
            }
        } catch (DataUnreachableException e) {
            LOGGER.error("AuthenticationRequiredFilter filter: ", e);
        }
    }
}
