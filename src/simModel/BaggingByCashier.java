package simModel;

import absmodJ.ConditionalActivity;

class BaggingByCashier extends ConditionalActivity{

	SMSuperstore model;
	Customer icCustomer;
	private int id;
	
	public BaggingByCashier (SMSuperstore model){
		this.model = model;
	}
	
	public static boolean precondition (SMSuperstore model){
		
		int test = model.udp.ShouldCashierBag();
		return test!= Constants.NONE;
	}
	
	@Override
	protected double duration() {
		// TODO Auto-generated method stub
		return model.rvp.uBaggingTm(icCustomer.numberOfItems);
	}

	@Override
	public void startingEvent() {
		// TODO Auto-generated method stub
		this.id = model.udp.ShouldCashierBag();
		icCustomer = model.rCheckouts[this.id].currentCustomer;
		model.rCheckouts[this.id].status = CheckoutCounter.Status.BUSY;
		
	}

	@Override
	protected void terminatingEvent() {
		// TODO Auto-generated method stub
		icCustomer.bagged = true;
		if(icCustomer.paymentType == Customer.PaymentType.CHECK_WITHOUT_CHECK_CASHING_CARD){
			model.rSupervisorQueue.add(icCustomer);
			model.rCheckouts[id].status = CheckoutCounter.Status.NOT_BUSY;
		}
		
	}

}
