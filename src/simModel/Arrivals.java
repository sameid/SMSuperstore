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
		
		//Find the CheckoutQueue with the least number of customers in it, that is serving. 
		int min = -1;
		int checkoutSelected = 0;
		for (int i = 0; i < model.rCheckouts.length; i++){
			CheckoutCounter c = model.rCheckouts[i]; 
			
			//If the customer finds a checkout with an idle cashier, it is the appropriate choice
			//A queue can be empty but the cashier busy, so selecting a not busy cashier instead is preferable
			if (c.status == CheckoutCounter.Status.NOT_BUSY && model.rCheckoutQueues[i].isEmpty()){
				checkoutSelected = i;
				break;
			}
			//This if condition will ensure that the Checkout has an employee that is currently serving customers.
			if (c.status != CheckoutCounter.Status.UNATTENDED && !c.isClosing){
				if (model.rCheckoutQueues[i].size() < min || min == -1){
					min = model.rCheckoutQueues[i].size();
					checkoutSelected = i;
				}
			}
		}
		
		//Add the customer to the CustomerQueue with the least number of people	
		model.rCheckoutQueues[checkoutSelected].add(icCustomer);
	}
}
