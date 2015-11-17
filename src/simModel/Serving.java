package simModel;

import absmodJ.ConditionalActivity;

class Serving extends ConditionalActivity{

	SMSuperstore model;
	Checkout checkout;
	
	public Serving (SMSuperstore model, Checkout checkout){
		this.model = model;
		this.checkout = checkout;
	}
	
	protected static boolean precondition(Checkout checkout){
		if (checkout.status == Checkout.Status.NOT_BUSY && !checkout.rCheckoutQueue.isEmpty()) return true;
		return false;
	}
	
	@Override
	protected double duration() {
		int numOfItems = checkout.currentCustomer.numberOfItems;
		return model.rvp.uCheckoutTm(numOfItems) + model.rvp.uPriceCheckTm(numOfItems);
	}

	@Override
	public void startingEvent() {
		// TODO Auto-generated method stub
		Customer icCustomer = null;
		icCustomer = checkout.rCheckoutQueue.remove();
		if (icCustomer != null ) checkout.currentCustomer = icCustomer;
		checkout.status = Checkout.Status.BUSY;
		if (model.rgBaggers.numAvailable > 0){
			model.rgBaggers.numAvailable--;
			checkout.baggerPresent = true;
		}
		
	}

	@Override
	protected void terminatingEvent() {
		// TODO Auto-generated method stub

		Customer icCustomer = checkout.currentCustomer;
		
		icCustomer.served = true;
		
		if (checkout.baggerPresent){
			model.rgBaggers.numAvailable++;
			checkout.baggerPresent = false;
			icCustomer.bagged = true;
		}
		
		if(icCustomer.paymentType == Customer.PaymentType.CHECK_WITHOUT_CHECK_CASHING_CARD){
			model.rSupervisorQueue.add(icCustomer);
			checkout.status = Checkout.Status.NOT_BUSY;
		}
		
	}

}
