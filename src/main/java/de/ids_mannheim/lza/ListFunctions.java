package de.ids_mannheim.lza;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/")
public class ListFunctions {
    class FunctionInfo {
        String name;
        String description;
        Map<String,String> params;

        public FunctionInfo(String name, String description, Map<String, String> params) {
            this.name = name;
            this.description = description;
            this.params = params;
        }
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return JSON response containing the result of the operation or HTTP error code 400
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listFunctions(@Context ResourceConfig ctx) throws JsonProcessingException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // Get all functions using reflections
        List<FunctionInfo> info = new ArrayList<>();
        Reflections reflections = new Reflections(this.getClass().getPackageName());
        // Get all classes derived from Function
        for (Class<? extends Function> f :
                reflections.getSubTypesOf(Function.class).stream()
                        .filter((c) -> !Modifier.isAbstract(c.getModifiers())).collect(Collectors.toSet())) {
            Function fo = f.getConstructor().newInstance();
            info.add(new FunctionInfo(fo.getName(), fo.getDescription(), fo.getParameters()));
        }
        // Convert to JSON
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .addModule(ObjectVersionFileSerializer.getModule())
                .build();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String json = mapper.writeValueAsString(info);
        return Response.ok().entity(json).build() ;
    }
}
