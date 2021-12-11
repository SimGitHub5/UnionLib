package com.stereowalker.unionlib.supporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.compress.utils.Lists;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stereowalker.unionlib.UnionLib;

public class Supporters {
	public static final List<Supporter> SUPPORTERS = Lists.newArrayList();
	protected static final Map<UUID, String> CAPES_LOCATION = Maps.newHashMap();
	protected static final Map<UUID, Boolean> CAPES_SHOWN = Maps.newHashMap();
	public static final String[] APPLICABLE_CAPES = new String[] {
			"custom1", "custom2", "custom3", "founder", "minecon2011", "minecon2012", "minecon2013", "minecon2015", 
			"minecon2016", "pancape", "stereowalker"};
	
	public static void debug(Object message) {
		UnionLib.debug(message);
	}
	
	public static String createJson() {
		String s = "{\r\n"
				+ "	\"supporters\": [";
		for (int i = 0; i < SUPPORTERS.size(); i++) {
			Supporter p = SUPPORTERS.get(i); 
			s+= (i==0?"":",")+"\r\n"
					+ "		{\r\n"
					+ "			\"uuid\": \""+p.uuid.toString()+"\", \r\n"
					+ "			\"name\": \""+p.displayName+"\",\r\n"
					+ "			\"type\": \""+p.type.toString().toLowerCase()+"\", \r\n"
					+ "			\"discordId\": "+p.discordId+",\r\n"
					+ "			\"showCape\": "+p.showCape+",\r\n"
					+ "			\"cape\": \""+p.cape+"\"\r\n"
					+ "		}";
		}
		s+= "\r\n"
				+ "	]\r\n"
				+ "}";
		return s;
	}
	
	public static void cacheSupporters(URL url, File cache) throws IOException {
		BufferedReader store = new BufferedReader(new InputStreamReader(url.openStream()));
		if (!cache.exists()) cache.createNewFile();
		try(PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(cache), StandardCharsets.UTF_8))){
			store.lines().forEach((line) -> printwriter.println(line));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		store.close();
	}

	public static void populateSupporters(File cache, boolean createCache) {
		BufferedReader read = null;
		try {
			URL url = new URL("https://raw.githubusercontent.com/Stereowalker/My-Home-Page/main/capes.json");
			URLConnection connection = url.openConnection();
			connection.connect();
			debug("Found the Supporter List File");
			read = new BufferedReader(new InputStreamReader(url.openStream()));
			if (createCache) cacheSupporters(url, cache);
			
		} catch (IOException e) {
			debug("Internet is not connected");
			if (cache.exists()) {
				FileInputStream stream;
				try {
					stream = new FileInputStream(cache);
					InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
					read = new BufferedReader(reader);
				} catch (FileNotFoundException e1) {
					debug("Supproter Cache file not found");
				}
			}
		}

		if (read != null) {
			Supporters.CAPES_LOCATION.clear();
			Supporters.CAPES_SHOWN.clear();
			Supporters.SUPPORTERS.clear();
			JsonElement element = JsonParser.parseReader(read);
			element.getAsJsonObject().getAsJsonArray("supporters").forEach((elem) -> {
				JsonObject object_2 = elem.getAsJsonObject();
				Supporters.Supporter sup = new Supporters.Supporter(
						object_2.get("name").getAsString(), 
						object_2.get("discordId").getAsLong(), 
						object_2.get("showCape").getAsBoolean(), 
						UUID.fromString(object_2.get("uuid").getAsString()), 
						Supporters.Type.byName(object_2.get("type").getAsString()), 
						object_2.get("cape").getAsString()
						);
				sup.insertSelfToList();
				if (sup.type() != Supporters.Type.DIAMOND) {
					debug("Found cape for "+sup.displayName()+" they seem to want to use the "+sup.cape()+" cape");
				}
			});
			//Sort
			List<Supporter> SUPPORTERS = Lists.newArrayList();
			for (Supporters.Type type : Supporters.Type.values()) {
				for (Supporters.Supporter sup2 : Supporters.SUPPORTERS) {
					if (sup2.type().equals(type)) SUPPORTERS.add(sup2);
				}
			}
			Supporters.SUPPORTERS.clear();
			Supporters.SUPPORTERS.addAll(SUPPORTERS);
			try {
				read.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			debug("No cache to load, skipping supporters. For now");
		}
	}
	
	public enum Type {
		OBSIDIAN(0x515461/*Raised values by 50*/, true), 
		DIAMOND(0x1cc4d8, true),
		PERSONAL(16733525, false), 
		OLDER(0x999999, false), 
		SELF(16755200, false);
		
		public int sty;
		public boolean disp;
		Type(int style, boolean display){
			this.sty = style;
			this.disp = display;
		}
		
		public static Type byName(String pName) {
			Type[] aboat$type = values();

	        for(int i = 0; i < aboat$type.length; ++i) {
	           if (aboat$type[i].name().toLowerCase().equals(pName)) {
	              return aboat$type[i];
	           }
	        }

	        return aboat$type[0];
	     }
	}
	
	public record Supporter(String displayName, long discordId, boolean showCape, UUID uuid, Type type, String cape) {
		public void insertSelfToList() {
			SUPPORTERS.add(this);
			if (type != Type.DIAMOND) {
				CAPES_LOCATION.put(uuid, cape);
				CAPES_SHOWN.put(uuid, showCape);
			}
		}
	}
}
