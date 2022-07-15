package de.ids_mannheim.lza;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.reflections.Reflections;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
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

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Map<String, String> getParams() {
            return params;
        }

        @Override
        public String toString() {
            return "FunctionInfo{" +
                    "name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", params=" + params +
                    '}';
        }
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return list of information about all functions
     */
    @GetMapping("/")
    public List<FunctionInfo> listFunctions() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
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
	return info;
    }
}
