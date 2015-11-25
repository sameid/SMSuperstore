package simModel;

import absmodJ.ConditionalActivity;

class BaggingByCashier extends ConditionalActivity{

	SMSuperstore model;
	Customer icCustomer;
	private int id;
	
	public static int call = 0;
	
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
		icCustomer = model.rCheckouts[this.id].currentCustomer;
		model.rCheckouts[this.id].cashierIsBagging = true;
		//Set the CheckoutCounter back to BUSY
		model.rCheckouts[this.id].status = CheckoutCounter.Status.BUSY;

		System.out.println("BaggingByCashier Started");
	}

	@Override
	protected void terminatingEvent() {
		//Now the customer has been bagged so set that flag to TRUE
		icCustomer.bagged = true;
		model.rCheckouts[id].currentCustomer.bagged = true;
		model.rCheckouts[this.id].cashierIsBagging = false;
		System.out.println("BaggingByCashier Terminated");
		//If the Customer's has to pay with a check, but they forgot their cashing card, they will be sent to
		//Supervisor so that their Payment can be approved.
		if(icCustomer.paymentType == Customer.PaymentType.CHECK_WITHOUT_CHECK_CASHING_CARD){
			model.rSupervisorQueue.add(icCustomer);
			model.rCheckouts[id].status = CheckoutCounter.Status.NOT_BUSY;
		}
		
	}

}
