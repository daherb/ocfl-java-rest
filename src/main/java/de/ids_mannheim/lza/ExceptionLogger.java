package de.ids_mannheim.lza;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 * The Exception handler logging exceptions.
 */
@Provider
public class ExceptionLogger implements ExceptionMapper<Exception> {

    private static final Logger log = Logger.getLogger("ExceptionLog");

    @Override
    public Response toResponse(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        try {
            sw.close();
            log.severe("Exception:" + sw);
        }
        catch (IOException exception) {
            log.severe("Exception when closing stream:" + exception);
        }
        return Response.status(500).entity(e.toString()).build();
    }
}
