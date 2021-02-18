package com.dauntlessnetworks.Startup;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.PermOverrideManager;
import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;



public class MessageListener extends ListenerAdapter {
	
	public static String prefix = "!";
	
	public static HashMap<TextChannel, Ticket> TextChannelTicket = new HashMap<>();
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		super.onMessageReceived(event);
		
		Message message = event.getMessage();
		String rawmessage = message.getContentRaw();
		Guild guild = event.getGuild();
		
		if(event.getMember() != null) {
			Member member = event.getMember();
			if(!event.getMember().getUser().isBot()) {
				if(rawmessage.startsWith(prefix)) {
					String command = rawmessage.replaceAll(prefix, "");
					switch (command) {
					case "new":
						
						
						
						message.delete().complete();
						
						if(event.getChannel().getName().equalsIgnoreCase("support-tickets")) {
							
							
							Category category = guild.getCategoryById(474218929858936853L);
							
							if(category.getChannels().size() > 0 ) {
								TextChannel LastChannel = category.getTextChannels().get(category.getTextChannels().size()-1);
								
								int ChannelNumber = Integer.parseInt(LastChannel.getName().replaceAll("ticket-", ""));
								
								ChannelNumber = ChannelNumber + 1;
								
								while(guild.getTextChannelsByName("ticket-" + ChannelNumber, false).size() > 0) {
									ChannelNumber = ChannelNumber + 1;
								}
								
								TextChannel textchannel = (TextChannel) category.createTextChannel("ticket-" + ChannelNumber).complete();

								textchannel.upsertPermissionOverride(member).grant(Permission.MESSAGE_READ,Permission.MESSAGE_WRITE).queue();

								Ticket ticket = new Ticket(textchannel, member);
								
								TextChannelTicket.put(textchannel, ticket);
								
							} else {
								
								TextChannel textchannel = (TextChannel) category.createTextChannel("ticket-" + 1).complete();

								textchannel.upsertPermissionOverride(member).grant(Permission.MESSAGE_READ,Permission.MESSAGE_WRITE).queue();

								Ticket ticket = new Ticket(textchannel, member);
								
								TextChannelTicket.put(textchannel, ticket);
								
								
							}
							
							
							

							
							
						} else {
							member.getUser().openPrivateChannel().queue((channel) -> 
							{
								TextChannel SupportTickets = guild.getTextChannelsByName("support-tickets", true).get(0);
								channel.sendMessage("Please use the support-tickets channel to create a ticket. " + SupportTickets.getAsMention() ).queue();
							});
							
							
						}
						
						
						
						break;
						
					case "close":
						event.getMessage().delete().complete();
						Category category = guild.getCategoryById(474218929858936853L);
						if(category.getChannels().contains(event.getTextChannel())) {
							event.getTextChannel().delete().complete();
							TextChannelTicket.remove(event.getChannel());
						}
						
						
						
						
						break;
						

					default:
						break;
					}
					
					
				} else if(event.getChannel().getName().equalsIgnoreCase("support-tickets")) {
					
					if(!member.getPermissions().contains(Permission.ADMINISTRATOR)) {
						message.delete().complete();
						member.getUser().openPrivateChannel().queue((channel) -> 
						{
							TextChannel SupportTickets = guild.getTextChannelsByName("support-tickets", true).get(0);
							channel.sendMessage("You can only use commands in the " + SupportTickets.getAsMention() + " channel, use !new to create a new ticket." ).queue();
						});
					}
					
				} else if(TextChannelTicket.containsKey(event.getChannel())) {
					
					Ticket ticket = TextChannelTicket.get(event.getChannel());
					if(ticket.Category == "Server") {
						
						if(ticket.QuestionNumber == 3) {
							
							ticket.Username = rawmessage;
							ticket.NextQuestion();
							
						} else if(ticket.QuestionNumber == 4) {
							ticket.Description = rawmessage;
							ticket.NextQuestion();
						}
						
						
						
					}
					
					
					
				}
			}
		} else {
			
			if(rawmessage.equalsIgnoreCase("!clearchat")) {
				if(event.getChannel().getType() == ChannelType.PRIVATE) {
					
					
					event.getPrivateChannel().getHistoryBefore(message.getId(), 100).queue((messagehistory) -> {
						Iterator<Message> I = messagehistory.getRetrievedHistory().iterator();
						while(I.hasNext()) {
							Message delmessage = I.next();
							if(!(delmessage.getAuthor() == event.getAuthor())) {
								delmessage.delete().queue();
							}
						}
					});
					
					
				}
			}
			
		}


		
		
		
		
	}
	
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		super.onMessageReactionAdd(event);
		Guild guild = event.getGuild();
		Category category = guild.getCategoryById(474218929858936853L);
		
		if(category.getTextChannels().contains(event.getTextChannel())) {
			
			Ticket ticket = TextChannelTicket.get(event.getTextChannel());
			
			if(!event.getUser().isBot()) {
				
				String Emotename = event.getReaction().getReactionEmote().getName();
				
				switch (Emotename) {
				case "\uD83C\uDDE6":
					
					if(ticket.Category == null) {
						ticket.Category = "General";
					}
					
					if(ticket.Category.equalsIgnoreCase("Server")) {
						if(ticket.QuestionNumber == 2) {
							ticket.ChosenServer = "Wizardry Evolved";
						}
					}
					
					if(ticket.Category.equalsIgnoreCase("Payment")) {
						if(ticket.QuestionNumber == 2) {
							ticket.SubmitTicket(false);
						}
					}
					
					ticket.NextQuestion();
					
					break;
					
				case "\uD83C\uDDE7":
					
					if(ticket.Category == null) {
						ticket.Category = "Server";
					}
					
					if(ticket.Category.equalsIgnoreCase("Server")) {
						if(ticket.QuestionNumber == 2) {
							ticket.ChosenServer = "Atom Fallout";
						}
					}
					
					if(ticket.Category.equalsIgnoreCase("Payment")) {
						if(ticket.QuestionNumber == 2) {
							
						}
					}
					
					ticket.NextQuestion();
					
					
					break;
					
				case "🇨":
					
					if(ticket.Category == null) {
						ticket.Category = "Payment";
					}
					
					ticket.NextQuestion();
					
					break;

				default:
					
					
					
					break;
				}
				
				
			}
			
		}
			
		
		
		
		
		
		
	}
	
	@Override
	public void onGenericMessageReaction(GenericMessageReactionEvent event) {
		// TODO Auto-generated method stub
		super.onGenericMessageReaction(event);
		
		
		
	}
	


}
