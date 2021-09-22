package com.stereowalker.unionlib.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraftforge.eventbus.api.Event;

public class StructurePieceAddedEvent extends Event {
	private final StructurePiece structurePiece;
	private final ServerLevel world;
	
	public StructurePieceAddedEvent(StructurePiece structurePiece, ServerLevel world) {
		this.structurePiece = structurePiece;
		this.world = world;
	}

	public StructurePiece getStructurePiece() {
		return structurePiece;
	}
	
	public ServerLevel getWorld() {
		return world;
	}

	
}
