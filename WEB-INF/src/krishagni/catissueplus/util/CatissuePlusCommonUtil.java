
package krishagni.catissueplus.util;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import krishagni.catissueplus.dto.SpecimenDTO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;

public class CatissuePlusCommonUtil {

	public static Gson getGson() {
		JsonDeserializer dese = new JsonDeserializer<Date>() {

			DateFormat df = new SimpleDateFormat(ApplicationProperties.getValue("date.pattern"));

			@Override
			public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
					throws JsonParseException {
				Date retObj = null;
				try {

					if (!Validator.isEmpty(json.getAsString())) {
						retObj = df.parse(json.getAsString());
					}

				}
				catch (ParseException e) {
					throw new JsonParseException("date");
				}
				return retObj;
			}
		};
		GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(Date.class, dese);
		Gson gson = gsonBuilder.setDateFormat(ApplicationProperties.getValue("date.pattern")).create();
		return gson;

	}

	public static String getAliquotEventJsonString(SpecimenDTO specimenDTO, Long userId, int aliquotCount,
			Long formContextId) {
		String aliquotJsonString = "{\"appData\":{\"formCtxtId\":" + formContextId + ",\"objectId\" :"
				+ specimenDTO.getParentSpecimenId() + "},\"user\":" + userId + ",\"time\":\""
				+ new SimpleDateFormat(ApplicationProperties.getValue("date.pattern.timestamp")).format(new Date())
				+ "\",\"aliquotCount\":\"" + aliquotCount + "\" }";

		return aliquotJsonString;
	}

	public static String getDerivativeEventJsonString(SpecimenDTO specimenDTO, Long userId, Long formContextId) {

		String derivativeJsonString = "{\"appData\":{\"formCtxtId\":" + formContextId + ",\"objectId\" :"
				+ specimenDTO.getParentSpecimenId() + "},\"user\":" + userId + ",\"time\":\""
				+ new SimpleDateFormat(ApplicationProperties.getValue("date.pattern.timestamp")).format(new Date())
				+ "\",\"derivativeType\":\" " + specimenDTO.getType() + "\" }";

		return derivativeJsonString;
	}
}
