package de.ids_mannheim.lza;
import edu.wisc.library.ocfl.api.OcflRepository;
import edu.wisc.library.ocfl.api.model.ObjectVersionId;
import edu.wisc.library.ocfl.api.model.OcflObjectVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GetObject extends Function {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    /**
     * Method handling HTTP GET requests.The returned object will be sent
 to the client as "application/json" media type.
     *
     * @param id
     * @param path
     * @return JSON response containing the result of the operation or HTTP error code 400
     * @throws NoSuchPropertyException if repository is missing from context
     */
    @GetMapping("get_object")
    public OcflObjectVersion getObject(@RequestParam("object_id") String id,
                                       @RequestParam(value = "path", defaultValue = "null") String path
                                           ) throws NoSuchPropertyException {
        OcflRepository repo = applicationContext.getEnvironment().getProperty("ocfl_repo",
                OcflRepository.class);
        if (repo == null)
            throw new NoSuchPropertyException("Repository missing from context");
        // Get object info
        OcflObjectVersion info = repo.getObject(ObjectVersionId.head(id));
        // Copy object if path is not null
        if (!path.equals("null")) {
            repo.getObject(ObjectVersionId.head(id),
                    java.nio.file.Path.of(path));
        }
        return info;
    }

    @Override
    public String getDescription() {
        return "Gets an object from the store. Returns a JSON object representing the current state of the object";
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String,String> params = new HashMap<>();
        params.put("object_id", "The ID of the object. If the object does not exist it creates an error. (mandatory)");
        params.put("path","The path where the files stored in the object should be copied. The target itself must not" +
                " exist but its parent must exist. If the path is missing only the object information is shown.");
        return params;
    }

}
