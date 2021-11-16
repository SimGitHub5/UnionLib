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

import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class ConfigBuilder {
	static Map<String,Holder> values = new HashMap<String,Holder>();
	
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
		if (configClass.isAnnotationPresent(UnionConfig.class) && !ConfigClassBuilder.configs.contains(configClass)) {
			UnionConfig con = configClass.getAnnotation(UnionConfig.class);
			System.out.println("Registered the config for "+con.name());
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
			System.out.println("Registered the config for "+con.name());
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
	
	public static void load(ModConfig.Type... exceptions) {
		System.out.println("Loading all values from the config files into their respective configuration variables");
		for (Class<?> configClass : ConfigClassBuilder.configs) {
			UnionConfig con = configClass.getAnnotation(UnionConfig.class);
			ConfigClassBuilder.read(configClass, exceptions);
			System.out.println("Loading "+con.name()+"'s config");
		}
		for (ConfigObject configObject : ConfigObjectBuilder.configs) {
			UnionConfig con = configObject.getClass().getAnnotation(UnionConfig.class);
			ConfigObjectBuilder.read(configObject, exceptions);
			System.out.println("Loading "+con.name()+"'s config");
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
    			System.out.println("Loading All Server Config Files");
    			load(ModConfig.Type.SERVER);
    		} else {
    			System.out.println("Loading All Client Config Files");
    			load(ModConfig.Type.CLIENT);
    		}
    	}
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
