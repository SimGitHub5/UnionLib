package com.stereowalker.unionlib.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.stereowalker.unionlib.UnionLib;

import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class ConfigBuilder {
	static Map<String,Holder<?>> client_values = new HashMap<String,Holder<?>>();
	static Map<String,Holder<?>> common_values = new HashMap<String,Holder<?>>();
	static Map<String,Holder<?>> server_values = new HashMap<String,Holder<?>>();

	static Map<String,Holder<?>> retrieveValues(ModConfig.Type... types) {
		Map<String,Holder<?>> values = new HashMap<String,Holder<?>>();
		boolean hasClient = false; 
		boolean hasCommon = false; 
		boolean hasServer = false;
		for (ModConfig.Type type : types) {
			if (type == Type.CLIENT && !hasClient) {
				hasClient = true;
				values.putAll(client_values);
			}
			if (type == Type.COMMON && !hasCommon) {
				hasCommon = true;
				values.putAll(common_values);
			}
			if (type == Type.SERVER && !hasServer) {
				hasServer = true;
				values.putAll(server_values);
			}
		}
		return values;
	}

	static void putValue(ModConfig.Type type, String key, Holder<?> value) {
		if (type == Type.CLIENT) {
			client_values.put(key, value);
		}
		if (type == Type.COMMON) {
			common_values.put(key, value);
		}
		if (type == Type.SERVER) {
			server_values.put(key, value);
		}
	}

	public static String configName(UnionConfig.Entry configEntry, String dataType) {

		if (configEntry.group().isEmpty()) {
			if (!dataType.isEmpty()) {
				return dataType+": "+configEntry.name();
			} else {
				return configEntry.name();
			}
		} else {
			if (!dataType.isEmpty()) {
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
			return retrieveValues(Type.CLIENT, Type.COMMON, Type.SERVER).getOrDefault(config.name()+"="+configName(configEntry), new Holder<>(new ForgeConfigSpec.Builder().define("empty", "nothing_was_found"), "nothing_was_found", null, false, 0, 0)).getValue();
		} else {
			return null;
		}
	}

	public static Map<String,Holder<?>> getValues(UnionConfig config) {
		Map<String,Holder<?>> values2 = new HashMap<String,Holder<?>>();

		for (String configValue : retrieveValues(Type.CLIENT, Type.COMMON, Type.SERVER).keySet()) {
			if (configValue.split("=")[0].equals(config.name())) {
				values2.put(configValue, retrieveValues(Type.CLIENT, Type.COMMON, Type.SERVER).get(configValue));
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
		if (configClass.isAnnotationPresent(UnionConfig.class) && !ConfigClassBuilder.configs.contains(configClass)) {
			UnionConfig con = configClass.getAnnotation(UnionConfig.class);
			UnionLib.debug("Registered the config for "+con.name());
			ConfigClassBuilder.configs.add(configClass);
			ConfigClassBuilder.client_builder.put(configClass, new ForgeConfigSpec.Builder());
			ConfigClassBuilder.common_builder.put(configClass, new ForgeConfigSpec.Builder());
			ConfigClassBuilder.server_builder.put(configClass, new ForgeConfigSpec.Builder());

			ConfigClassBuilder.registerConfigurations(configClass);
			ConfigClassBuilder.loadConfigs(configClass);
			ConfigClassBuilder.read(configClass);
		} else if (!configClass.isAnnotationPresent(UnionConfig.class)) {
			throw new RuntimeException("You cannot register a config if it does not have the UnionConfig annotation");
		} else if (ConfigClassBuilder.configs.contains(configClass)) {
			throw new RuntimeException("This config class has already been regtistered");
		}
	}

	public static void registerConfig(ConfigObject configObject) {
		if (configObject.getClass().isAnnotationPresent(UnionConfig.class) && !ConfigObjectBuilder.configs.contains(configObject)) {
			UnionConfig con = configObject.getClass().getAnnotation(UnionConfig.class);
			UnionLib.debug("Registered the config for "+con.name());
			ConfigObjectBuilder.configs.add(configObject);
			ConfigObjectBuilder.client_builder.put(configObject, new ForgeConfigSpec.Builder());
			ConfigObjectBuilder.common_builder.put(configObject, new ForgeConfigSpec.Builder());
			ConfigObjectBuilder.server_builder.put(configObject, new ForgeConfigSpec.Builder());

			ConfigObjectBuilder.registerConfigurations(configObject);
			ConfigObjectBuilder.loadConfigs(configObject);
			ConfigObjectBuilder.read(configObject);
		} else if (!configObject.getClass().isAnnotationPresent(UnionConfig.class)) {
			throw new RuntimeException("You cannot register a config if it does not have the UnionConfig annotation");
		} else if (ConfigObjectBuilder.configs.contains(configObject)) {
			throw new RuntimeException("This config object has already been regtistered");
		}
	}

	public static void loadConfig(ForgeConfigSpec config, String path, String fileName) {
		File configFile = new File(path, fileName);
		configFile.getParentFile().mkdirs();
		UnionLib.debug(path);
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

	public static void reload() {
		for (Class<?> configClass : ConfigClassBuilder.configs) {
			UnionConfig con = configClass.getAnnotation(UnionConfig.class);
			if (con.autoReload()) {
				ConfigClassBuilder.read(configClass);
				UnionLib.debug("Detected change in "+con.name()+"'s config file. Reloading values");
			}
		}
		for (ConfigObject configObject : ConfigObjectBuilder.configs) {
			UnionConfig con = configObject.getClass().getAnnotation(UnionConfig.class);
			if (con.autoReload()) {
				ConfigObjectBuilder.read(configObject);
				UnionLib.debug("Detected change in "+con.name()+"'s config file. Reloading values");
			}
		}
	}

	public static void load(ModConfig.Type... exceptions) {
		UnionLib.debug("Loading all values from the config files into their respective configuration variables");
		for (Class<?> configClass : ConfigClassBuilder.configs) {
			UnionConfig con = configClass.getAnnotation(UnionConfig.class);
			ConfigClassBuilder.read(configClass, exceptions);
			UnionLib.debug("Loading "+con.name()+"'s config");
		}
		for (ConfigObject configObject : ConfigObjectBuilder.configs) {
			UnionConfig con = configObject.getClass().getAnnotation(UnionConfig.class);
			ConfigObjectBuilder.read(configObject, exceptions);
			UnionLib.debug("Loading "+con.name()+"'s config");
		}
	}

	private static final Splitter DOT_SPLITTER = Splitter.on(".");
	static List<String> split(String path)
	{
		return Lists.newArrayList(DOT_SPLITTER.split(path));
	}

	@EventBusSubscriber(bus = Bus.MOD)
	public static class ModEventBus {
		@SubscribeEvent
		public static void onLoad(ModConfigEvent.Loading event) {
			load();
		}
		@SubscribeEvent
		public static void onReload(ModConfigEvent.Reloading event) {
			reload();
		}
	}
	@EventBusSubscriber(bus = Bus.FORGE)
	public static class ForgeEventBus {
		@SubscribeEvent
		public static void onLoad(WorldEvent.Load event) {
			load(ModConfig.Type.COMMON);
			if (!event.getWorld().isClientSide()) {
				UnionLib.debug("Loading All Server Config Files");
				load(ModConfig.Type.SERVER);
			} else {
				UnionLib.debug("Loading All Client Config Files");
				load(ModConfig.Type.CLIENT);
			}
		}
	}

	public static class Holder<T extends Object> {
		protected final ForgeConfigSpec.ConfigValue<T> value;
		protected final T defaultValue;
		protected final List<Component> comments;
		protected final	boolean usesSlider;
		protected final double min;
		protected final double max;
		public Holder(final ForgeConfigSpec.ConfigValue<T> configvalue, final T defaultValue, final List<Component> comments, final boolean usesSlider, final double min, final double max) {
			this.comments = comments;
			this.value = configvalue;
			this.defaultValue = defaultValue;
			this.usesSlider = usesSlider;
			this.min = min;
			this.max = max;
		}
		public ForgeConfigSpec.ConfigValue<T> getValue() {
			return value;
		}
		public T getDefaultValue() {
			return defaultValue;
		}
		public List<Component> getComments() {
			return comments;
		}
		public boolean isUsingSlider() {
			return usesSlider;
		}
		public double getMin() {
			return min;
		}
		public double getMax() {
			return max;
		}

	}
}
