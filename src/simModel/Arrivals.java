package simModel;

import absmodJ.ScheduledAction;

public class Arrivals extends ScheduledAction{

	SMSuperstore model;
	
	public Arrivals(SMSuperstore model){
		this.model = model;
	}
	
	@Override
	protected double timeSequence() {
		return model.rvp.duC();
	}

	@Override
	protected void actionEvent() {
		Customer icCustomer = new Customer();
		icCustomer.numberOfItems = model.rvp.uNumItem();
		icCustomer.paymentType = model.rvp.uPaymentType(icCustomer.numberOfItems);
		icCustomer.startWait = model.getClock();
		
		int min = -1;
		int minIndex = 0;
		for (int i = 0; i < model.rCheckouts.length; i++){
			CheckoutCounter c = model.rCheckouts[i]; 
			
			if (c.status == CheckoutCounter.Status.BUSY || c.status == CheckoutCounter.Status.NOT_BUSY){
				
				
				if (model.rCheckoutQueues[i].size() < min || min == -1){
					
					min = model.rCheckoutQueues[i].size();
					minIndex = i;
				}
			}
		}
		
		
		model.rCheckoutQueues[minIndex].add(icCustomer);
	}



}
