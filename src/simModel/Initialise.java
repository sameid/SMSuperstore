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
		for (int i = 0 ; i < model.rCheckoutCounters.length ; i++){
			model.rCheckoutCounters[i] = new CheckoutCounter();
			model.rCheckoutCounters[i].status = CheckoutCounter.Status.UNATTENDED;
			model.qCheckoutQueues[i] = new LinkedList<Customer>();
		}
		
		model.rSupervisor.status = Supervisor.Status.NOT_BUSY;
		
		for (int i = 0 ; i < Constants.NUM_TIME_SLOTS ; i++){
			model.output.numLongWait[i] = 0.0;
			model.output.numServed[i] = 0.0;
			model.output.propLongWait[i] = 0.0;
		}
		
	}
	

}
