/*
 * Copyright 2015 Bern University of Applied Sciences.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.fusepool.p3.transformer.web.client.server;

import eu.fusepool.p3.accept.util.AcceptPreference;
import eu.fusepool.p3.transformer.client.Transformer;
import eu.fusepool.p3.transformer.client.TransformerClientImpl;
import eu.fusepool.p3.transformer.commons.Entity;
import eu.fusepool.p3.transformer.commons.util.InputStreamEntity;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

@Path("/")
public class RootResource {

    public RootResource() {
    }

    @GET
    @Produces("text/plain")
    public Entity transform(final @QueryParam("transformer") String transformerUri,
            final @QueryParam("resource") String resourceUri,
            final @HeaderParam("accept") String acceptHeader) throws IOException {
        if (transformerUri == null) {
            final String tansformerQueryParamMissingMsg = "Must specify a transformer query paramter";
            throw new WebApplicationException(tansformerQueryParamMissingMsg,
                    Response.status(Response.Status.BAD_REQUEST).entity(tansformerQueryParamMissingMsg).build());
        }
        if (resourceUri == null) {
            final String resourceQueryParamMissingMsg = "Must specify a resource query paramter";
            throw new WebApplicationException(resourceQueryParamMissingMsg,
                    Response.status(Response.Status.BAD_REQUEST).entity(resourceQueryParamMissingMsg).build());
        }
        final Transformer t = new TransformerClientImpl(transformerUri);
        final ClosableEntity entity = getEntity(resourceUri, t.getSupportedInputFormats());
        final List<AcceptPreference.AcceptHeaderEntry> acceptEntries = AcceptPreference.fromString(acceptHeader).getEntries();
        final MimeType[] acceptedTypes = new MimeType[acceptEntries.size()];
        for (int i = 0; i< acceptedTypes.length; i++) {
            acceptedTypes[i] = acceptEntries.get(i).getMediaType();
        }
        final Entity transformedEntity = t.transform(entity, acceptedTypes);
        return transformedEntity;
    }

    @GET
    @Path("/test")
    @Produces("text/plain")
    public String test() {
        return "Hello world...";
    }

    private ClosableEntity getEntity(String resourceUri, Set<MimeType> supportedInputFormats) throws IOException {
        final CloseableHttpClient httpclient = HttpClients.createDefault();
        final HttpGet request = new HttpGet(resourceUri);
        request.setHeader("Accept", toAcceptHeaderValue(supportedInputFormats.iterator()));
        final CloseableHttpResponse response = httpclient.execute(request);
        final HttpEntity httpEntity = response.getEntity();
        httpEntity.getContent();
        return new ClosableInputStreamEntity() {

            @Override
            public MimeType getType() {
                try {
                    return new MimeType(httpEntity.getContentType().getValue());
                } catch (MimeTypeParseException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public InputStream getData() throws IOException {
                return httpEntity.getContent();
            }

            @Override
            public void close() throws IOException {
                response.close();
            }
        };
    }

    private String toAcceptHeaderValue(Iterator<MimeType> iterator) {
        final String next = iterator.next().toString();
        if (iterator.hasNext()) {
            return next + "," + toAcceptHeaderValue(iterator);
        } else {
            return next;
        }
    }

    interface ClosableEntity extends Entity {
        public void close() throws IOException;
    }

    abstract class ClosableInputStreamEntity extends InputStreamEntity implements ClosableEntity {

    }

}
