package simModel;

import absmodJ.ConditionalActivity;

class BaggingByCashier extends ConditionalActivity{

	SMSuperstore model;
	Checkout checkout;
	
	public BaggingByCashier (SMSuperstore model, Checkout checkout){
		this.model = model;
		this.checkout = checkout;
	}
	
	public static boolean precondition (SMSuperstore model, Checkout checkout){
		return model.rgBaggers.numAvailable == 0 
				&& !checkout.baggerPresent 
				&& checkout.currentCustomer.served 
				&& !checkout.currentCustomer.bagged;
	}
	
	@Override
	protected double duration() {
		// TODO Auto-generated method stub
		return model.rvp.uBaggingTm(checkout.currentCustomer.numberOfItems);
	}

	@Override
	public void startingEvent() {
		// TODO Auto-generated method stub
		checkout.status = Checkout.Status.BUSY;
		
	}

	@Override
	protected void terminatingEvent() {
		// TODO Auto-generated method stub
		Customer icCustomer = checkout.currentCustomer;
		if(icCustomer.paymentType == Customer.PaymentType.CHECK_WITHOUT_CHECK_CASHING_CARD){
			model.rSupervisorQueue.add(icCustomer);
			checkout.status = Checkout.Status.NOT_BUSY;
		}
		
	}

}
