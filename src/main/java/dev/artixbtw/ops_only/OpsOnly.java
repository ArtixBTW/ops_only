package dev.artixbtw.ops_only;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class OpsOnly implements ModInitializer {
	public static final String MOD_ID = "ops_only";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static boolean operatorsOnlyEnabled = false;

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT
				.register((dispatcher, registryAccess, environment) -> OpsOnlyCommand.register(dispatcher));

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			if (operatorsOnlyEnabled && !server.getPlayerList().isOp(handler.player.nameAndId())) {
				handler.disconnect(getTranslatableComponent("disconnect_reason.not_op"));
			}
		});
	}

	public static MutableComponent getTranslatableComponent(String path, Object... translatableArgs) {
		return Component.translatable(MOD_ID + "." + path, translatableArgs);
	}
}
