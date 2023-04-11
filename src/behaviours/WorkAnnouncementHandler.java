package behaviours;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import ProductHolonGUI.WelcomeFrame;
import agents.OperationalAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WorkAnnouncementHandler extends CyclicBehaviour {

	private String[] skills; // skills that the OH agent is capable of fulfilling
	private int price;
	private int price2;

	OperationalAgent opA;

	public WorkAnnouncementHandler(OperationalAgent opA, String[] skills) {
		this.opA = opA;
		this.skills = skills;
	}

	public void action() {
		// receive message with CFP performative
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) {
			// CFP Message received. Process it
			String requiredSkill = msg.getContent();
			opA.requiredSkill = requiredSkill; // update OH knowledge of the required skill at the moment
			ACLMessage reply = msg.createReply();
			boolean i = true;

			if (hasSkill(skills, requiredSkill)) {
				// check if there is a time slot in the schedule
				if (i) {
//					price = getPrice(WelcomeFrame.startDate, opA.operationSequence, opA.catalogue, requiredSkill); // calculate price
					price = getPrice(opA.OHTime, opA.thTime, requiredSkill); //calculate price
					
					// send a proposal
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(Integer.toString(price));
//					System.out.println("Propose message sent to TH agent from: " + myAgent.getLocalName());

					// get the previously saved price for the product
					Integer previousPrice = (Integer) opA.priceCatalogue.get(requiredSkill);
					// update the last price for the skill in the OH agent data base
					if (previousPrice != null) {
						opA.priceCatalogue.replace(requiredSkill, price);// update the price of the skill in the
																			// catalogue
//						System.out.println(requiredSkill + " changed price in " + myAgent.getLocalName()
//								+ " price catalogue: " + opA.priceCatalogue);
					} else {
						opA.priceCatalogue.put(requiredSkill, price);
//						System.out.println(myAgent.getLocalName() + " price catalogue: " + opA.priceCatalogue);
					}
				}
			} else {
				// The OH cannot complete the wo
				reply.setPerformative(ACLMessage.REFUSE);
				reply.setContent("not-available");
//				System.out.println("Refuse message sent to TH agent from: " + myAgent.getLocalName());
			}
			myAgent.send(reply);
		} else {
			block();
		}
	}

	// Linear-search function to find the index of an element
	public static boolean hasSkill(String[] skills, String requiredSkill) {
		// if array is Null
		if (skills == null) {
			System.out.println("Skill array is empty");
			return false;
		}
		int i = 0;
		// traverse in the array
		while (i < skills.length) {
			// if the i-th element is t then return the index
			if (Objects.equals(skills[i], requiredSkill)) {
				return true;
			} else {
				i = i + 1;
			}
		}
		return false;
	}

	// calculate the price based on the estimate of "when the OH agent will finish
	// the proposed task"
	private int getPrice(int ohTime, int thTime, String requiredSkill) {
		int taskFinishedTime;
		int requiredSkillMfgTime = (Integer) opA.catalogue.get(requiredSkill);
		
		if (thTime>ohTime) {
			taskFinishedTime = (thTime + requiredSkillMfgTime);
		}
		else {
			taskFinishedTime = (ohTime + requiredSkillMfgTime);
		}
//		System.out.println(myAgent.getLocalName() + " price breakdown: \n"
//				+ "	OH time: " + ohTime + "\n"
//				+ "	TH time: " + thTime + "\n"
//				+ "	Time to complete the required skill: " + requiredSkillMfgTime + "\n"
//				+ "	Time to finish the task (price): "+ taskFinishedTime);
		return taskFinishedTime;
	}
}
