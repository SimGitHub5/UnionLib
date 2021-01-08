package com.stereowalker.unionlib.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.stereowalker.unionlib.config.annotations.UnionConfig;
import com.stereowalker.unionlib.config.annotations.UnionConfigComment;
import com.stereowalker.unionlib.config.annotations.UnionConfigEntry;
import com.stereowalker.unionlib.config.annotations.UnionConfigRange;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.loading.FMLPaths;

@EventBusSubscriber(bus = Bus.MOD)
public class ConfigBuilder {
	private static Map<String,ForgeConfigSpec.ConfigValue<?>> values = new HashMap<String,ForgeConfigSpec.ConfigValue<?>>();

	public static void load(Class<?> configClass) {
		if (configClass.isAnnotationPresent(UnionConfig.class)) {
			UnionConfig config = configClass.getAnnotation(UnionConfig.class);
			for (Field field : configClass.getFields()) {
				UnionConfigEntry configEntry = field.getAnnotation(UnionConfigEntry.class);
				if (configEntry != null) {
					try {
						field.set(null, values.get(config.name()+"="+configEntry.group()+"."+configEntry.name()).get());
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static boolean hasConfigType(Class<?> configClass, Type type) {
		if (configClass.isAnnotationPresent(UnionConfig.class)) {
			for (Field field : configClass.getFields()) {
				UnionConfigEntry configEntry = field.getAnnotation(UnionConfigEntry.class);
				if (configEntry != null) {
					if (configEntry.type() == type) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static void init(Class<?> configClass, ForgeConfigSpec.Builder server, ForgeConfigSpec.Builder common, ForgeConfigSpec.Builder client) {
		if (configClass.isAnnotationPresent(UnionConfig.class)) {
			UnionConfig config = configClass.getAnnotation(UnionConfig.class);
			for (Field field : configClass.getFields()) {
				UnionConfigEntry configEntry = field.getAnnotation(UnionConfigEntry.class);
				if (configEntry != null) {
					ForgeConfigSpec.Builder builder = configEntry.type() == Type.CLIENT ? client : configEntry.type() == Type.SERVER ? server : common;
					try {
						ForgeConfigSpec.ConfigValue<?> conf;
						String j = "\n###########################################";
						String h = j+"\n";
						if (field.isAnnotationPresent(UnionConfigComment.class)) {
							builder = builder.comment(h+field.getAnnotation(UnionConfigComment.class).comment()+j+ "\nDefault: "+field.get(null)+h);
						} else {
							builder = builder.comment(h+"Default: "+field.get(null)+h);
						}

						if (field.get(null) instanceof Boolean) { //Boolean
							conf = builder
									.define(configEntry.group()+"."+configEntry.name(), (Boolean)field.get(null));
						} else if (field.get(null) instanceof Enum) {
							conf = builder
									.define(configEntry.group()+"."+configEntry.name(), (Enum<?>)field.get(null));
						} else if (field.isAnnotationPresent(UnionConfigRange.class)) {
							UnionConfigRange range = field.getAnnotation(UnionConfigRange.class);
							Double min = (Double)range.min();
							Double max = (Double)range.max();

							if (field.get(null) instanceof Integer) {
								conf = builder
										.defineInRange(configEntry.group()+"."+configEntry.name(), (Integer)field.get(null), min.intValue(), max.intValue(), Integer.class);
							} else if (field.get(null) instanceof Float) {
								conf = builder
										.defineInRange(configEntry.group()+"."+configEntry.name(), (Float)field.get(null), min.floatValue(), max.floatValue(), Float.class);
							} else if (field.get(null) instanceof Long) {
								conf = builder
										.defineInRange(configEntry.group()+"."+configEntry.name(), (Long)field.get(null), min.longValue(), max.longValue(), Long.class);
							} else if (field.get(null) instanceof Short) {
								conf = builder
										.defineInRange(configEntry.group()+"."+configEntry.name(), (Short)field.get(null), min.shortValue(), max.shortValue(), Short.class);
							} else if (field.get(null) instanceof Byte) {
								conf = builder
										.defineInRange(configEntry.group()+"."+configEntry.name(), (Byte)field.get(null), min.byteValue(), max.byteValue(), Byte.class);
							} else {
								conf = builder
										.defineInRange(configEntry.group()+"."+configEntry.name(), (Double)field.get(null), min, max, Double.class);
							}
						} else {
							conf = builder
									.define(configEntry.group()+"."+configEntry.name(), field.get(null));
						}

						values.put(config.name()+"="+configEntry.group()+"."+configEntry.name(), conf);

					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private static final List<Class<?>> configs = Lists.newArrayList();

	private static final Map<Class<?>,ForgeConfigSpec.Builder> client_builder = Maps.newHashMap();
	public static final Map<Class<?>,ForgeConfigSpec> client_config = Maps.newHashMap();

	private static final Map<Class<?>,ForgeConfigSpec.Builder> common_builder = Maps.newHashMap();
	public static final Map<Class<?>,ForgeConfigSpec> common_config = Maps.newHashMap();

	private static final Map<Class<?>,ForgeConfigSpec.Builder> server_builder = Maps.newHashMap();
	public static final Map<Class<?>,ForgeConfigSpec> server_config = Maps.newHashMap();

	/**
	 * This is where all config classes are registered. 
	 * As long as the class has the annotation {@link UnionConfig} it will be registered
	 * @param configClass
	 */
	public static void registerConfig(Class<?> configClass) {
		if (configClass.isAnnotationPresent(UnionConfig.class)) {
			configs.add(configClass);
			client_builder.put(configClass, new ForgeConfigSpec.Builder());
			common_builder.put(configClass, new ForgeConfigSpec.Builder());
			server_builder.put(configClass, new ForgeConfigSpec.Builder());
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

	public static void registerConfigs() {
		for (Class<?> configClass : configs) {
			UnionConfig con = configClass.getAnnotation(UnionConfig.class);
			String name = con.folder().isEmpty() ? con.name() : con.folder()+"\\"+con.name();

			init(configClass, server_builder.get(configClass), common_builder.get(configClass), client_builder.get(configClass));
			server_config.put(configClass, server_builder.get(configClass).build());
			client_config.put(configClass, client_builder.get(configClass).build());
			common_config.put(configClass, common_builder.get(configClass).build());

			if (hasConfigType(configClass, ModConfig.Type.CLIENT))ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, client_config.get(configClass), name+(con.appendWithType()?".client":"")+".toml");
			if (hasConfigType(configClass, ModConfig.Type.COMMON))ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, common_config.get(configClass), name+(con.appendWithType()?".common":"")+".toml");
			if (hasConfigType(configClass, ModConfig.Type.SERVER))ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, server_config.get(configClass), name+(con.appendWithType()?".server":"")+".toml");
		}
	}

	public static void loadConfigs() {
		for (Class<?> configClass : configs) {
			UnionConfig con = configClass.getAnnotation(UnionConfig.class);

			if (hasConfigType(configClass, ModConfig.Type.CLIENT))loadConfig(client_config.get(configClass), FMLPaths.CONFIGDIR.get().toString() + (con.folder().isEmpty() ? "" : "\\"+con.folder()), con.name()+(con.appendWithType()?".client":"")+".toml");
			if (hasConfigType(configClass, ModConfig.Type.COMMON))loadConfig(common_config.get(configClass), FMLPaths.CONFIGDIR.get().toString() + (con.folder().isEmpty() ? "" : "\\"+con.folder()), con.name()+(con.appendWithType()?".common":"")+".toml");
			if (hasConfigType(configClass, ModConfig.Type.SERVER))loadConfig(server_config.get(configClass), FMLPaths.CONFIGDIR.get().toString() + (con.folder().isEmpty() ? "" : "\\"+con.folder()), con.name()+(con.appendWithType()?".server":"")+".toml");
		}
	}

	@SubscribeEvent
	public static void onReload(ModConfig.Reloading event) {
		for (Class<?> configClass : configs) {
			UnionConfig con = configClass.getAnnotation(UnionConfig.class);
			if (con.autoReload()) {
				load(configClass);
				System.out.println("Detected change in a "+con.name()+"'s config file. Reloading values");
			}
		}
	}

	@SubscribeEvent
	public static void onLoad(ModConfig.Loading event) {
		System.out.println("Detected change in a config file. Re-read and set all config values from file");
		for (Class<?> configClass : configs) {
			UnionConfig con = configClass.getAnnotation(UnionConfig.class);
			load(configClass);
			System.out.println("Loading "+con.name()+"'s config");
		}
	}
}
