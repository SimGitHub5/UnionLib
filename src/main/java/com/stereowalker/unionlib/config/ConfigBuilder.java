package com.stereowalker.unionlib.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.EnumGetMethod;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLPaths;

@EventBusSubscriber(bus = Bus.MOD)
public class ConfigBuilder {
	static Map<String,Holder> values = new HashMap<String,Holder>();
	
	public static String configName(UnionConfig.Entry configEntry, String dataType) {
		
		if (configEntry.group() == "") {
			if (dataType != "") {
				return dataType+": "+configEntry.name();
			} else {
				return configEntry.name();
			}
		} else {
			if (dataType != "") {
				return configEntry.group()+"."+dataType+": "+configEntry.name();
			} else {
				return configEntry.group()+"."+configEntry.name();
			}
		}
	}
	
	public static String configName(UnionConfig.Entry configEntry) {
		return configName(configEntry, "");
	}

	static ForgeConfigSpec.ConfigValue<?> getConfigValue(UnionConfig config, UnionConfig.Entry configEntry){
		if (config != null && configEntry != null) {
			return values.getOrDefault(config.name()+"="+configName(configEntry), new Holder(new ForgeConfigSpec.Builder().define("empty", "nothing_was_found"), null, false, 0, 0)).getValue();
		} else {
			return null;
		}
	}
	
	public static Map<String,Holder> getValues(UnionConfig config) {
		Map<String,Holder> values2 = new HashMap<String,Holder>();
		
		for (String configValue : values.keySet()) {
			if (configValue.split("=")[0].equals(config.name())) {
				values2.put(configValue, values.get(configValue));
			}
		}
		return values2;
	}

	/**
	 * This is where all config classes are registered. 
	 * As long as the class has the annotation {@link UnionConfig} it will be registered
	 * @param configClass
	 */
	public static void registerConfig(Class<?> configClass) {
		if (configClass.isAnnotationPresent(UnionConfig.class)) {
			UnionConfig con = configClass.getAnnotation(UnionConfig.class);
			System.out.println("Registered the config for "+con.name());
			ConfigClassBuilder.configs.add(configClass);
			ConfigClassBuilder.client_builder.put(configClass, new ForgeConfigSpec.Builder());
			ConfigClassBuilder.common_builder.put(configClass, new ForgeConfigSpec.Builder());
			ConfigClassBuilder.server_builder.put(configClass, new ForgeConfigSpec.Builder());
			
			ConfigClassBuilder.registerConfigurations(configClass);
			ConfigClassBuilder.loadConfigs(configClass);
			ConfigClassBuilder.read(configClass);
		}
	}
	
	public static void registerConfig(ConfigObject configObject) {
		if (configObject.getClass().isAnnotationPresent(UnionConfig.class)) {
			UnionConfig con = configObject.getClass().getAnnotation(UnionConfig.class);
			System.out.println("Registered the config for "+con.name());
			ConfigObjectBuilder.configs.add(configObject);
			ConfigObjectBuilder.client_builder.put(configObject, new ForgeConfigSpec.Builder());
			ConfigObjectBuilder.common_builder.put(configObject, new ForgeConfigSpec.Builder());
			ConfigObjectBuilder.server_builder.put(configObject, new ForgeConfigSpec.Builder());
			
			ConfigObjectBuilder.registerConfigurations(configObject);
			ConfigObjectBuilder.loadConfigs(configObject);
			ConfigObjectBuilder.read(configObject);
		}
	}

	public static void loadConfig(ForgeConfigSpec config, String path, String fileName) {
		File configFile = new File(path, fileName);
		configFile.getParentFile().mkdirs();
		System.out.println(path);
		CommentedFileConfig file = (CommentedFileConfig)CommentedFileConfig.builder(configFile).sync().autosave().preserveInsertionOrder().onFileNotFound((newfile, configFormat) -> setupConfigFile(configFile, newfile, configFormat)).writingMode(WritingMode.REPLACE).build();
		file.load();
		config.setConfig(file);
	}

	private static boolean setupConfigFile(File path, Path file, ConfigFormat<?> conf) throws IOException {
		Path p = Paths.get(path.getPath(), new String[0]);
		if (Files.exists(p)) {
			//	      Combat.LOGGER.info(CONFIG, "Loading default config file from path {}", p);
			Files.copy(p, file);
		} else {
			Files.createFile(file);
			conf.initEmptyFile(file);
		} 
		return true;
	}

	@SubscribeEvent
	public static void onReload(ModConfigEvent event) {
		reload();
		System.out.println("From the event itself");
	}

	@SubscribeEvent
	public static void onReload(ModConfigEvent.Reloading event) {
		reload();
	}
	
	public static void reload() {
		for (Class<?> configClass : ConfigClassBuilder.configs) {
			UnionConfig con = configClass.getAnnotation(UnionConfig.class);
			if (con.autoReload()) {
				ConfigClassBuilder.read(configClass);
				System.out.println("Detected change in "+con.name()+"'s config file. Reloading values");
			}
		}
		for (ConfigObject configObject : ConfigObjectBuilder.configs) {
			UnionConfig con = configObject.getClass().getAnnotation(UnionConfig.class);
			if (con.autoReload()) {
				ConfigObjectBuilder.read(configObject);
				System.out.println("Detected change in "+con.name()+"'s config file. Reloading values");
			}
		}
	}
	
	public static void load() {
		System.out.println("Loading all values from the config files into their respective configuration variables");
		for (Class<?> configClass : ConfigClassBuilder.configs) {
			UnionConfig con = configClass.getAnnotation(UnionConfig.class);
			ConfigClassBuilder.read(configClass);
			System.out.println("Loading "+con.name()+"'s config");
		}
		for (ConfigObject configObject : ConfigObjectBuilder.configs) {
			UnionConfig con = configObject.getClass().getAnnotation(UnionConfig.class);
			ConfigObjectBuilder.read(configObject);
			System.out.println("Loading "+con.name()+"'s config");
		}
	}

	@SubscribeEvent
	public static void onLoad(ModConfigEvent.Loading event) {
		load();
	}
	
    private static final Splitter DOT_SPLITTER = Splitter.on(".");
    static List<String> split(String path)
    {
        return Lists.newArrayList(DOT_SPLITTER.split(path));
    }
    
    public static class Holder {
    	protected final ForgeConfigSpec.ConfigValue<?> value;
    	protected final List<Component> comments;
    	protected final	boolean usesSider;
    	protected final double min;
    	protected final double max;
    	public Holder(final ForgeConfigSpec.ConfigValue<?> configvalue, final List<Component> comments, final boolean usesSider, final double min, final double max) {
    		this.comments = comments;
    		this.value = configvalue;
    		this.usesSider = usesSider;
    		this.min = min;
    		this.max = max;
		}
		public ForgeConfigSpec.ConfigValue<?> getValue() {
			return value;
		}
		public List<Component> getComments() {
			return comments;
		}
		public boolean isUsesSider() {
			return usesSider;
		}
		public double getMin() {
			return min;
		}
		public double getMax() {
			return max;
		}
    	
    }
}
