package de.ids_mannheim.lza;

import jakarta.ws.rs.Path;

import java.util.Map;

abstract class Function {

    /**
     * Returns the name of the function based on the Path annotation
     * @return the name
     */
    public String getName() {
        if (this.getClass().isAnnotationPresent(Path.class))
            return this.getClass().getAnnotation(Path.class).value();
        else
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
