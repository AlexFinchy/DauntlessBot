package com.dauntlessnetworks.Startup;

import java.awt.Color;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class BotStartup {



	public static void main(String[] args) {
		
		
		try {
			JDA jda = JDABuilder.createDefault(Token.TOKEN).build();

			jda.awaitReady();

			TextChannel channel = jda.getTextChannelsByName("rules", true).get(0);
			
			jda.addEventListener(new MessageListener());
			
			EmbedBuilder builder = new EmbedBuilder();
			
			builder.setTitle("**NETWORK RULES**");
			
			builder.setColor(Color.cyan);
			
			builder.setDescription("These rules are in place to help ensure a safe and fun community - rules are subject to change so it is advisable to check the rules every few months.");
			builder.addField("1) No homophobic, racist or bigoted language", "*Language of this kind will not be tolerated (Mute/Ban)*", false);
			
			builder.addField("2) No insulting or rude comments towards staff", "*Staff will treat you with respect we expect the same from all players (Mute/Ban)*", false);
			
			builder.addField("3) No hacking or cheat clients", "*The offical client should be used at all times (Tempban/Ban)*", false);
			
			builder.addField("4) Abide with server rules", "*Follow the rules of each server you play on (Mute/Tempban/Ban)*", false);
			
			builder.setFooter("Dauntless Networks™", "http://storage.pandaserv.net:8000/f/d2bef98dce8d4138b4ad/?dl=1");
			
			builder.setThumbnail("http://storage.pandaserv.net:8000/f/30b93a979b9e4e18a1b7/?dl=1");
			
			//builder.setAuthor("name");
			
			channel.sendMessage(builder.build()).queue();
			
	
			
			
			
			
		} catch (LoginException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	
	
}
