package simModel;

import java.util.LinkedList;

import absmodJ.ScheduledAction;


class Initialise extends ScheduledAction
{
	SMSuperstore model;
	
	// Constructor
	protected Initialise(SMSuperstore model) { this.model = model; }

	double [] ts = { 0.0, -1.0 }; // -1.0 ends scheduling
	int tsix = 0;  // set index to first entry.
	protected double timeSequence() 
	{
		return ts[tsix++];  // only invoked at t=0
	}

	protected void actionEvent() 
	{
		// System Initialisation
        // Add initilisation instructions
//		for (int i = 0 ; i < model.rCheckouts.size() ; i++){
//			model.rCheckouts.get(i).rCheckoutQueue.clear();
//		}
//		model.rSupervisorQueue.clear();
		
		for (int i = 0 ; i < model.rCheckouts.length ; i++){
			model.rCheckouts[i] = new CheckoutCounter();
			model.rCheckoutQueues[i] = new LinkedList<Customer>();
		}
		
		model.output.numLongWait = 0;
		model.output.numServed = 0;
	}
	

}
