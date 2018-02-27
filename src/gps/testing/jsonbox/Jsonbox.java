package gps.testing.jsonbox;

import java.util.Map;
import com.google.gson.Gson;
import no.ntnu.item.arctis.runtime.Block;

public class Jsonbox extends Block {
	public void parameters(String para) {
		String json = para;
		Map jsonJavaRootObject = new Gson().fromJson(json, Map.class);
		System.out.println("soma--" + jsonJavaRootObject.get("latitude"));
	}
}
