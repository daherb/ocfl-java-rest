package de.ids_mannheim.lza;

import edu.wisc.library.ocfl.api.OcflRepository;
import edu.wisc.library.ocfl.api.model.ValidationResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ValidateObjects extends Function {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    /**
     * Method handling HTTP GET requests.The returned object will be sent
 to the client as "application/json" media type.
     *
     * @return JSON response containing the result of the operation or HTTP error code 400
     * @throws NoSuchPropertyException If repository is missing from context
     */
    @GetMapping("validate_objects")
    public Map<String, ValidationResults> validateObjects() throws NoSuchPropertyException {
        OcflRepository repo = applicationContext.getEnvironment().getProperty("ocfl_repo",
                OcflRepository.class);
        HashMap<String, ValidationResults> results = new HashMap<>();
        if (repo != null) {
            for (String object : repo.listObjectIds().collect(Collectors.toList())) {
                results.put(object,repo.validateObject(object,true));
            }
            return results;
        }
        throw new NoSuchPropertyException("Repository is missing from context");
    }

    public String getDescription() {
        return "Validates all objects in the store. Returns a JSON object mapping from object identifiers to " +
                "validation results";
    }

    public Map<String, String> getParameters() {
        return new HashMap<>();
    }
}
