package com.stereowalker.unionlib.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.stereowalker.unionlib.event.StructureAddedEvent;
import com.stereowalker.unionlib.event.StructurePieceAddedEvent;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraftforge.common.MinecraftForge;

@Mixin(StructureStart.class)
public class StructureStartMixin {
	@Shadow @Final private PiecesContainer f_192654_;


	@Redirect(method = "placeInChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/StructurePiece;m_183269_(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/StructureFeatureManager;Lnet/minecraft/world/level/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/core/BlockPos;)V"))
	private void structurePieceGenerated(StructurePiece structurePiece, WorldGenLevel serverWorldAccess, StructureFeatureManager structureAccessor, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
		ServerLevel world;

		if (serverWorldAccess instanceof ServerLevel) {
			world = (ServerLevel) serverWorldAccess;
		} else {
			world = ((WorldGenRegion) serverWorldAccess).level;
		}

		MinecraftForge.EVENT_BUS.post(new StructurePieceAddedEvent(structurePiece, world));
		structurePiece.m_183269_(serverWorldAccess, structureAccessor, chunkGenerator, random, boundingBox, chunkPos, blockPos);
	}

	@Inject(method = "placeInChunk", at = @At("RETURN"))
	private void structureGenerated(WorldGenLevel serverWorldAccess, StructureFeatureManager structureAccessor, ChunkGenerator chunkGenerator, Random random, BoundingBox blockBox, ChunkPos chunkPos, CallbackInfo ci) {
		ServerLevel world;

		if (serverWorldAccess instanceof ServerLevel) {
			world = (ServerLevel) serverWorldAccess;
		} else {
			world = ((WorldGenRegion) serverWorldAccess).level;
		}

		if(!this.f_192654_.f_192741_().isEmpty())
			MinecraftForge.EVENT_BUS.post(new StructureAddedEvent((StructureStart<?>) (Object)this, world));
	}
}