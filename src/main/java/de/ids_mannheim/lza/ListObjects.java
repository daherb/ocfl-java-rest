package de.ids_mannheim.lza;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.wisc.library.ocfl.api.OcflRepository;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.stream.Collectors;


@Path("list_objects")
public class ListObjects {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return JSON response containing the list of objects or HTTP error code 400
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObjects(@Context ResourceConfig ctx) throws JsonProcessingException {
        // Convert to JSON
        ObjectMapper mapper = new ObjectMapper();
        String json =
                mapper.writeValueAsString(((OcflRepository) ctx.getProperties().get("ocfl_repo"))
                        .listObjectIds().collect(Collectors.toList()));
        return Response.ok().entity(json).build() ;
    }


}
