package com.stereowalker.unionlib.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.stereowalker.unionlib.config.ConfigBuilder.Holder;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.loading.FMLPaths;

@EventBusSubscriber(bus = Bus.MOD)
public class ConfigObjectBuilder {
	
	public static TranslatableComponent getConfigName(ConfigObject configObject) {
		if (configObject.getClass().isAnnotationPresent(UnionConfig.class)) {
			UnionConfig config = configObject.getClass().getAnnotation(UnionConfig.class);
			if (config.translatableName().isEmpty())
				return new TranslatableComponent(config.name());
			else
				return new TranslatableComponent(config.translatableName());
		}
		return new TranslatableComponent("");
	}
	
	public static void read(ConfigObject configObject, ModConfig.Type... readOnly) {
		List<ModConfig.Type> types = Lists.newArrayList(readOnly);
		if (configObject.getClass().isAnnotationPresent(UnionConfig.class)) {
			UnionConfig config = configObject.getClass().getAnnotation(UnionConfig.class);
			for (Field field : configObject.getClass().getFields()) {
				UnionConfig.Entry configEntry = field.getAnnotation(UnionConfig.Entry.class);
				if (configEntry != null && (readOnly.length == 0 || types.contains(configEntry.type()))) {
					try {
						if (ConfigBuilder.getConfigValue(config, configEntry).get() instanceof Double && field.get(configObject) instanceof Float) {
							field.set(configObject, ((Double)ConfigBuilder.getConfigValue(config, configEntry).get()).floatValue());
						} else {
							field.set(configObject, ConfigBuilder.getConfigValue(config, configEntry).get());
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void write(ConfigObject configObject) {
		if (configObject.getClass().isAnnotationPresent(UnionConfig.class)) {
			UnionConfig config = configObject.getClass().getAnnotation(UnionConfig.class);
			for (Field field : configObject.getClass().getFields()) {
				UnionConfig.Entry configEntry = field.getAnnotation(UnionConfig.Entry.class);
				if (configEntry != null && ConfigBuilder.getConfigValue(config, configEntry) != null) {
					try {
						((ForgeConfigSpec.ConfigValue<Object>)ConfigBuilder.getConfigValue(config, configEntry)).set(field.get(configObject));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static boolean hasConfigType(ConfigObject configObject, Type type) {
		if (configObject.getClass().isAnnotationPresent(UnionConfig.class)) {
			for (Field field : configObject.getClass().getFields()) {
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

	private static void init(ConfigObject configObject, ForgeConfigSpec.Builder server, ForgeConfigSpec.Builder common, ForgeConfigSpec.Builder client) {
		if (configObject.getClass().isAnnotationPresent(UnionConfig.class)) {
			UnionConfig config = configObject.getClass().getAnnotation(UnionConfig.class);
			for (Field field : configObject.getClass().getFields()) {
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
						if (field.get(configObject) instanceof CommentedEnum<?> && field.get(configObject) instanceof Enum<?>) {
							enumComment =  ((CommentedEnum<?>)field.get(configObject)).getConfigComment();
						}
						enumComment+="\n";
						
						ForgeConfigSpec.ConfigValue<?> conf;
						String j = "\n###########################################";
						String k = "\n-------------------------------------------";
						String h = j+"\n";
						
						String comment = "";
						List<Component> saved_comment = new ArrayList<Component>();
						if (field.isAnnotationPresent(UnionConfig.Comment.class)) {
							String config_comment = field.getAnnotation(UnionConfig.Comment.class).comment()[0];
							
							if (field.getAnnotation(UnionConfig.Comment.class).comment().length > 1) {
								for (int i = 1; i < field.getAnnotation(UnionConfig.Comment.class).comment().length; i++) {
									config_comment+="\n"+field.getAnnotation(UnionConfig.Comment.class).comment()[i];
								}
							}
							
							comment = h+config_comment+k+enumComment+"Default: "+field.get(configObject)+k;
							
							for (String s : config_comment.split("\n")) {
								saved_comment.add(new TextComponent(s).withStyle(ChatFormatting.AQUA));
							}
							for (String s : enumComment.split("\n")) {
								saved_comment.add(new TextComponent(s).withStyle(ChatFormatting.YELLOW));
							}
							saved_comment.add(new TextComponent("Default: "+field.get(configObject)).withStyle(ChatFormatting.GREEN));
						} else {
							comment = h+enumComment+"Default: "+field.get(configObject)+k;
							
							for (String s : enumComment.split("\n")) {
								saved_comment.add(new TextComponent(s).withStyle(ChatFormatting.YELLOW));
							}
							saved_comment.add(new TextComponent("Default: "+field.get(configObject)).withStyle(ChatFormatting.GREEN));
						}
						ForgeConfigSpec.Builder commented_builder = builder.comment(comment);
						
						Double min = 0.0d;
						Double max = 0.0d;

						if (field.get(configObject) instanceof Boolean) { //Boolean
							conf = commented_builder
									.define(ConfigBuilder.configName(configEntry, "Boolean"), (Boolean)field.get(configObject));
						} else if (field.get(configObject) instanceof Enum) {
							Enum<?> defaultValue = (Enum<?>)field.get(configObject);
							
							Collection<?> acceptableValues = (Collection<?>) Arrays.asList(defaultValue.getDeclaringClass().getEnumConstants());
							
							conf = commented_builder
									.defineEnum(ConfigBuilder.split(ConfigBuilder.configName(configEntry, "Enum")),
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
						} else if (field.get(configObject) instanceof String) {
							conf = commented_builder
									.define(ConfigBuilder.configName(configEntry, "String"), (String)field.get(configObject));
						} else if (field.isAnnotationPresent(UnionConfig.Range.class)) {
							UnionConfig.Range range = field.getAnnotation(UnionConfig.Range.class);
							min = (Double)range.min();
							max = (Double)range.max();

							if (field.get(configObject) instanceof Integer) {
								conf = commented_builder
										.defineInRange(ConfigBuilder.configName(configEntry, "Integer"), (Integer)field.get(configObject), min.intValue(), max.intValue(), Integer.class);
							} else if (field.get(configObject) instanceof Float) {
								conf = commented_builder
										.defineInRange(ConfigBuilder.configName(configEntry, "Float"), (Float)field.get(configObject), min.floatValue(), max.floatValue(), Float.class);
							} else if (field.get(configObject) instanceof Long) {
								conf = commented_builder
										.defineInRange(ConfigBuilder.configName(configEntry, "Long"), (Long)field.get(configObject), min.longValue(), max.longValue(), Long.class);
							} else if (field.get(configObject) instanceof Short) {
								conf = commented_builder
										.defineInRange(ConfigBuilder.configName(configEntry, "Short"), (Short)field.get(configObject), min.shortValue(), max.shortValue(), Short.class);
							} else if (field.get(configObject) instanceof Byte) {
								conf = commented_builder
										.defineInRange(ConfigBuilder.configName(configEntry, "Byte"), (Byte)field.get(configObject), min.byteValue(), max.byteValue(), Byte.class);
							} else {
								conf = commented_builder
										.defineInRange(ConfigBuilder.configName(configEntry, "Double"), (Double)field.get(configObject), min, max, Double.class);
							}
						} else {
							conf = commented_builder
									.define(ConfigBuilder.configName(configEntry), field.get(configObject));
						}

						ConfigBuilder.putValue(type, config.name()+"="+ConfigBuilder.configName(configEntry), new Holder(conf, field.get(configObject), saved_comment, field.isAnnotationPresent(UnionConfig.Slider.class), min, max));
						config_initialization.put(configObject, true);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void registerConfigurations(ConfigObject configObject) {
		UnionConfig con = configObject.getClass().getAnnotation(UnionConfig.class);
		String name = con.folder().isEmpty() ? con.name() : con.folder()+"\\"+con.name();

		init(configObject, server_builder.get(configObject), common_builder.get(configObject), client_builder.get(configObject));
		server_config.put(configObject, server_builder.get(configObject).build());
		client_config.put(configObject, client_builder.get(configObject).build());
		common_config.put(configObject, common_builder.get(configObject).build());

		if (hasConfigType(configObject, ModConfig.Type.CLIENT))ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, client_config.get(configObject), name+(con.appendWithType()?"-client":"")+".toml");
		if (hasConfigType(configObject, ModConfig.Type.COMMON))ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, common_config.get(configObject), name+(con.appendWithType()?"-common":"")+".toml");
		if (hasConfigType(configObject, ModConfig.Type.SERVER))ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, server_config.get(configObject), name+(con.appendWithType()?"-server":"")+".toml");
	}

	public static void loadConfigs(ConfigObject configObject) {
		UnionConfig con = configObject.getClass().getAnnotation(UnionConfig.class);

		if (hasConfigType(configObject, ModConfig.Type.CLIENT))ConfigBuilder.loadConfig(client_config.get(configObject), FMLPaths.CONFIGDIR.get().toString() + (con.folder().isEmpty() ? "" : "\\"+con.folder()), con.name()+(con.appendWithType()?"-client":"")+".toml");
		if (hasConfigType(configObject, ModConfig.Type.COMMON))ConfigBuilder.loadConfig(common_config.get(configObject), FMLPaths.CONFIGDIR.get().toString() + (con.folder().isEmpty() ? "" : "\\"+con.folder()), con.name()+(con.appendWithType()?"-common":"")+".toml");
		if (hasConfigType(configObject, ModConfig.Type.SERVER))ConfigBuilder.loadConfig(server_config.get(configObject), FMLPaths.CONFIGDIR.get().toString() + (con.folder().isEmpty() ? "" : "\\"+con.folder()), con.name()+(con.appendWithType()?"-server":"")+".toml");
	}

	static final List<ConfigObject> configs = Lists.newArrayList();
	public static final Map<ConfigObject,Boolean> config_initialization = Maps.newHashMap();

	static final Map<ConfigObject,ForgeConfigSpec.Builder> client_builder = Maps.newHashMap();
	public static final Map<ConfigObject,ForgeConfigSpec> client_config = Maps.newHashMap();

	static final Map<ConfigObject,ForgeConfigSpec.Builder> common_builder = Maps.newHashMap();
	public static final Map<ConfigObject,ForgeConfigSpec> common_config = Maps.newHashMap();

	static final Map<ConfigObject,ForgeConfigSpec.Builder> server_builder = Maps.newHashMap();
	public static final Map<ConfigObject,ForgeConfigSpec> server_config = Maps.newHashMap();
}
