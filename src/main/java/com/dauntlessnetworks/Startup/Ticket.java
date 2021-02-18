package com.dauntlessnetworks.Startup;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.PermOverrideManager;
import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;

import java.awt.Color;
import java.util.List;



public class Ticket {
	
	int QuestionNumber = 0;
	
	TextChannel channel;
	Member TicketOwner;
	Message CurrentMessage;
	String Category = null;
	Boolean Ticketopen;
	
	String ChosenServer = "N/A";
	String Username = "N/A";
	String Description = "N/A";
	
	public Ticket(TextChannel TicketChannel, Member member) {
			channel = TicketChannel;
			TicketOwner = member;
			EmbedBuilder TicketBuilder = new EmbedBuilder();
			TicketBuilder.setTitle("**" + member.getEffectiveName().toUpperCase() + "'S TICKET**");
			
			TicketBuilder.setColor(Color.CYAN);
			
			TicketBuilder.addField("Welcome to your ticket!", "We will ask you a few questions to help understand your issue and get the appropriate staff to you as soon as possible.", false);

			TicketChannel.sendMessage(TicketBuilder.build()).queue();
			
			
			
			StartQuestions();
		
			
			
			
		}
	
	public void StartQuestions() {
		
		QuestionNumber = 1;
		
		EmbedBuilder Question1 = new EmbedBuilder();
		Question1.addField("Question 1 - What does your issue relate to?", "Please put the type the appropriate letter that best describes your issue.", false);
		Question1.addField("A) General Enquiries", "This will cover most areas, including open staff spots, server rules, press and advertisement.", false);
		Question1.addField("B) Server issues", "This covers any issues you have on or with our servers, including connection issues.", false);
		Question1.addField("C) Payment issues", "This is for any payment related issues, for example if your card is denied when donating.", false);
		Question1.setColor(Color.CYAN);
		channel.sendMessage(Question1.build()).queue((message) -> {
			message.addReaction("\uD83C\uDDE6").queue();
			message.addReaction("\uD83C\uDDE7").queue();
			message.addReaction("\uD83C\uDDE8").queue();
			CurrentMessage = message;
		});
		
		
		
		}
		
	public void NextQuestion() {
		
		CurrentMessage.clearReactions().complete();
		
		QuestionNumber = QuestionNumber + 1;
		
		
		switch (Category) {
		case "General":
			SubmitTicket(false);
			break;
			
		case "Server":
			
			
			//First Questions
			
			if(QuestionNumber == 2) {
				
				EmbedBuilder ServerQuestion2 = new EmbedBuilder();
				ServerQuestion2.addField("What server is the issue occurring on?", "Please select the server the issue is happening on", false);
				ServerQuestion2.addField("A) Wizardry Evolved", "The Wizardry Evolved Server", false);
				ServerQuestion2.addField("B) Atom Fallout", "The Atom Fallout", false);
				ServerQuestion2.setColor(Color.CYAN);
				channel.sendMessage(ServerQuestion2.build()).queue((message) -> {
					
					
					message.addReaction("\uD83C\uDDE6").queue();
					message.addReaction("\uD83C\uDDE7").queue();
					//message.addReaction("🇨").queue();
					CurrentMessage = message;
				});
				
				
				
			} else if(QuestionNumber == 3) {
				
				EmbedBuilder ServerQuestion3 = new EmbedBuilder();
				ServerQuestion3.setColor(Color.CYAN);
				ServerQuestion3.addField("What is your Minecraft username?", "Please enter your minecraft username below", false);
				channel.sendMessage(ServerQuestion3.build()).queue();
			}
			
			else if(QuestionNumber == 4) {
				EmbedBuilder ServerQuestion4 = new EmbedBuilder();
				ServerQuestion4.setColor(Color.CYAN);
				ServerQuestion4.addField("Please enter a brief description of your issue", "Enter the issue below", false);
				channel.sendMessage(ServerQuestion4.build()).queue();
			}
			
			else if(QuestionNumber == 5) {
				
				SubmitTicket(true);
				
				
			}
			
			
			break;
			
		case "Payment":
			
			if(QuestionNumber == 2) {
				
				EmbedBuilder PaymentQuestion2 = new EmbedBuilder();
				PaymentQuestion2.addField("Do you require assistance with making a purchase? ", "Please select an option", false);
				PaymentQuestion2.addField("A) Yes", "Yes, I require assistance to make a purchase,", false);
				PaymentQuestion2.addField("B) No", "No, my issue is regarding something else.", false);
				PaymentQuestion2.setColor(Color.CYAN);
				channel.sendMessage(PaymentQuestion2.build()).queue((message) -> {
					
					
					message.addReaction("\uD83C\uDDE6").queue();
					message.addReaction("\uD83C\uDDE7").queue();
					CurrentMessage = message;
				});

				
			}
			if(QuestionNumber == 3) {
				
			}
			
			break;

		default:
			break;
		}
		
	}
	
	public void SubmitTicket(Boolean AdditionalInfo) {
		
		EmbedBuilder TicketConfirm = new EmbedBuilder();
		
		TicketConfirm.setTitle("**Ticket Confirmed**".toUpperCase());
		
		
		
		TicketConfirm.setColor(Color.CYAN);
		if(AdditionalInfo) {
			TicketConfirm.addField("Here are the details of your ticket:", "Your issue was on " + ChosenServer + ". Your username is " + Username + ". Your issue was: " + Description, false);
		}
		Role role = channel.getGuild().getRolesByName("Support Team", false).get(0);
		channel.putPermissionOverride(role).complete();

		List<Member> Members = channel.getGuild().getMembersWithRoles(role);
		
		int OnlineSupport = 0;
		
		for(Member member : Members) {
			if(member.getOnlineStatus().equals(OnlineStatus.ONLINE)) {
				OnlineSupport = OnlineSupport + 1;
			}
			member.getUser().openPrivateChannel().queue((channel) -> {
				channel.sendMessage("**TICKET NOTIFICATION**" + "\n" + "Discord User: " + TicketOwner.getAsMention() + "\n" + "Category: " + Category + "\n" +  "Sever: " + ChosenServer + "\n" + "Player Username: " + Username + "\n" + "Description: " + Description + "\n" + "Ticket Channel: " + this.channel.getAsMention()).queue();
			
			});
		}
		
		if(OnlineSupport > 1) {
			TicketConfirm.addField("Your request has been confirmed!", "There is currently " + OnlineSupport + " " + role.getAsMention() + " members online and they have been informed of your ticket; they will be with you shortly. There is currently " + String.valueOf(MessageListener.TextChannelTicket.size() - 1) + " other tickets open!" , false);
		} else {
			TicketConfirm.addField("Your request has been confirmed!", "There is currently " + OnlineSupport + " " + role.getAsMention() + " member online and they have been informed of your ticket; they will be with you shortly. There is currently " + String.valueOf(MessageListener.TextChannelTicket.size() - 1) + " other tickets open!" , false);
		}
		
		TicketConfirm.setColor(Color.CYAN);
		channel.sendMessage(TicketConfirm.build()).queue();
		
	}
	
	}
	
