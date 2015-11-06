package io.vertigo.vega.impl.rest.handler;

import io.vertigo.vega.impl.rest.RestHandlerPlugin;
import io.vertigo.vega.impl.rest.multipart.ApacheMultipartHelper;
import io.vertigo.vega.plugins.rest.handler.HandlerChain;
import io.vertigo.vega.plugins.rest.handler.RouteContext;
import io.vertigo.vega.rest.exception.SessionException;
import io.vertigo.vega.rest.exception.VSecurityException;
import io.vertigo.vega.rest.metamodel.EndPointDefinition;
import spark.Request;
import spark.Response;

/**
 * Plugin d'upload de fichier, par la librairie org.apache.commons.upload.
 *
 * @author npiedeloup
 * @version $Id: ApacheFileUploadPlugin.java,v 1.11 2013/06/25 10:57:08 pchretien Exp $
 */
public final class MultipartHandler implements RestHandlerPlugin {

	@Override
	public boolean accept(final EndPointDefinition endPointDefinition) {
		return true;
	}

	@Override
	public Object handle(final Request request, final Response response, final RouteContext routeContext, final HandlerChain chain) throws SessionException, VSecurityException {
		if (ApacheMultipartHelper.isMultipart(request)) {
			final Request wrappedRequest = ApacheMultipartHelper.createRequestWrapper(request);
			return chain.handle(wrappedRequest, response, routeContext);
		}
		//TODO voir si l'on peut d�tecter l'oublie de contentType = "multipart/form-data" sur le form, c'est une erreur standard
		return chain.handle(request, response, routeContext);
	}

}
