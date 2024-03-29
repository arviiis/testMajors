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
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage msg = myAgent.receive(mt); // receive INFORM msg from TH agent

		if (msg != null) {
			opA.thTime = Integer.parseInt(msg.getContent());
//			System.out.println("TA time in " + myAgent.getLocalName() + " memory: "+ opA.thTime);
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.INFORM);
			myAgent.send(reply);
		} else {
			block();
		}
	}

}
