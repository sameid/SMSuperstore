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
		double clock = model.getClock();	
		//All logic for determining how the schedule should be 
		//change found in the UDP
		model.udp.SchedulePersonnel(clock);
	}
}
