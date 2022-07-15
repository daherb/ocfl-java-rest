package de.ids_mannheim.lza;

import edu.wisc.library.ocfl.api.OcflRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ListObjects extends Function {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return JSON response containing the list of objects or HTTP error code 400
     */
    @GetMapping("list_objects")
    public List<String> listObjects() {
    List<String> objectList = applicationContext.getEnvironment().getProperty("ocfl_repo",
            OcflRepository.class)
                                .listObjectIds().collect(Collectors.toList());
        return objectList ;
    }

    public String getDescription() {
        return "List all objects in the store. Returns a JSON list of strings containing all object identifiers";
    }

    public Map<String, String> getParameters() {
        return new HashMap<>();
    }
}
