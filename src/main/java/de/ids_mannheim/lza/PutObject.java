package de.ids_mannheim.lza;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.wisc.library.ocfl.api.OcflRepository;
import edu.wisc.library.ocfl.api.model.ObjectVersionId;
import edu.wisc.library.ocfl.api.model.OcflObjectVersion;
import edu.wisc.library.ocfl.api.model.VersionInfo;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.*;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.HashMap;
import java.util.Map;

@Path("put_object")
public class PutObject extends Function {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return JSON response containing the result of the operation or HTTP error code 400
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response putObject(@QueryParam("object_id") @NotNull String id,
                              @QueryParam("path") @NotNull String path,
                              @QueryParam("name") String name,
                              @QueryParam("address") String address,
                              @QueryParam("message") String message,
                              @Context ResourceConfig ctx) throws JsonProcessingException {
        OcflRepository repo = (OcflRepository) ctx.getProperties().get("ocfl_repo");
        // Copy object
        VersionInfo versionInfo = new VersionInfo()
                .setUser(name,address)
                .setMessage(message);
        repo.putObject(ObjectVersionId.head(id),
                java.nio.file.Path.of(path),
                versionInfo);
        OcflObjectVersion info = repo.getObject(ObjectVersionId.head(id));
        // Convert to JSON
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .addModule(ObjectVersionFileSerializer.getModule())
                .build();
        String json = mapper.writeValueAsString(info);
        return Response.ok().entity(json).build() ;
    }

    public String getDescription() {
        return "Puts an object into the store. Returns JSON object representing the most recent state of the object, " +
                "i.e. after putting it into the store";
    }

    public Map<String, String> getParameters() {
        Map<String,String> params = new HashMap<>();
        params.put("object_id", "The ID of the object. If the object does not exist, it will be created, otherwise it" +
                " will be updated (mandatory)");
        params.put("path", "Path to the folder containing the files the object should contain (mandatory)");
                params.put("name", "The name of the user performing the action (optional)");
                params.put("address", "The contact of the user, should be an URI, e.g. mailto: (optional)");
                params.put("message", "A short string similar to a Git commit message (optional)");
        return params;
    }
}
