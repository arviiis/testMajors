package behaviours;

import java.util.Hashtable;

import agents.OperationalAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;



public class InfoAboutWinningOH extends CyclicBehaviour {
	
	String requiredSkill;
	OperationalAgent opA;
	
	
	public InfoAboutWinningOH(OperationalAgent opA) {
		this.opA = opA;
	}

	public void action() {
		// Receive the info message from TH containing info about the best price
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		
		ACLMessage msg = myAgent.receive(mt);
		
		if (msg != null && msg.getConversationId() == "announce_winner") {
			// Purchase order reply received
			if (msg.getPerformative() == ACLMessage.INFORM) {
//				String title = msg.getContent();
				int bestPrice = Integer.parseInt(msg.getContent());
				requiredSkill = opA.requiredSkill;
//				opA.taTime = bestPrice; // bestPrice + time it takes to complete all the TH tasks
				updateBestPriceCatalogue(requiredSkill, bestPrice);
			} else {
				System.out.println("Message performative mismatch");
			}
		} else {
			block();
		}
	}
	
	// updates the catalogue with the best price for the respective product
	public void updateBestPriceCatalogue(final String skill, final int bestPrice) {
		
		// check if this skill had a price in the best price catalogue and update it
		Integer previousBestPrice = (Integer) opA.bestPriceCatalogue.get(skill);
		
		if (previousBestPrice != null) {
			opA.bestPriceCatalogue.replace(skill, bestPrice);
//			System.out.println(requiredSkill + " changed price in " + myAgent.getLocalName()
//					+ " best price catalogue: " + opA.bestPriceCatalogue);
		} else {
			opA.bestPriceCatalogue.put(skill, bestPrice); // update the best price of the skill in the catalogue
//			System.out.println(myAgent.getLocalName() + " best price catalogue: " + opA.bestPriceCatalogue);
		}
	}
} 

