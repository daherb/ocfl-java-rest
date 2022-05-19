package de.ids_mannheim.lza;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import edu.wisc.library.ocfl.api.model.OcflObjectVersionFile;

import java.io.IOException;

/**
 * Class to serialize OclObjectVersionFile without the "stream" field
 */
public class ObjectVersionFileSerializer extends StdSerializer<OcflObjectVersionFile> {

    protected ObjectVersionFileSerializer(Class<OcflObjectVersionFile> t) {
        super(t);
    }

    @Override
    public void serialize(OcflObjectVersionFile ocflObjectVersionFile, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("fixity", ocflObjectVersionFile.getFixity());
        jsonGenerator.writeStringField("path", ocflObjectVersionFile.getPath());
        jsonGenerator.writeStringField("storageRelativePath", ocflObjectVersionFile.getStorageRelativePath());
        jsonGenerator.writeEndObject();
    }

    public static Module getModule() {
        SimpleModule module =
                new SimpleModule("CustomObjectVersionFileSerializer");
        module.addSerializer(OcflObjectVersionFile.class, new ObjectVersionFileSerializer(OcflObjectVersionFile.class));
        return module;
    }
}
