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
public class ConfigClassBuilder {

	public static TranslatableComponent getConfigName(Class<?> configClass) {
		if (configClass.isAnnotationPresent(UnionConfig.class)) {
			UnionConfig config = configClass.getAnnotation(UnionConfig.class);
			if (config.translatableName().isEmpty())
				return new TranslatableComponent(config.name());
			else
				return new TranslatableComponent(config.translatableName());
		}
		return new TranslatableComponent("");
	}

	public static void read(Class<?> configClass, ModConfig.Type... readOnly) {
		List<ModConfig.Type> types = Lists.newArrayList(readOnly);
		if (configClass.isAnnotationPresent(UnionConfig.class)) {
			UnionConfig config = configClass.getAnnotation(UnionConfig.class);
			for (Field field : configClass.getFields()) {
				UnionConfig.Entry configEntry = field.getAnnotation(UnionConfig.Entry.class);
				if (configEntry != null && (readOnly.length == 0 || types.contains(configEntry.type()))) {
					try {
						if (ConfigBuilder.getConfigValue(config, configEntry).get() instanceof Double && field.get(null) instanceof Float) {
							field.set(null, ((Double)ConfigBuilder.getConfigValue(config, configEntry).get()).floatValue());
						} else {
							field.set(null, ConfigBuilder.getConfigValue(config, configEntry).get());
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
				if (configEntry != null && ConfigBuilder.getConfigValue(config, configEntry) != null) {
					try {
						((ForgeConfigSpec.ConfigValue<Object>)ConfigBuilder.getConfigValue(config, configEntry)).set(field.get(null));
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
						List<Component> saved_comment = new ArrayList<Component>();
						if (field.isAnnotationPresent(UnionConfig.Comment.class)) {
							String config_comment = field.getAnnotation(UnionConfig.Comment.class).comment()[0];

							if (field.getAnnotation(UnionConfig.Comment.class).comment().length > 1) {
								for (int i = 1; i < field.getAnnotation(UnionConfig.Comment.class).comment().length; i++) {
									config_comment+="\n"+field.getAnnotation(UnionConfig.Comment.class).comment()[i];
								}
							}

							comment = h+config_comment+k+enumComment+"Default: "+field.get(null)+k;

							for (String s : config_comment.split("\n")) {
								saved_comment.add(new TextComponent(s).withStyle(ChatFormatting.AQUA));
							}
							for (String s : enumComment.split("\n")) {
								saved_comment.add(new TextComponent(s).withStyle(ChatFormatting.YELLOW));
							}
							saved_comment.add(new TextComponent("Default: "+field.get(null)).withStyle(ChatFormatting.GREEN));
						} else {
							comment = h+enumComment+"Default: "+field.get(null)+k;

							for (String s : enumComment.split("\n")) {
								saved_comment.add(new TextComponent(s).withStyle(ChatFormatting.YELLOW));
							}
							saved_comment.add(new TextComponent("Default: "+field.get(null)).withStyle(ChatFormatting.GREEN));
						}
						ForgeConfigSpec.Builder commented_builder = builder.comment(comment);

						Double min = 0.0d;
						Double max = 0.0d;

						if (field.get(null) instanceof Boolean) { //Boolean
							conf = commented_builder
									.define(ConfigBuilder.configName(configEntry, "Boolean"), (Boolean)field.get(null));
						} else if (field.get(null) instanceof Enum) {
							Enum<?> defaultValue = (Enum<?>)field.get(null);

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
						} else if (field.get(null) instanceof String) {
							conf = commented_builder
									.define(ConfigBuilder.configName(configEntry, "String"), (String)field.get(null));
						} else if (field.isAnnotationPresent(UnionConfig.Range.class)) {
							UnionConfig.Range range = field.getAnnotation(UnionConfig.Range.class);
							min = (Double)range.min();
							max = (Double)range.max();

							if (field.get(null) instanceof Integer) {
								conf = commented_builder
										.defineInRange(ConfigBuilder.configName(configEntry, "Integer"), (Integer)field.get(null), min.intValue(), max.intValue(), Integer.class);
							} else if (field.get(null) instanceof Float) {
								conf = commented_builder
										.defineInRange(ConfigBuilder.configName(configEntry, "Float"), (Float)field.get(null), min.floatValue(), max.floatValue(), Float.class);
							} else if (field.get(null) instanceof Long) {
								conf = commented_builder
										.defineInRange(ConfigBuilder.configName(configEntry, "Long"), (Long)field.get(null), min.longValue(), max.longValue(), Long.class);
							} else if (field.get(null) instanceof Short) {
								conf = commented_builder
										.defineInRange(ConfigBuilder.configName(configEntry, "Short"), (Short)field.get(null), min.shortValue(), max.shortValue(), Short.class);
							} else if (field.get(null) instanceof Byte) {
								conf = commented_builder
										.defineInRange(ConfigBuilder.configName(configEntry, "Byte"), (Byte)field.get(null), min.byteValue(), max.byteValue(), Byte.class);
							} else {
								conf = commented_builder
										.defineInRange(ConfigBuilder.configName(configEntry, "Double"), (Double)field.get(null), min, max, Double.class);
							}
						} else {
							conf = commented_builder
									.define(ConfigBuilder.configName(configEntry), field.get(null));
						}

						ConfigBuilder.values.put(config.name()+"="+ConfigBuilder.configName(configEntry), new Holder(conf, saved_comment, field.isAnnotationPresent(UnionConfig.Slider.class), min, max));
						config_initialization.put(configClass, true);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void registerConfigurations(Class<?> configClass) {
		UnionConfig con = configClass.getAnnotation(UnionConfig.class);
		String name = con.folder().isEmpty() ? con.name() : con.folder()+"\\"+con.name();

		init(configClass, server_builder.get(configClass), common_builder.get(configClass), client_builder.get(configClass));
		server_config.put(configClass, server_builder.get(configClass).build());
		client_config.put(configClass, client_builder.get(configClass).build());
		common_config.put(configClass, common_builder.get(configClass).build());

		if (hasConfigType(configClass, ModConfig.Type.CLIENT))ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, client_config.get(configClass), name+(con.appendWithType()?"-client":"")+".toml");
		if (hasConfigType(configClass, ModConfig.Type.COMMON))ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, common_config.get(configClass), name+(con.appendWithType()?"-common":"")+".toml");
		if (hasConfigType(configClass, ModConfig.Type.SERVER))ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, server_config.get(configClass), name+(con.appendWithType()?"-server":"")+".toml");
	}

	public static void loadConfigs(Class<?> configClass) {
		UnionConfig con = configClass.getAnnotation(UnionConfig.class);

		if (hasConfigType(configClass, ModConfig.Type.CLIENT))ConfigBuilder.loadConfig(client_config.get(configClass), FMLPaths.CONFIGDIR.get().toString() + (con.folder().isEmpty() ? "" : "\\"+con.folder()), con.name()+(con.appendWithType()?"-client":"")+".toml");
		if (hasConfigType(configClass, ModConfig.Type.COMMON))ConfigBuilder.loadConfig(common_config.get(configClass), FMLPaths.CONFIGDIR.get().toString() + (con.folder().isEmpty() ? "" : "\\"+con.folder()), con.name()+(con.appendWithType()?"-common":"")+".toml");
		if (hasConfigType(configClass, ModConfig.Type.SERVER))ConfigBuilder.loadConfig(server_config.get(configClass), FMLPaths.CONFIGDIR.get().toString() + (con.folder().isEmpty() ? "" : "\\"+con.folder()), con.name()+(con.appendWithType()?"-server":"")+".toml");
	}

	static final List<Class<?>> configs = Lists.newArrayList();
	public static final Map<Class<?>,Boolean> config_initialization = Maps.newHashMap();

	static final Map<Class<?>,ForgeConfigSpec.Builder> client_builder = Maps.newHashMap();
	public static final Map<Class<?>,ForgeConfigSpec> client_config = Maps.newHashMap();

	static final Map<Class<?>,ForgeConfigSpec.Builder> common_builder = Maps.newHashMap();
	public static final Map<Class<?>,ForgeConfigSpec> common_config = Maps.newHashMap();

	static final Map<Class<?>,ForgeConfigSpec.Builder> server_builder = Maps.newHashMap();
	public static final Map<Class<?>,ForgeConfigSpec> server_config = Maps.newHashMap();
}
