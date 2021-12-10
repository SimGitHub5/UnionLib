package com.stereowalker.unionlib.mixin.client;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.platform.GlDebug;
import com.stereowalker.unionlib.UnionLib;

@Mixin(GlDebug.class)
public abstract class GlDebugMixin {
	@Redirect(method = "printDebugLog", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;)V"))
	private static void noDebug(Logger logger, String message, Object p0) {
		if (!UnionLib.CONFIG.no_gl_debug) {
			logger.info(message, p0);
		}
	}
}
