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

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
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
	private static Map<String,Holder> values = new HashMap<String,Holder>();
	
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

	private static ForgeConfigSpec.ConfigValue<?> getConfigValue(UnionConfig config, UnionConfig.Entry configEntry){
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
	
	public static void read(Class<?> configClass) {
		if (configClass.isAnnotationPresent(UnionConfig.class)) {
			UnionConfig config = configClass.getAnnotation(UnionConfig.class);
			for (Field field : configClass.getFields()) {
				UnionConfig.Entry configEntry = field.getAnnotation(UnionConfig.Entry.class);
				if (configEntry != null) {
					try {
						if (getConfigValue(config, configEntry).get() instanceof Double && field.get(null) instanceof Float) {
							field.set(null, ((Double)getConfigValue(config, configEntry).get()).floatValue());
						} else {
							field.set(null, getConfigValue(config, configEntry).get());
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void write(Class<?> configClass) {
		if (configClass.isAnnotationPresent(UnionConfig.class)) {
			UnionConfig config = configClass.getAnnotation(UnionConfig.class);
			for (Field field : configClass.getFields()) {
				UnionConfig.Entry configEntry = field.getAnnotation(UnionConfig.Entry.class);
				if (configEntry != null && getConfigValue(config, configEntry) != null) {
					try {
						((ForgeConfigSpec.ConfigValue<Object>)getConfigValue(config, configEntry)).set(field.get(null));
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
				UnionConfig.Entry configEntry = field.getAnnotation(UnionConfig.Entry.class);
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
				UnionConfig.Entry configEntry = field.getAnnotation(UnionConfig.Entry.class);
				if (configEntry != null) {
					Type type = configEntry.type() != null ? configEntry.type() : Type.COMMON;
					
					 ForgeConfigSpec.Builder builder = common;
					
					if (type == Type.CLIENT) {
						builder = client;
					} else if (type == Type.SERVER) {
						builder = server;
					} else {
						builder = common;
					}
					
					try {
						String enumComment = "";
						if (field.get(null) instanceof CommentedEnum<?> && field.get(null) instanceof Enum<?>) {
							enumComment =  ((CommentedEnum<?>)field.get(null)).getConfigComment();
						}
						enumComment+="\n";
						
						ForgeConfigSpec.ConfigValue<?> conf;
						String j = "\n###########################################";
						String k = "\n-------------------------------------------";
						String h = j+"\n";
						
						String comment = "";
						List<ITextComponent> saved_comment = new ArrayList<ITextComponent>();
						if (field.isAnnotationPresent(UnionConfig.Comment.class)) {
							String config_comment = field.getAnnotation(UnionConfig.Comment.class).comment()[0];
							
							if (field.getAnnotation(UnionConfig.Comment.class).comment().length > 1) {
								for (int i = 1; i < field.getAnnotation(UnionConfig.Comment.class).comment().length; i++) {
									config_comment+="\n"+field.getAnnotation(UnionConfig.Comment.class).comment()[i];
								}
							}
							
							comment = h+config_comment+k+enumComment+"Default: "+field.get(null)+k;
							
							for (String s : config_comment.split("\n")) {
								saved_comment.add(new StringTextComponent(s).mergeStyle(TextFormatting.AQUA));
							}
							for (String s : enumComment.split("\n")) {
								saved_comment.add(new StringTextComponent(s).mergeStyle(TextFormatting.YELLOW));
							}
							saved_comment.add(new StringTextComponent("Default: "+field.get(null)).mergeStyle(TextFormatting.GREEN));
						} else {
							comment = h+enumComment+"Default: "+field.get(null)+k;
							
							for (String s : enumComment.split("\n")) {
								saved_comment.add(new StringTextComponent(s).mergeStyle(TextFormatting.YELLOW));
							}
							saved_comment.add(new StringTextComponent("Default: "+field.get(null)).mergeStyle(TextFormatting.GREEN));
						}
						ForgeConfigSpec.Builder commented_builder = builder.comment(comment);
						
						Double min = 0.0d;
						Double max = 0.0d;

						if (field.get(null) instanceof Boolean) { //Boolean
							conf = commented_builder
									.define(configName(configEntry, "Boolean"), (Boolean)field.get(null));
						} else if (field.get(null) instanceof Enum) {
							Enum<?> defaultValue = (Enum<?>)field.get(null);
							
							Collection<?> acceptableValues = (Collection<?>) Arrays.asList(defaultValue.getDeclaringClass().getEnumConstants());
							
							conf = commented_builder
									.defineEnum(split(configName(configEntry, "Enum")),
											Enum.valueOf(defaultValue.getDeclaringClass(), defaultValue.name()), EnumGetMethod.NAME_IGNORECASE, obj -> {
								                if (obj instanceof Enum) {
								                    return acceptableValues.contains(obj);
								                }
								                if (obj == null) {
								                    return false;
								                }
								                try {
								                    return acceptableValues.contains(EnumGetMethod.NAME_IGNORECASE.get(obj, defaultValue.getDeclaringClass()));
								                } catch (IllegalArgumentException | ClassCastException e) {
								                    return false;
								                }
								            });
						} else if (field.get(null) instanceof String) {
							conf = commented_builder
									.define(configName(configEntry, "String"), (String)field.get(null));
						} else if (field.isAnnotationPresent(UnionConfig.Range.class)) {
							UnionConfig.Range range = field.getAnnotation(UnionConfig.Range.class);
							min = (Double)range.min();
							max = (Double)range.max();

							if (field.get(null) instanceof Integer) {
								conf = commented_builder
										.defineInRange(configName(configEntry, "Integer"), (Integer)field.get(null), min.intValue(), max.intValue(), Integer.class);
							} else if (field.get(null) instanceof Float) {
								conf = commented_builder
										.defineInRange(configName(configEntry, "Float"), (Float)field.get(null), min.floatValue(), max.floatValue(), Float.class);
							} else if (field.get(null) instanceof Long) {
								conf = commented_builder
										.defineInRange(configName(configEntry, "Long"), (Long)field.get(null), min.longValue(), max.longValue(), Long.class);
							} else if (field.get(null) instanceof Short) {
								conf = commented_builder
										.defineInRange(configName(configEntry, "Short"), (Short)field.get(null), min.shortValue(), max.shortValue(), Short.class);
							} else if (field.get(null) instanceof Byte) {
								conf = commented_builder
										.defineInRange(configName(configEntry, "Byte"), (Byte)field.get(null), min.byteValue(), max.byteValue(), Byte.class);
							} else {
								conf = commented_builder
										.defineInRange(configName(configEntry, "Double"), (Double)field.get(null), min, max, Double.class);
							}
						} else {
							conf = commented_builder
									.define(configName(configEntry), field.get(null));
						}

						values.put(config.name()+"="+configName(configEntry), new Holder(conf, saved_comment, field.isAnnotationPresent(UnionConfig.Slider.class), min, max));
						config_initialization.put(configClass, true);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private static final List<Class<?>> configs = Lists.newArrayList();
	public static final Map<Class<?>,Boolean> config_initialization = Maps.newHashMap();

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
			UnionConfig con = configClass.getAnnotation(UnionConfig.class);
			System.out.println("Registered the config for "+con.name());
			configs.add(configClass);
			client_builder.put(configClass, new ForgeConfigSpec.Builder());
			common_builder.put(configClass, new ForgeConfigSpec.Builder());
			server_builder.put(configClass, new ForgeConfigSpec.Builder());
			
			registerConfigurations(configClass);
			loadConfigs(configClass);
			read(configClass);
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

	public static void registerConfigurations(Class<?> configClass) {
//		for (Class<?> configClass : configs) {
			UnionConfig con = configClass.getAnnotation(UnionConfig.class);
			String name = con.folder().isEmpty() ? con.name() : con.folder()+"\\"+con.name();

			init(configClass, server_builder.get(configClass), common_builder.get(configClass), client_builder.get(configClass));
			server_config.put(configClass, server_builder.get(configClass).build());
			client_config.put(configClass, client_builder.get(configClass).build());
			common_config.put(configClass, common_builder.get(configClass).build());

			if (hasConfigType(configClass, ModConfig.Type.CLIENT))ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, client_config.get(configClass), name+(con.appendWithType()?".client":"")+".toml");
			if (hasConfigType(configClass, ModConfig.Type.COMMON))ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, common_config.get(configClass), name+(con.appendWithType()?".common":"")+".toml");
			if (hasConfigType(configClass, ModConfig.Type.SERVER))ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, server_config.get(configClass), name+(con.appendWithType()?".server":"")+".toml");
//		}
	}

	public static void loadConfigs(Class<?> configClass) {
//		for (Class<?> configClass : configs) {
			UnionConfig con = configClass.getAnnotation(UnionConfig.class);

			if (hasConfigType(configClass, ModConfig.Type.CLIENT))loadConfig(client_config.get(configClass), FMLPaths.CONFIGDIR.get().toString() + (con.folder().isEmpty() ? "" : "\\"+con.folder()), con.name()+(con.appendWithType()?".client":"")+".toml");
			if (hasConfigType(configClass, ModConfig.Type.COMMON))loadConfig(common_config.get(configClass), FMLPaths.CONFIGDIR.get().toString() + (con.folder().isEmpty() ? "" : "\\"+con.folder()), con.name()+(con.appendWithType()?".common":"")+".toml");
			if (hasConfigType(configClass, ModConfig.Type.SERVER))loadConfig(server_config.get(configClass), FMLPaths.CONFIGDIR.get().toString() + (con.folder().isEmpty() ? "" : "\\"+con.folder()), con.name()+(con.appendWithType()?".server":"")+".toml");
//		}
	}

	@SubscribeEvent
	public static void onReload(ModConfig.ModConfigEvent event) {
		reload();
		System.out.println("From the event itself");
	}

	@SubscribeEvent
	public static void onReload(ModConfig.Reloading event) {
		reload();
	}
	
	public static void reload() {
		for (Class<?> configClass : configs) {
			UnionConfig con = configClass.getAnnotation(UnionConfig.class);
			if (con.autoReload()) {
				read(configClass);
				System.out.println("Detected change in "+con.name()+"'s config file. Reloading values");
			}
		}
	}
	
	public static void load() {
		System.out.println("Loading all values from the config files into their respective configuration variables");
		for (Class<?> configClass : configs) {
			UnionConfig con = configClass.getAnnotation(UnionConfig.class);
			read(configClass);
			System.out.println("Loading "+con.name()+"'s config");
		}
	}

	@SubscribeEvent
	public static void onLoad(ModConfig.Loading event) {
		load();
	}
	
    private static final Splitter DOT_SPLITTER = Splitter.on(".");
    private static List<String> split(String path)
    {
        return Lists.newArrayList(DOT_SPLITTER.split(path));
    }
    
    public static class Holder {
    	protected final ForgeConfigSpec.ConfigValue<?> value;
    	protected final List<ITextComponent> comments;
    	protected final	boolean usesSider;
    	protected final double min;
    	protected final double max;
    	public Holder(final ForgeConfigSpec.ConfigValue<?> configvalue, final List<ITextComponent> comments, final boolean usesSider, final double min, final double max) {
    		this.comments = comments;
    		this.value = configvalue;
    		this.usesSider = usesSider;
    		this.min = min;
    		this.max = max;
		}
		public ForgeConfigSpec.ConfigValue<?> getValue() {
			return value;
		}
		public List<ITextComponent> getComments() {
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
