package fr.corenting.edcompanion.utils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import fr.corenting.edcompanion.models.apis.EDSM.EDSMSystemInformationResponse;

public class EDSMDeserializer implements JsonDeserializer<EDSMSystemInformationResponse> {

    @Override
    public EDSMSystemInformationResponse deserialize(JsonElement element, Type type,
                                                     JsonDeserializationContext deserializationContext)
            throws JsonParseException {
        if (element.isJsonArray()) {
            return null;
        } else {
            Gson gson = new Gson();
            return gson.fromJson(element.getAsJsonObject(), EDSMSystemInformationResponse.class);
        }
    }

}