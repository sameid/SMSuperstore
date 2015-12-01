package simModel;

import absmodJ.ConditionalActivity;

class BaggingByCashier extends ConditionalActivity{

	SMSuperstore model;
	Customer icCustomer;
	private int id;
	
	
	public static int call = 0;//Not apart of the model, simply used for debugging
	
	public BaggingByCashier (SMSuperstore model){
		this.model = model;
	}
	
	public static boolean precondition (SMSuperstore model){
		int test = model.udp.ShouldCashierBag();
		return test!= Constants.NONE;
	}
	
	@Override
	protected double duration() {
		return model.rvp.uBaggingTm(icCustomer.numberOfItems);
	}

	@Override
	public void startingEvent() {
		//The followiing will get the id of the CheckoutCounter that requires it's Employee to bag customer items 
		this.id = model.udp.ShouldCashierBag();
		//Set the CheckoutCounter back to BUSY
		model.rCheckoutCounters[this.id].status = CheckoutCounter.Status.BUSY;
		icCustomer = model.rCheckoutCounters[this.id].currentCustomer;
		model.rCheckoutCounters[this.id].isBagging = true;

	}

	@Override
	protected void terminatingEvent() {
		//Now the customer has been bagged so set that flag to TRUE
		icCustomer.isBagged = true;
		model.rCheckoutCounters[this.id].isBagging = false;
		//If the Customer's has to pay with a check, but they forgot their cashing card, they will be sent to
		//Supervisor so that their Payment can be approved.
		if(icCustomer.paymentType == Customer.PaymentType.CHECK_WITHOUT_CHECK_CASHING_CARD){
			model.rSupervisorQueue.add(icCustomer);
			
			//Now that the Customer has been served, bagged, and has payed, they can leave, and free up the respective CheckoutCounter
			//or if the checkout is closing and the queue is empty, the cashier will leave
			if(model.rCheckoutCounters[id].isClosing && model.rCheckoutQueues[id].isEmpty()){
				model.rCheckoutCounters[id].status = CheckoutCounter.Status.UNATTENDED;
			} 
			else{
				model.rCheckoutCounters[id].status = CheckoutCounter.Status.NOT_BUSY;
			}
		}
		
	}

}
