package simModel;

import absmodJ.ScheduledAction;

public class Arrivals extends ScheduledAction{

	SMSuperstore model;
	
	public Arrivals(SMSuperstore model){
		this.model = model;
	}
	
	@Override
	protected double timeSequence() {
		// TODO Auto-generated method stub
		return model.rvp.duC();
	}

	@Override
	protected void actionEvent() {
		// TODO Auto-generated method stub
		
	
	}



}
