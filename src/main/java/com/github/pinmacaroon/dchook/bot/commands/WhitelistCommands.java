package com.github.pinmacaroon.dchook.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import com.github.pinmacaroon.dchook.conf.ModConfigs;
import com.github.pinmacaroon.dchook.Hook;

import java.text.MessageFormat;

public class WhitelistCommands {
	static String bedrockPrefix = "bedrock:";
	public static void add(SlashCommandInteractionEvent event) {
		String username = event.getOption("username").getAsString();
		boolean isWhitelisted = false;
		boolean bedrock = false;
		if (username.startsWith(bedrockPrefix)) {
			 username = username.substring(bedrockPrefix.length());
			 bedrock = true;
		}

		for (String name : Hook.getGameServer().getPlayerList().getWhiteListNames()) {
			if (name.equalsIgnoreCase(username) || (bedrock && name.equalsIgnoreCase("."+username))) {
				isWhitelisted = true;
				break;
			}
		}

		if (isWhitelisted) {
			event.reply(MessageFormat.format(ModConfigs.MESSAGES_BOT_WHITELIST_ADD_FAIL, username)).queue();
			return;
		}
		Hook.getGameServer().getCommands().performPrefixedCommand(Hook.getGameServer().createCommandSourceStack(), (bedrock ? "f" : "") + "whitelist add " + username);
		event.reply(MessageFormat.format(ModConfigs.MESSAGES_BOT_WHITELIST_ADD_SUCCESS, username)).queue();
	}

	public static void remove(SlashCommandInteractionEvent event) {
		String username = event.getOption("username").getAsString();
		boolean isWhitelisted = false;
		boolean bedrock = false;
		if (username.startsWith(bedrockPrefix)) {
			 username = username.substring(bedrockPrefix.length());
			 bedrock = true;
		}

		for (String name : Hook.getGameServer().getPlayerList().getWhiteListNames()) {
			if (name.equalsIgnoreCase(username) || (bedrock && name.equalsIgnoreCase("."+username))) {
				isWhitelisted = true;
				break;
			}
		}

		if (!isWhitelisted) {
			event.reply(MessageFormat.format(ModConfigs.MESSAGES_BOT_WHITELIST_REMOVE_FAIL, username)).queue();
			return;
		}

		Hook.getGameServer().getCommands().performPrefixedCommand(Hook.getGameServer().createCommandSourceStack(), (bedrock ? "f" : "") + "whitelist remove" + username);
		
		event.reply(MessageFormat.format(ModConfigs.MESSAGES_BOT_WHITELIST_REMOVE_SUCCESS, username)).queue();
	}

	public static void list(SlashCommandInteractionEvent event) {
		String[] names = Hook.getGameServer().getPlayerList().getWhiteListNames();
		StringBuilder message = new StringBuilder();
		
		message.append(ModConfigs.MESSAGES_BOT_WHITELIST_LIST);
		
		if (names.length == 0) {
			message.append("❌");
		} else {
			message.append("`").append(String.join("`, `", names)).append("`");
		}
		
		OptionMapping ephemeralOption = event.getOption("ephemeral");
		boolean ephemeral = ephemeralOption != null && ephemeralOption.getAsBoolean();
		
		event.reply(message.toString()).setEphemeral(ephemeral).queue();
	}

	public static void reload(SlashCommandInteractionEvent event) {
		Hook.getGameServer().getPlayerList().reloadWhiteList();
		event.reply(ModConfigs.MESSAGES_BOT_WHITELIST_RELOADED).queue();
	}
}