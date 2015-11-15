package simModel;

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
		for (int i = 0 ; i < model.rCheckoutQueues.size() ; i++){
			model.rCheckoutQueues.get(i).rCheckoutQueue.clear();
			model.rCheckoutQueues.get(i).baggerPresent = false;
		}
		model.rSupervisorQueue.clear();
		model.rCheckoutQueues.clear(); 
		model.output.numLongWait = 0;
		model.output.numShopped = 0;
	}
	

}
