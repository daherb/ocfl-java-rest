package de.ids_mannheim.lza;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.wisc.library.ocfl.api.OcflRepository;
import edu.wisc.library.ocfl.api.model.ObjectVersionId;
import edu.wisc.library.ocfl.api.model.OcflObjectVersion;
import edu.wisc.library.ocfl.api.model.OcflObjectVersionFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GetObject extends Function {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return JSON response containing the result of the operation or HTTP error code 400
     */
    @GetMapping("get_object")
    public OcflObjectVersion getObject(@RequestParam("object_id") String id,
                                       @RequestParam(value = "path", defaultValue = "null") String path
                                           ) throws MalformedURLException, JsonProcessingException {
        OcflRepository repo = applicationContext.getEnvironment().getProperty("ocfl_repo",
                OcflRepository.class);
        // Get object info
        OcflObjectVersion info = repo.getObject(ObjectVersionId.head(id));
        // Copy object if path is not null
        if (!path.equals("null")) {
            repo.getObject(ObjectVersionId.head(id),
                    java.nio.file.Path.of(path));
        }
        return info;
//        return JsonMapper.builder()
//                .addModule(new JavaTimeModule())
//                .addModule(ObjectVersionFileSerializer.getModule())
//                .build()
//                .writeValueAsString(info);
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
