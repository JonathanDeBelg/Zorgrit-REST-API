package nl.ica.oose.a2.zorgrit.controller;

import nl.ica.oose.a2.zorgrit.dto.CareInstitutionDTO;
import nl.ica.oose.a2.zorgrit.service.ICareInstitutionService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/v0.2/careinstitutions/")
public class CareInstitutionController {
    @Inject
    private ICareInstitutionService careInstitutionService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCareInstitutions() {
        List<CareInstitutionDTO> careInstitutions = careInstitutionService.getAllCareInstitutions();
        return Response.status(Response.Status.OK).entity(careInstitutions).build();
    }

    @GET
    @Path("{careinstitutionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCareInstitution(@PathParam("careinstitutionId") final int careInstitutionId) {
        CareInstitutionDTO careInstitution = careInstitutionService.getCareInstitution(careInstitutionId);
        return Response.status(Response.Status.OK).entity(careInstitution).build();
    }
}
