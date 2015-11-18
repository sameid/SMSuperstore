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
		
		int min = 0;
		int minIndex = 0;
		for (int i = 0; i < model.rCheckouts.length; i++){
			Checkout c = model.rCheckouts[i]; 
			if (c != null && c.status == Checkout.Status.BUSY || c.status == Checkout.Status.NOT_BUSY){
				if (model.rCheckoutQueues[i].customers.size() < min){
					minIndex = i;
				}
			}
		}
		
		model.rCheckoutQueues[minIndex].customers.add(icCustomer);
	}



}
