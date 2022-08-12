package de.ids_mannheim.lza;

import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Method;
import java.util.Map;

abstract class Function {

    /**
     * Returns the name of the function based on the Path annotation
     * @return the name
     */
    public String getName() {
        for (Method m : this.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(GetMapping.class)) {
                return String.join(",", m.getAnnotation(GetMapping.class).value());
            }
        }
        throw new RuntimeException("Path annotation missing in class " + this.getClass());
    }

    /**
     * Returns a short description of the function
     * @return the description
     */
    abstract String getDescription();

    /**
     * Returns a map of parameters with short descriptions
     * @return the map of parameters and descriptions
     */
    abstract Map<String, String> getParameters();

}
