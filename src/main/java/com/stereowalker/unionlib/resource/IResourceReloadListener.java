package com.stereowalker.unionlib.resource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

@SuppressWarnings("deprecation")
public interface IResourceReloadListener<T> extends ResourceManagerReloadListener {

	@Override
	default void onResourceManagerReload(
			ResourceManager resourceManager/* , Predicate<IResourceType> resourcePredicate */) {
		
	}
	
	@Override
	default CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier stage, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
		return load(resourceManager, preparationsProfiler, backgroundExecutor)
				.thenCompose(stage::wait)
				.thenAccept((value) -> {
						apply(value, resourceManager, reloadProfiler, gameExecutor);
				});

	}

	CompletableFuture<T> load(ResourceManager resourceManager, ProfilerFiller profiler, Executor executor);

	CompletableFuture<Void> apply (T value, ResourceManager resourceManager, ProfilerFiller profiler, Executor executor);
}
