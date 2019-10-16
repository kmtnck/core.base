package it.alessandromodica.product.common;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * Un adattatore json che converte in java json e viceversa
 * 
 * @author Alessandro
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdapterGson {

	private static TypeAdapter<Number> IntegerTypeAdapter = new TypeAdapter<Number>() {

		@Override
		public void write(JsonWriter out, Number value) throws IOException {
			out.value(value);
		}

		@Override
		public Number read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			try {
				String result = in.nextString();
				if ("".equals(result)) {
					return null;
				}
				return Integer.parseInt(result);
			} catch (NumberFormatException e) {
				throw new JsonSyntaxException(e);
			} catch (Exception e) {
				throw new IOException(e);
			}
		}
	};

	private static TypeAdapter<Number> DoubleTypeAdapter = new TypeAdapter<Number>() {

		@Override
		public void write(JsonWriter out, Number value) throws IOException {
			out.value(value);
		}

		@Override
		public Number read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			try {
				String result = in.nextString();
				if ("".equals(result)) {
					return null;
				}
				return Double.parseDouble(result);
			} catch (NumberFormatException e) {
				throw new JsonSyntaxException(e);
			} catch (Exception e) {
				throw new IOException(e);
			}
		}
	};

	public static boolean isJSONValid(String jsonInString, Class entity) {
		try {
			gson.fromJson(jsonInString, entity);
			return true;
		} catch (com.google.gson.JsonSyntaxException ex) {
			return false;
		}
	}

	private static Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class, IntegerTypeAdapter)
			.registerTypeAdapter(Double.class, DoubleTypeAdapter).setPrettyPrinting().serializeNulls()
			.setDateFormat("dd/MM/yyyy").create();

	public static Gson GetInstance() {
		return gson;
	}
}
