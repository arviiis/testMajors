package behaviours;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WorkAnnouncementHandler extends CyclicBehaviour {

	// The catalogue of type of products the OH can work with (maps the wo to it
	// price (price changes dynamically))
	private Hashtable catalogue;
	private String[] skills; // skills that the OH agent is capable of fulfilling

	public WorkAnnouncementHandler(Hashtable catalogue, String[] skills) {
		this.catalogue = catalogue;
		this.skills = skills;
	}

	public void action() {
		// receive message with CFP performative
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) {
			// CFP Message received. Process it
			String requiredSkill = msg.getContent();
			ACLMessage reply = msg.createReply();
			boolean i = true;
			// generate random price for the work
			int price = ThreadLocalRandom.current().nextInt(0, 30 + 1);		
			
			if (hasSkill(skills, requiredSkill)) {
				// check if there is a time slot in the schedule
				if (i) {
					// calculate price and send a proposal
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(Integer.toString(price));
					System.out.println("Propose message sent to TH agent from: " + myAgent.getLocalName());
				
				} 
			} else {
					// The OH cannot complete the wo
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent("not-available");
					System.out.println("Refuse message sent to TH agent from: " + myAgent.getLocalName());
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
	
}
