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
		return model.udp.ShouldCashierBag() != -1;
	}
	
	@Override
	protected double duration() {
		// TODO Auto-generated method stub
		return model.rvp.uBaggingTm(model.rCheckouts[this.id].currentCustomer.numberOfItems);
	}

	@Override
	public void startingEvent() {
		// TODO Auto-generated method stub
		this.id = model.udp.ShouldCashierBag();
		icCustomer = model.rCheckouts[this.id].currentCustomer;
		model.rCheckouts[this.id].status = Checkout.Status.BUSY;
		
	}

	@Override
	protected void terminatingEvent() {
		// TODO Auto-generated method stub
		icCustomer.bagged = true;
		if(icCustomer.paymentType == Customer.PaymentType.CHECK_WITHOUT_CHECK_CASHING_CARD){
			model.rSupervisorQueue.add(icCustomer);
			model.rCheckouts[id].status = Checkout.Status.NOT_BUSY;
		}
		
	}

}
