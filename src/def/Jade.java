package def;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Jade {

	static Runtime rt = Runtime.instance();
	static Profile p = new ProfileImpl();
	static ContainerController cc;
	static AgentController ac;

	int shAgent_qty = 1;

	public Jade() {
		runJade();
	}

	public void runJade() {
		// create Jade runtime instance
		// set profile parameters
		// create and start agents
		p.setParameter(Profile.MAIN_HOST, "localhost");
		p.setParameter(Profile.GUI, "true");
		p.setParameter(Profile.PLATFORM_ID, "Platform1");
		cc = rt.createMainContainer(p);

		// Adds SH agent to the runtime instance
		addShAgent(shAgent_qty);

	}

	public static void addPhAgent(int qty, String pType) {

		String agentName = "PH Agent_" + pType;

		Object[] args = new String[3];
		args[0] = pType;
//		args[1] = "arg2"; // optional arguments
//		args[2] = "argument3";

		for (int i = 0; i < qty; i++) {
			try {
				// Start a product agent for each product that was ordered from GUI
				ac = cc.createNewAgent(agentName + (i + 1), "agents.ProductAgent", args);

//				// Define the type of agent to start
//				if (pType == "A") {
//
//				} else if (pType == "B") {
//					ac = cc.createNewAgent(agentName + (i + 1), "agents.ProductAgent", args);
//				} else {
//					ac = cc.createNewAgent(agentName + (i + 1), "agents.ProductAgent", args);
//				}
				// Start the agents
				ac.start();
			} catch (StaleProxyException spe) {
				// TODO: handle exception
				spe.printStackTrace();
				System.out.println(spe.getMessage());
			}
		}
	}

	// Create Task holon agent
	public static void addThAgent() {

	}

	// Create Supervisor holon agent
	static void addShAgent(int qty) {

		for (int i = 0; i < qty; i++) {
			try {
				// Add SH agent to the platform
				ac = cc.createNewAgent("SH agent_" + (i + 1), "agents.SupervisorAgent", null);
				// Start the agents
				ac.start();
			} catch (StaleProxyException spe) {
				// TODO: handle exception
				spe.printStackTrace();
				System.out.println(spe.getMessage());
			}
		}
	}

	// Create Operational holon agent
	static void addOhAgent(int qty) {

		for (int i = 0; i < qty; i++) {
			try {
				// Add SH agent to the platform
				ac = cc.createNewAgent("OH agent_" + (i + 1), "agents.OperationalAgent", null);
				// Start the agents
				ac.start();
			} catch (StaleProxyException spe) {
				// TODO: handle exception
				spe.printStackTrace();
				System.out.println(spe.getMessage());
			}
		}
	}

}
