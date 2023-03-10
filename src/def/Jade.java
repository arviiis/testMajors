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
		// Adds OH agents to the runtime instance
		addOhAgents();

	}

	public static void addPhAgent(int qty, String pType) {

		String agentName = "PH Agent_" + pType;

		Object[] args = new String[3];
		args[0] = pType; // one of 3 options: A, B, C.

		for (int i = 0; i < qty; i++) {
			try {
				// Start a product agent for each product that was ordered from GUI
				ac = cc.createNewAgent(agentName + (i + 1), "agents.ProductAgent", args);
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
	public static void addThAgent(int qty) {
		for (int i = 0; i < qty; i++) {
			try {
				// Add SH agent to the platform
				ac = cc.createNewAgent("TH agent_" + (i + 1), "agents.TaskAgent", null);
				// Start the agents
				ac.start();
			} catch (StaleProxyException spe) {
				// TODO: handle exception
				spe.printStackTrace();
				System.out.println(spe.getMessage());
			}
		}
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
	static void addOhAgent(int id, String type, String[] products, int[] mfgTime) {

		Object[] args = new Object[3];
		args[0] = type;
		args[1] = products;
		args[2] = mfgTime;

		try {
				// Add SH agent to the platform
				ac = cc.createNewAgent("OH agent"+id, "agents.OperationalAgent", args);
			// Start the agents
			ac.start();
		} catch (StaleProxyException spe) {
			// TODO: handle exception
			spe.printStackTrace();
			System.out.println(spe.getMessage());
		}
	}
	
	// define the type of OH agents to add
	static void addOhAgents() {
		
		String[] products = { "none" };
		String[] products1 = { "pA" };
		String[] products2 = { "pB" };
		String[] products3 = { "pC" };
		String[] products4 = { "pA", "pB" };
		
		int[] mfgTime = {0};
		int[] mfgTime1 = {25};
		int[] mfgTime2 = {45};
		int[] mfgTime3 = {30};
		int[] mfgTime4 = {35,30};
		int[] mfgTime5 = {10};
		
		addOhAgent(1,"stacker", products1, mfgTime1);
		addOhAgent(2,"stacker", products2, mfgTime2);
		addOhAgent(3,"stacker", products3, mfgTime3);
		addOhAgent(4,"stacker", products4, mfgTime4);

		addOhAgent(5,"wrapper", products1, mfgTime5);
		addOhAgent(6,"mover", products, mfgTime);
	}

}
