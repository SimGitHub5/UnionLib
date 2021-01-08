package net.minecraft.server.dedicated;

import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;

public class ServerProperties extends PropertyManager<ServerProperties> {
   public boolean onlineMode = this.registerBool("online-mode", true);
   public boolean preventProxyConnections = this.registerBool("prevent-proxy-connections", false);
   public String serverIp = this.registerString("server-ip", "");
   public boolean spawnAnimals = this.registerBool("spawn-animals", true);
   public boolean spawnNPCs = this.registerBool("spawn-npcs", true);
   public boolean allowPvp = this.registerBool("pvp", true);
   public boolean allowFlight = this.registerBool("allow-flight", false);
   public String resourcePack = this.registerString("resource-pack", "");
   public String motd = this.registerString("motd", "A Minecraft Server");
   public boolean forceGamemode = this.registerBool("force-gamemode", false);
   public boolean enforceWhitelist = this.registerBool("enforce-whitelist", false);
   public Difficulty difficulty = this.func_218983_a("difficulty", enumConverter(Difficulty::byId, Difficulty::byName), Difficulty::getTranslationKey, Difficulty.EASY);
   public GameType gamemode = this.func_218983_a("gamemode", enumConverter(GameType::getByID, GameType::getByName), GameType::getName, GameType.SURVIVAL);
   public String worldName = this.registerString("level-name", "world");
   public int serverPort = this.registerInt("server-port", 25565);
   public int maxBuildHeight = this.func_218962_a("max-build-height", (p_218987_0_) -> {
      return MathHelper.clamp((p_218987_0_ + 8) / 16 * 16, 64, 256);
   }, 256);
   public Boolean announceAdvancements = this.func_218978_b("announce-player-achievements");
   public boolean enableQuery = this.registerBool("enable-query", false);
   public int queryPort = this.registerInt("query.port", 25565);
   public boolean enableRcon = this.registerBool("enable-rcon", false);
   public int rconPort = this.registerInt("rcon.port", 25575);
   public String rconPassword = this.registerString("rcon.password", "");
   /** Deprecated. Use resourcePackSha1 instead. */
   public String resourcePackHash = this.func_218980_a("resource-pack-hash");
   public String resourcePackSha1 = this.registerString("resource-pack-sha1", "");
   public boolean hardcore = this.registerBool("hardcore", false);
   public boolean allowNether = this.registerBool("allow-nether", true);
   public boolean spawnMonsters = this.registerBool("spawn-monsters", true);
   public boolean field_218993_F;
   public boolean useNativeTransport;
   public boolean enableCommandBlock;
   public int spawnProtection;
   public int opPermissionLevel;
   public int functionPermissionLevel;
   public long maxTickTime;
   public int rateLimit;
   public int viewDistance;
   public int maxPlayers;
   public int networkCompressionThreshold;
   public boolean broadcastRconToOps;
   public boolean broadcastConsoleToOps;
   public int maxWorldSize;
   public boolean field_241078_O_;
   public boolean field_241079_P_;
   public boolean field_241080_Q_;
   public int field_241081_R_;
   public String field_244715_T;
   public PropertyManager<ServerProperties>.Property<Integer> playerIdleTimeout;
   public PropertyManager<ServerProperties>.Property<Boolean> whitelistEnabled;
   public DimensionGeneratorSettings field_241082_U_;

   public ServerProperties(Properties p_i242099_1_, DynamicRegistries p_i242099_2_) {
      super(p_i242099_1_);
      if (this.registerBool("snooper-enabled", true)) {
      }

      this.field_218993_F = false;
      this.useNativeTransport = this.registerBool("use-native-transport", true);
      this.enableCommandBlock = this.registerBool("enable-command-block", false);
      this.spawnProtection = this.registerInt("spawn-protection", 16);
      this.opPermissionLevel = this.registerInt("op-permission-level", 4);
      this.functionPermissionLevel = this.registerInt("function-permission-level", 2);
      this.maxTickTime = this.func_218967_a("max-tick-time", TimeUnit.MINUTES.toMillis(1L));
      this.rateLimit = this.registerInt("rate-limit", 0);
      this.viewDistance = this.registerInt("view-distance", 10);
      this.maxPlayers = this.registerInt("max-players", 20);
      this.networkCompressionThreshold = this.registerInt("network-compression-threshold", 256);
      this.broadcastRconToOps = this.registerBool("broadcast-rcon-to-ops", true);
      this.broadcastConsoleToOps = this.registerBool("broadcast-console-to-ops", true);
      this.maxWorldSize = this.func_218962_a("max-world-size", (p_218986_0_) -> {
         return MathHelper.clamp(p_218986_0_, 1, 29999984);
      }, 29999984);
      this.field_241078_O_ = this.registerBool("sync-chunk-writes", true);
      this.field_241079_P_ = this.registerBool("enable-jmx-monitoring", false);
      this.field_241080_Q_ = this.registerBool("enable-status", true);
      this.field_241081_R_ = this.func_218962_a("entity-broadcast-range-percentage", (p_241083_0_) -> {
         return MathHelper.clamp(p_241083_0_, 10, 1000);
      }, 100);
      this.field_244715_T = this.registerString("text-filtering-config", "");
      this.playerIdleTimeout = this.func_218974_b("player-idle-timeout", 0);
      this.whitelistEnabled = this.func_218961_b("white-list", false);
      this.field_241082_U_ = DimensionGeneratorSettings.func_242753_a(p_i242099_2_, p_i242099_1_);
   }

   public static ServerProperties func_244380_a(DynamicRegistries registries, Path p_244380_1_) {
      return new ServerProperties(load(p_244380_1_), registries);
   }

   protected ServerProperties func_241881_b(DynamicRegistries p_241881_1_, Properties p_241881_2_) {
      return new ServerProperties(p_241881_2_, p_241881_1_);
   }
}
