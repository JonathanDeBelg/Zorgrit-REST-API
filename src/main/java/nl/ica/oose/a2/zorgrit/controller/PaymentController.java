package nl.ica.oose.a2.zorgrit.controller;

import nl.ica.oose.a2.zorgrit.dto.ClientDTO;
import nl.ica.oose.a2.zorgrit.dto.RideDTO;
import nl.ica.oose.a2.zorgrit.dto.payment.*;
import nl.ica.oose.a2.zorgrit.exceptions.DataUnreachableException;
import nl.ica.oose.a2.zorgrit.persistance.TransactionDAO;
import nl.ica.oose.a2.zorgrit.persistance.util.PaymentProperties;
import nl.ica.oose.a2.zorgrit.service.PaymentService;
import nl.ica.oose.a2.zorgrit.service.RideService;
import nl.ica.oose.a2.zorgrit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DecimalFormat;

@Path("/v0.2/payment/")
public class PaymentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionDAO.class);
    @Inject
    RideService rideService;

    @Inject
    UserService userService;

    @Inject
    PaymentService paymentService;

    @POST
    @Path("{rideId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTransaction(@PathParam("rideId") final int rideId) throws DataUnreachableException {
        RideDTO ride = rideService.getRide(rideId);
        ClientDTO client = (ClientDTO)userService.getProfile(ride.getClientId());


        //Calculate the ride price.
        double price = ((ride.getDistance() / 1000.0f) * 0.25) + 1;

        //CHECK IF RIDE IS ALREADY PAID
        if(!paymentService.rideIsPaid(rideId) && ride.isExecuted()) {
            //CHECK IF THERE ISN'T ALREADY A PAYMENT IN PROGRESS. ELSE RETURN EXISTING PAYMENT URL
            if(!paymentService.paymentInProgress(rideId)) {
                Invocation.Builder builder = getMollieApiInvocation("payments");
                DecimalFormat decimalFormat = new DecimalFormat("###0.##");

                Amount amount = createAmountInstance(price, decimalFormat);
                TranscationCreation transactionObject = createTranscationCreationInstance(ride, client, amount);

                Entity<?> entity = Entity.entity(transactionObject, MediaType.APPLICATION_JSON);
                Response response = builder.post(entity);
                TransactionCreationResponse transactionCreationResponse = response.readEntity(TransactionCreationResponse.class);

                paymentService.addTransaction(transactionCreationResponse, ride);

                return Response.status(200).entity(transactionCreationResponse.getLinks().getCheckout()).build();

            } else {
                Link redirectUri = paymentService.returnExistingPaymentUrl(rideId);
                return Response.status(200).entity(redirectUri).build();
            }
        }
        //ELSE: Do nothing
        return Response.status(200).build();
    }

    private Amount createAmountInstance(double price, DecimalFormat decimalFormat) {
        Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setValue(decimalFormat.format(price));
        return amount;
    }

    private TranscationCreation createTranscationCreationInstance(RideDTO ride, ClientDTO client, Amount amount) {
        TranscationCreation transactionObject = new TranscationCreation();
        transactionObject.setDescription("Rit " + client.getFirstName() + " " + client.getLastName() + " naar " + ride.getDropOffLocation());
        transactionObject.setRedirectUrl(PaymentProperties.getInstance().getAppReturnUlr());
        transactionObject.setWebhookUrl(PaymentProperties.getInstance().getServerWebhookUrl());
        transactionObject.setAmount(amount);
        return transactionObject;
    }

    @POST
    @Path("transaction-updated")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response transactionStatusUpdated(@FormParam("id") String id) {
        Invocation.Builder builder = getMollieApiInvocation("payments/" + id);
        Response response = builder.get();
        TransactionStatusResponse statusResponse = response.readEntity(TransactionStatusResponse.class);
        try {
            paymentService.updateTransactionStatus(statusResponse);
        } catch (DataUnreachableException e) {
            LOGGER.error("Transaction Error", e);
        }
        return Response.status(200).build();
    }

    private Invocation.Builder getMollieApiInvocation(String path) {
        javax.ws.rs.client.Client webClient = ClientBuilder.newBuilder().newClient();
        WebTarget target = webClient.target(PaymentProperties.getInstance().getMollieApiUrl());
        target = target.path(path);
        Invocation.Builder builder = target.request();
        builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + PaymentProperties.getInstance().getMollieApiKey());
        return builder;
    }
}
