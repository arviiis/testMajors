package behaviours;

import java.util.ArrayList;
import java.util.List;

import agents.OperationalAgent;
import def.Main;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class TaTimeHandler extends CyclicBehaviour {

	OperationalAgent opA;

	public TaTimeHandler(OperationalAgent opA) {
		this.opA = opA;
	}

	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
		ACLMessage msg = myAgent.receive(mt); // receive INFORM msg from TH agent

		if (msg != null) {
			opA.taTime = Integer.parseInt(msg.getContent());
//			System.out.println(myAgent.getLocalName() + " received TH time");
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.AGREE);
			
			int skillMfgTime = (Integer) opA.catalogue.get(opA.requiredSkill);
//			System.out.println("Time added to TH occupied time: "+ skillMfgTime);
			reply.setContent(Integer.toString(skillMfgTime));
			myAgent.send(reply);
		} else {
			block();
		}
	}

}
