package de.ids_mannheim.lza;

import edu.wisc.library.ocfl.api.OcflRepository;
import edu.wisc.library.ocfl.api.model.ObjectVersionId;
import edu.wisc.library.ocfl.api.model.OcflObjectVersion;
import edu.wisc.library.ocfl.api.model.VersionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PutObject extends Function {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return JSON response containing the result of the operation or HTTP error code 400
     * @throws NoSuchPropertyException if the repository is missing from context
     */
    @GetMapping("put_object")
    public OcflObjectVersion putObject(@RequestParam("object_id") String id,
                                       @RequestParam(value="path",defaultValue = "null") String path,
                                       @RequestParam(value="name",defaultValue = "no_name") String name,
                                       @RequestParam(value = "address",defaultValue = "no_address") String address,
                                       @RequestParam(value = "message",defaultValue = "") String message) throws NoSuchPropertyException {
        OcflRepository repo = applicationContext.getEnvironment().getProperty("ocfl_repo",
                OcflRepository.class);
        if (repo != null) {
            // Get information
            VersionInfo versionInfo = new VersionInfo()
                .setUser(name,address)
                .setMessage(message);
            // Copy object
            repo.putObject(ObjectVersionId.head(id),
                java.nio.file.Path.of(path),
                versionInfo);
            return repo.getObject(ObjectVersionId.head(id));
        }
        throw new NoSuchPropertyException("Repository is missing from context");
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
