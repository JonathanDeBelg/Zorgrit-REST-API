package nl.ica.oose.a2.zorgrit.controller;

import nl.ica.oose.a2.zorgrit.service.ridematcher.IRideMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/v0.2/matching/")
public class MatchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchController.class);

    @Inject
    IRideMatcher rideMatcher;

    @POST
    @Path("update")
    public Response updateMatching() {
        rideMatcher.updateMatching();
        return Response.status(Response.Status.OK).build();
    }
}
