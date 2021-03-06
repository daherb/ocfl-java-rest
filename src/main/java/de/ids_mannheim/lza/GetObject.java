package de.ids_mannheim.lza;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.wisc.library.ocfl.api.OcflRepository;
import edu.wisc.library.ocfl.api.model.ObjectVersionId;
import edu.wisc.library.ocfl.api.model.OcflObjectVersion;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

@Path("get_object")
public class GetObject extends Function {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return JSON response containing the result of the operation or HTTP error code 400
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObject(@QueryParam("object_id") @NotNull String id,
                              @QueryParam("path") @NotNull String path,
                              @Context ResourceConfig ctx) throws JsonProcessingException, MalformedURLException {
        OcflRepository repo = (OcflRepository) ctx.getProperties().get("ocfl_repo");
        // Get object info
        OcflObjectVersion info = repo.getObject(ObjectVersionId.head(id));
        // Copy object
        if (path == null)
            throw new MalformedURLException("Path is required");
        repo.getObject(ObjectVersionId.head(id),
                java.nio.file.Path.of(path));
        // Convert to JSON
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .addModule(ObjectVersionFileSerializer.getModule())
                .build();
        String json = mapper.writeValueAsString(info);
        return Response.ok().entity(json).build() ;
    }

    public String getDescription() {
        return "Gets an object from the store. Returns a JSON object representing the current state of the object";
    }

    public Map<String, String> getParameters() {
        Map<String,String> params = new HashMap<>();
        params.put("object_id", "The ID of the object. If the object does not exist it creates an error. (mandatory)");
        params.put("path","The path where the files stored in the object should be copied. The target itself must not" +
                " exist but its parent must exist. (mandatory)");
        return params;
    }

}
