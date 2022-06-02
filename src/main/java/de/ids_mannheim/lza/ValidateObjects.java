package de.ids_mannheim.lza;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.wisc.library.ocfl.api.OcflRepository;
import edu.wisc.library.ocfl.api.model.ValidationResults;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Path("validate_objects")
public class ValidateObjects extends Function {

        /**
         * Method handling HTTP GET requests. The returned object will be sent
         * to the client as "application/json" media type.
         *
         * @return JSON response containing the result of the operation or HTTP error code 400
         */
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public Response validateObjects(@Context ResourceConfig ctx) throws JsonProcessingException {
            OcflRepository repo = (OcflRepository) ctx.getProperties().get("ocfl_repo");
            // Convert to JSON
            ObjectMapper mapper = JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .addModule(ObjectVersionFileSerializer.getModule())
                    .build();
            HashMap<String, ValidationResults> results = new HashMap<>();
            for (String object : repo.listObjectIds().collect(Collectors.toList())) {
                results.put(object,repo.validateObject(object,true));
            }
            String json = mapper.writeValueAsString(results);
            return Response.ok().entity(json).build() ;
        }

    public String getDescription() {
        return "Validates all objects in the store. Returns a JSON object mapping from object identifiers to " +
                "validation results";
    }

    public Map<String, String> getParameters() {
        return new HashMap<>();
    }
}
