package simModel;

import absmodJ.ScheduledAction;

public class StaffChange extends ScheduledAction {

	SMSuperstore model;  // reference to model object
	public StaffChange(SMSuperstore model) { this.model = model; }

	// Implementation of timeSequence
	//                                     2:00 pm, 3:30 pm, 6:00 pm, 8:30 pm
	private double[] staffChangeTimeSeq = {0.0,        90.0,       240.0,     390.0, -1 };
	private int sctIx = 0;
	public double timeSequence() 
	{ 
		double nxtTime = staffChangeTimeSeq[sctIx];
		sctIx++;
		return(nxtTime); 
	}

	// SchedEmp Event
	protected void actionEvent()
	{
//		double clock = (int)model.getClock();
		double clock = model.getClock();
		
	    if(clock == staffChangeTimeSeq[0])
	    {
	    	model.rgBaggers.numAvailable = model.udp.ChangeNumOfBaggers(1);
	    	model.udp.ChangeNumOfCashiers(1);
	    }
	    else if(clock == staffChangeTimeSeq[1]) 
	    {
	    	model.rgBaggers.numAvailable = model.udp.ChangeNumOfBaggers(2);
	    	model.udp.ChangeNumOfCashiers(2);
	    }
	    else if(clock == staffChangeTimeSeq[2])
	    {
	    	model.rgBaggers.numAvailable = model.udp.ChangeNumOfBaggers(3);
	    	model.udp.ChangeNumOfCashiers(3);
	    }
	    else if( clock == staffChangeTimeSeq[3])
	    {
	    	model.rgBaggers.numAvailable = model.udp.ChangeNumOfBaggers(4);
	    	model.udp.ChangeNumOfCashiers(4);
	    }
	    else System.out.println("Invalid time to schedule employees:"+model.getClock());
	}
}
