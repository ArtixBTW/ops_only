package dev.artixbtw.ops_only;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;

public class OpsOnlyCommand {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands
						.literal("opsonly")
						.requires(src -> src.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
						.then(Commands.literal("off").executes(OpsOnlyCommand::off))
						.then(Commands.literal("on").executes(OpsOnlyCommand::on))
						.then(Commands.literal("status").executes(OpsOnlyCommand::status)));
	}

	private static int on(CommandContext<CommandSourceStack> ctx) {
		if (OpsOnly.operatorsOnlyEnabled) {
			ctx.getSource().sendSuccess(
					() -> OpsOnly.getTranslatableComponent("already_enabled"), true);
		} else {
			OpsOnly.operatorsOnlyEnabled = true;
			ctx.getSource()
					.sendSuccess(() -> OpsOnly.getTranslatableComponent("enabled"), true);
		}

		return Command.SINGLE_SUCCESS;
	}

	private static int off(CommandContext<CommandSourceStack> ctx) {
		if (!OpsOnly.operatorsOnlyEnabled) {
			ctx.getSource().sendSuccess(
					() -> OpsOnly.getTranslatableComponent("already_disabled"), true);
		} else {
			OpsOnly.operatorsOnlyEnabled = false;
			ctx.getSource()
					.sendSuccess(() -> OpsOnly.getTranslatableComponent("disabled"), true);
		}

		return Command.SINGLE_SUCCESS;
	}

	private static int status(CommandContext<CommandSourceStack> ctx) {
		Component translation = OpsOnly.getTranslatableComponent(
				OpsOnly.operatorsOnlyEnabled ? "is_enabled" : "is_disabled");
		ctx.getSource().sendSuccess(() -> translation, true);

		return Command.SINGLE_SUCCESS;
	}
}
