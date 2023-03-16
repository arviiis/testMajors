package behaviours;

import java.util.Hashtable;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;



public class InfoAboutWinningOH extends CyclicBehaviour {
	
	private Hashtable bestPriceCatalogue;
	
	public InfoAboutWinningOH(Hashtable bestPriceCatalogue) {
		this.bestPriceCatalogue = bestPriceCatalogue;
	}

	public void action() {
		// Receive the purchase order reply
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) {
			// Purchase order reply received
			if (msg.getPerformative() == ACLMessage.INFORM) {
//						String title = msg.getContent();
				int price = Integer.parseInt(msg.getContent());
				updateBestPriceCatalogue("Job1", price);
			} else {
				System.out.println("something");
			}
		} else {
			block();
		}
	}
	
	// updates the catalogue with the best price for the respective product
	public void updateBestPriceCatalogue(final String title, final int bestPrice) {
		
		// get the previously saved best price for the product
		Integer previousBest = (Integer) bestPriceCatalogue.get(title);
		
		if (previousBest != null) {
			bestPriceCatalogue.replace(title, new Integer(bestPrice));
//			System.out.println(title+" replaced in best price catalogue. New best price = "+ bestPrice);
		}
		else {
			bestPriceCatalogue.put(title, new Integer(bestPrice));
//			System.out.println(title+" inserted into best price catalogue. New best price = "+bestPrice);
		}
	}
} 

