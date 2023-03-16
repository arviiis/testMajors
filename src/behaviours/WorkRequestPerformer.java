package behaviours;

import agents.TaskAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WorkRequestPerformer extends Behaviour {

	private int bestPrice; // The best offered price
	private int repliesCnt = 0; // The counter of replies from seller agents
	private MessageTemplate mt; // The template to receive replies
	private int step = 0;

	private AID[] availableAgents;
	private AID[] otherSellers;
	private List<AID> answeredSellers = new ArrayList<AID>();
	private AID bestSeller; // The agent who provides the best offer

	
//	private String targetBookTitle = "nosaukums";
	private String requiredSkill;
	private String type = "stacker";
	private Agent a;

	public WorkRequestPerformer(AID[] availableAgents, String requiredSkill) {
		this.availableAgents = availableAgents; // what type of OH Agent it has to look for
		this.requiredSkill = requiredSkill; // the skill the agent has to come up with a price
	}

	public void action() {
		
		// identifier to receive appropriate messages
		Random rand = new Random();
		String cId = "wo-trade" + rand.nextInt(10000);
		
		switch (step) {
		// Send out CFP for all the agents that offer the necessary service
		case 0:
			// create message for all the available OH agents that provide the service
			ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
			for (int i = 0; i < availableAgents.length; ++i) {
				cfp.addReceiver(availableAgents[i]);
			}
			// set the skill the TH agent is looking for
			cfp.setContent(requiredSkill);
			cfp.setConversationId(cId);
			cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
			myAgent.send(cfp);
			// Prepare the template to get proposals
			mt = MessageTemplate.and(MessageTemplate.MatchConversationId(cId),
					MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
			step = 1;
			break;
		// Receive all the proposals from the contacted agents
		case 1:
			ACLMessage reply = myAgent.receive(mt);
			if (reply != null) {
				// Reply received
				if (reply.getPerformative() == ACLMessage.PROPOSE) {
					// This is an offer
					System.out.println("Propose message received from: "+ reply.getSender().getLocalName());
					answeredSellers.add(reply.getSender()); // get a list of all the agents that proposed an offer
					int price = Integer.parseInt(reply.getContent());
					if (bestSeller == null || price < bestPrice) {
						// This is the best offer at present
						bestPrice = price;
						bestSeller = reply.getSender();
					}
				}
				repliesCnt++;
				if (repliesCnt >= availableAgents.length) {
					// Received all replies
					otherSellers = removeTheElement(availableAgents, bestSeller);
//					System.out.println("Best price is: " + bestPrice);
//					System.out.println("Best seller is: " + bestSeller.getLocalName());
//					System.out.print("Other sellers: ");
//					for (int i = 0; i < otherSellers.length; i++)
//						System.out.print(otherSellers[i].getLocalName() + ",  ");
//					System.out.println();
					step = 2;
				}
			} else {
				block();
			}
			break;
		// Accept one offer
		case 2:
			// Send the purchase order to the seller that provided the best offer
			ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
			order.addReceiver(bestSeller);
			order.setContent(Integer.toString(bestPrice));
			order.setConversationId(cId);
			order.setReplyWith("order" + System.currentTimeMillis());
			myAgent.send(order);
			System.out.println("Accept proposal message sent to: " + bestSeller.getLocalName());
			// Prepare the template to get the purchase order reply
			mt = MessageTemplate.and(MessageTemplate.MatchConversationId(cId),
					MessageTemplate.MatchInReplyTo(order.getReplyWith()));
			step = 3;
			break;
		// Confirm the deal and inform all the involved agents about the offer that was
		// accepted
		case 3:
			// Receive the purchase order reply
			reply = myAgent.receive(mt);
			if (reply != null) {
				// Purchase order reply received
				if (reply.getPerformative() == ACLMessage.INFORM) {
					// Purchase successful. We can terminate
					System.out.println(requiredSkill + " successfully purchased from agent " + reply.getSender().getLocalName());
					System.out.println("Price = " + bestPrice);
//						myAgent.doDelete();
				} else {
					System.out.println("Attempt failed: requested wo already sold.");
				}
				step = 4;
			} else {
				block();
			}
			break;
		case 4:
			// send out inform messages to the OH agents to inform about the offer that won
			ACLMessage inform = new ACLMessage(ACLMessage.INFORM); // msg that will be sent to all the OH agents
			for (int i = 0; i < answeredSellers.size(); ++i) {
				inform.addReceiver(answeredSellers.get(i));
			}
			// set the skill the TH agent is looking for
			inform.setContent(Integer.toString(bestPrice));
			inform.setConversationId(cId);
			inform.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
			myAgent.send(inform);
			step = 5;
		}	
	}

	public boolean done() {
		if (step == 2 && bestSeller == null) {
			System.out.println("Attempt failed: " + requiredSkill + " not available for sale");
		}
		return ((step == 2 && bestSeller == null) || step == 5);
	}

	// Function to remove the element
	public static AID[] removeTheElement(AID[] arr, AID bestSeller) {

		// finds the index of the bestSeller in the array of sellers
		int index = findIndex(arr, bestSeller);

		// return the original array if the array is empty or the index is not in array
		// range
		if (arr == null || index < 0 || index >= arr.length) {
			return arr;
		}

		// Create another array of size one less
		AID[] anotherArray = new AID[arr.length - 1];

		// Copy the elements except the index
		// from original array to the other array
		for (int i = 0, k = 0; i < arr.length; i++) {
			// if the index is
			// the removal element index
			if (i == index) {
				continue;
			}
			else {
			// if the index is not
			// the removal element index
			anotherArray[k++] = arr[i];
			}
		}
		// return the resultant array
		return anotherArray;
	}

	// Linear-search function to find the index of an element
	public static int findIndex(AID arr[], AID t) {
		// if array is Null
		if (arr == null) {
			System.out.println("Array is empty");
			return -1;
		}
		// find length of array
		int len = arr.length;
		int i = 0;
		// traverse in the array
		while (i < len) {
			// if the i-th element is t then return the index
			if (Objects.equals(arr[i], t)) {
				return i;
			} else {
				i = i + 1;
			}
		}
		return -1;
	}

}
