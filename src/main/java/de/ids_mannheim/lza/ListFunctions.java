package de.ids_mannheim.lza;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

@RestController
public class ListFunctions {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    public class FunctionInfo {
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
    public List<FunctionInfo> listFunctions() {
        // Get all functions using reflections
        List<FunctionInfo> info = new ArrayList<>();
        for (Map.Entry<String,Function> fun : applicationContext.getBeansOfType(Function.class).entrySet()) {
            info.add(new FunctionInfo(fun.getValue().getName(),
                    fun.getValue().getDescription(),
                    fun.getValue().getParameters())
            );
        }
//        Reflections reflections = new Reflections(this.getClass().getPackageName());
//        // Get all classes derived from Function
//        for (Class<? extends Function> f :
//                 reflections.getSubTypesOf(Function.class).stream()
//                        .filter((c) -> !Modifier.isAbstract(c.getModifiers())).collect(Collectors.toSet())) {
//            Function fo = f.getConstructor().newInstance();
//            info.add(new FunctionInfo(fo.getName(), fo.getDescription(), fo.getParameters()));
//        }
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Info{0}", info.stream().map((i) -> i.name).collect(Collectors.joining(",")));
	return info;
    }
}
