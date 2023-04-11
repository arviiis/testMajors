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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WorkRequestPerformer extends Behaviour {

	private int bestPrice; // The best offered price
	private int repliesCnt = 0; // The counter of replies from seller agents
	private int repliesCnt2 = 0; 
	private MessageTemplate mt; // The template to receive replies
	private int step = 0;

	private TaskAgent tA;
	private AID[] availableAgents;
	private AID[] otherSellers;
	private List<AID> answeredSellers = new ArrayList<AID>();
	private AID bestSeller; // The agent who provides the best offer

	private String requiredSkill;

	public WorkRequestPerformer(TaskAgent tA, AID[] availableAgents, String requiredSkill) {
		this.tA = tA;
		this.availableAgents = availableAgents; // what type of OH Agent it has to look for
		this.requiredSkill = requiredSkill; // the skill the agent has to come up with a price
	}

	public void action() {

		// identifier to receive appropriate messages
		Random rand = new Random();
		String cId = Integer.toString(rand.nextInt(10000));

		switch (step) {
		// send out the TH time to all the available agents
		case 0:
			ACLMessage info = new ACLMessage(ACLMessage.REQUEST);
			for (int i = 0; i < availableAgents.length; ++i) {
				info.addReceiver(availableAgents[i]);
			}
			info.setContent(Integer.toString(tA.thTime));
			info.setConversationId("th_inform" + cId);
			info.setReplyWith("th_inform" + System.currentTimeMillis());
			myAgent.send(info);
			System.out.println(
					"\n" + myAgent.getLocalName() + " sends the TA time (" + tA.thTime + ") to available agents.");
			// Prepare the template to get the purchase order reply
			mt = MessageTemplate.and(MessageTemplate.MatchConversationId("th_inform" + cId),
					MessageTemplate.MatchInReplyTo(info.getReplyWith()));
			step = 1;
			break;
		// receive answer from all the agents
		case 1:
			ACLMessage reply = myAgent.receive(mt);
			if (reply != null) {
				if (reply.getPerformative() == ACLMessage.INFORM) {
//					System.out.println(myAgent.getLocalName() + " received INFORM message from: " + reply.getSender().getLocalName());
					repliesCnt++;
				}
				if (repliesCnt >= availableAgents.length) {
					System.out.println("All the answers received!");
					step = 2;
				}
			} else {
				block();
			}
			break;
			// Send out CFP for all the agents that offer the necessary service
		case 2:
			System.out.println(tA.getLocalName() + " looking for price for skill: " + requiredSkill);

			// create message for all the available OH agents that provide the service
			ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
			for (int i = 0; i < availableAgents.length; ++i) {
				cfp.addReceiver(availableAgents[i]);
			}
			// set the skill the TH agent is looking for
			cfp.setContent(requiredSkill);
			cfp.setConversationId("trade" + cId);
			cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
			myAgent.send(cfp);
			// Prepare the template to get proposals
			mt = MessageTemplate.and(MessageTemplate.MatchConversationId("trade" + cId),
					MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
			step = 3;
			break;
		// Receive all the proposals from the contacted agents
		case 3:
			reply = myAgent.receive(mt);
			if (reply != null) {
				// Reply received
				if (reply.getPerformative() == ACLMessage.PROPOSE) {
					// This is an offer

					answeredSellers.add(reply.getSender()); // get a list of all the agents that proposed an offer
					int price = Integer.parseInt(reply.getContent());
					System.out.println(myAgent.getLocalName() + " received PROPOSE message from: "
							+ reply.getSender().getLocalName() + " = " + price);
					if (bestSeller == null || price < bestPrice) {
						// This is the best offer at present
						bestPrice = price;
						bestSeller = reply.getSender();
					}
				}
				repliesCnt2++;
				if (repliesCnt2 >= availableAgents.length) {
					// Received all replies
					otherSellers = removeTheElement(availableAgents, bestSeller); // remove an element from array
//					System.out.println("Best price is: " + bestPrice);
//					System.out.println("Best seller is: " + bestSeller.getLocalName());
//					System.out.print("Other sellers: ");
//					for (int i = 0; i < otherSellers.length; i++)
//						System.out.print(otherSellers[i].getLocalName() + ",  ");
//					System.out.println();
					step = 4;
				}
			} else {
				block();
			}
			break;
		case 4:
			// Send the purchase order to the seller that provided the best offer
			ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
			order.addReceiver(bestSeller);
			order.setContent(requiredSkill);
			order.setConversationId(cId);
			order.setReplyWith("order" + System.currentTimeMillis());
			myAgent.send(order);
			System.out.println(
					myAgent.getLocalName() + " sends ACCEPT_PROPOSAL message to: " + bestSeller.getLocalName());
			// Prepare the template to get the purchase order reply
			mt = MessageTemplate.and(MessageTemplate.MatchConversationId(cId),
					MessageTemplate.MatchInReplyTo(order.getReplyWith()));
			step = 5;
			break;
		// Confirm the deal and inform all the involved agents about the offer that was
		// accepted
		case 5:
			// Receive the purchase order reply
			reply = myAgent.receive(mt);
			if (reply != null) {
				// Purchase order reply received
				if (reply.getPerformative() == ACLMessage.INFORM) {
					// Purchase successful
					System.out.println(requiredSkill + " successfully purchased from agent "
							+ reply.getSender().getLocalName() + " for price: " + bestPrice);

					tA.operationList.add(reply.getSender().getLocalName()); // TH agent task executor list
					
					tA.thTime = bestPrice;// (bestPrice = (thTime + skillMfgTime))seconds it takes from startDate to complete all the TA accepted tasks
					System.out.println(myAgent.getLocalName() + " occupied time: " + tA.thTime);

				} else {
					System.out.println("Attempt failed: requested wo already sold.");
				}
				step = 6;
			} else {
				block();
			}
			break;
		case 6:
			// send out inform messages to the OH agents to inform about the offer that won
			ACLMessage inform = new ACLMessage(ACLMessage.INFORM); // msg that will be sent to all the OH agents
			for (int i = 0; i < answeredSellers.size(); ++i) {
				inform.addReceiver(answeredSellers.get(i));
			}
			// set the skill the TH agent is looking for
			inform.setContent(Integer.toString(bestPrice));
			inform.setConversationId("announce_winner");
			inform.setReplyWith("announce_winner" + System.currentTimeMillis()); // Unique value
			myAgent.send(inform);
			step = 7;
		}
	}

//		
//			System.out.println("\nTH agent " + tA.getLocalName() + " looking for price for skill: " + requiredSkill);
////			System.out.println("\nTH agent "+ myAgent.getLocalName() + " sends out CFP to available agents");
//			
//			// create message for all the available OH agents that provide the service
//			ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
//			for (int i = 0; i < availableAgents.length; ++i) {
//				cfp.addReceiver(availableAgents[i]);
//			}
//			// set the skill the TH agent is looking for
//			cfp.setContent(requiredSkill);
//			cfp.setConversationId("trade"+cId);
//			cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
//			myAgent.send(cfp);
//			// Prepare the template to get proposals
//			mt = MessageTemplate.and(MessageTemplate.MatchConversationId("trade"+cId),
//					MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
//			step = 1;
//			break;
//		// Receive all the proposals from the contacted agents
//		case 1:
//			ACLMessage reply = myAgent.receive(mt);
//			if (reply != null) {
//				// Reply received
//				if (reply.getPerformative() == ACLMessage.PROPOSE) {
//					// This is an offer
//					System.out.println(myAgent.getLocalName() + " received PROPOSE message from: "+ reply.getSender().getLocalName());
//					answeredSellers.add(reply.getSender()); // get a list of all the agents that proposed an offer
//					int price = Integer.parseInt(reply.getContent());
//					if (bestSeller == null || price < bestPrice) {
//						// This is the best offer at present
//						bestPrice = price;
//						bestSeller = reply.getSender();
//					}
//				}
//				repliesCnt++;
//				if (repliesCnt >= availableAgents.length) {
//					// Received all replies
//					otherSellers = removeTheElement(availableAgents, bestSeller); // remove an element from array
////					System.out.println("Best price is: " + bestPrice);
////					System.out.println("Best seller is: " + bestSeller.getLocalName());
////					System.out.print("Other sellers: ");
////					for (int i = 0; i < otherSellers.length; i++)
////						System.out.print(otherSellers[i].getLocalName() + ",  ");
////					System.out.println();
//					step = 2;
//				}
//			} else {
//				block();
//			}
//			break;
//		// inform the OH agent about the TH time it is already occupied
//		case 2:
//			ACLMessage info = new ACLMessage(ACLMessage.PROPOSE);
//			info.addReceiver(bestSeller);
//			info.setContent(Integer.toString(tA.thTime));
//			info.setConversationId("ta_inform" + cId);
//			info.setReplyWith("ta_inform" + System.currentTimeMillis());
//			myAgent.send(info);
//			System.out.println(myAgent.getLocalName() + " sends the TA time ("+tA.thTime+") to: " + bestSeller.getLocalName());
//			// Prepare the template to get the purchase order reply
//			mt = MessageTemplate.and(MessageTemplate.MatchConversationId("ta_inform" + cId),
//					MessageTemplate.MatchInReplyTo(info.getReplyWith()));
//			step = 3;
//			break;
//		case 3:
//			reply = myAgent.receive(mt);
//			if (reply != null) {
//				if(reply.getPerformative() == ACLMessage.AGREE) {
//					int skillMfgTime = Integer.parseInt(reply.getContent());
//					
//					System.out.println(myAgent.getLocalName() + " received the skill ("+ requiredSkill+") mfg time: "+ skillMfgTime);
//					tA.thTime += skillMfgTime; // get just the time it takes to produce the single skill
//				}
//				else {
//					System.out.println("Best seller agent did not receive TH agent occupied time");
//				}
//				step = 4;
//			} else {	
//				block();
//			}
//			break;
//		// Accept one offer
//		case 4:
//			// Send the purchase order to the seller that provided the best offer
//			ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
//			order.addReceiver(bestSeller);
//			order.setContent(requiredSkill);
//			order.setConversationId(cId);
//			order.setReplyWith("order" + System.currentTimeMillis());
//			myAgent.send(order);
//			System.out.println(myAgent.getLocalName() + " sends ACCEPT_PROPOSAL message to: " + bestSeller.getLocalName());
//			// Prepare the template to get the purchase order reply
//			mt = MessageTemplate.and(MessageTemplate.MatchConversationId(cId),
//					MessageTemplate.MatchInReplyTo(order.getReplyWith()));
//			step = 5;
//			break;
//		// Confirm the deal and inform all the involved agents about the offer that was
//		// accepted
//			
//		case 5:
//			// Receive the purchase order reply
//			reply = myAgent.receive(mt);
//			if (reply != null) {
//				// Purchase order reply received
//				if (reply.getPerformative() == ACLMessage.INFORM) {
//					// Purchase successful
//					System.out.println(requiredSkill + " successfully purchased from agent " + reply.getSender().getLocalName()+" for price: "+ bestPrice);
//					
//					tA.operationList.add(reply.getSender().getLocalName()); // TH agent task executor list
//					
////					tA.taTime += bestPrice; // seconds it takes from startDate to complete all the TA accepted tasks
//					System.out.println(myAgent.getLocalName() + " TH agent occupied time: " + tA.thTime);
//				} else {
//					System.out.println("Attempt failed: requested wo already sold.");
//				}
//				step = 6;
//			} else {
//				block();
//			}
//			break;
//		case 6:
//			// send out inform messages to the OH agents to inform about the offer that won
//			ACLMessage inform = new ACLMessage(ACLMessage.INFORM); // msg that will be sent to all the OH agents
//			for (int i = 0; i < answeredSellers.size(); ++i) {
//				inform.addReceiver(answeredSellers.get(i));
//			}
//			// set the skill the TH agent is looking for
//			inform.setContent(Integer.toString(bestPrice));
//			inform.setConversationId("announce_winner");
//			inform.setReplyWith("announce_winner" + System.currentTimeMillis()); // Unique value
//			myAgent.send(inform);
//			step = 7;
//		}	
//	}

	public boolean done() {

		if (step == 4 && bestSeller == null) {
			System.out.println("Attempt failed: " + requiredSkill + " not available for sale");
		}

//		return ((step == 2 && bestSeller == null) || step == 5);
		if ((step == 4 && bestSeller == null) || step == 7) {
			System.out.println(myAgent.getLocalName() + " getting " + requiredSkill + " done!");
			return true;
		} else {
			return false;
		}
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
			} else {
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
