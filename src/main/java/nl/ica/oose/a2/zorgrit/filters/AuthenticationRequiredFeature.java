package nl.ica.oose.a2.zorgrit.filters;

import nl.ica.oose.a2.zorgrit.service.IOAuthService;

import javax.inject.Inject;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticationRequiredFeature implements DynamicFeature {

    @Inject
    private IOAuthService oauthService;

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
        AuthenticationRequired required = resourceInfo.getResourceMethod().getAnnotation(AuthenticationRequired.class);
        if(required == null) {
            return;
        }
        AuthenticationRequiredFilter filter = new AuthenticationRequiredFilter(oauthService);
        featureContext.register(filter);
    }
}
