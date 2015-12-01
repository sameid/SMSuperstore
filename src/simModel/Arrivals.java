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
		
		//Create a new customer and initialize it's attributes.
		Customer icCustomer = new Customer();
		icCustomer.numberOfItems = model.rvp.uNumItem();
		icCustomer.paymentType = model.rvp.uPaymentType(icCustomer.numberOfItems);
		icCustomer.startWait = model.getClock();
		icCustomer.isServed = false;
		icCustomer.isBagged = false;
		icCustomer.isPayed = false;
		
		int id = model.udp.GetShortestQueue();
		
		//Add the customer to the CustomerQueue with the least number of people	
		model.rCheckoutQueues[id].add(icCustomer);
	}
}
