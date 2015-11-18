package simModel;

import absmodJ.ConditionalActivity;

class PaymentWOutApproval extends ConditionalActivity{

	SMSuperstore model;
	Customer icCustomer;
	int id;
	
	public PaymentWOutApproval (SMSuperstore model){
		this.model = model;
	}
	
	protected static boolean precondition(SMSuperstore model){
		return model.udp.CanCustomerPay() != -1;
	}
	
	@Override
	protected double duration() {
		// TODO Auto-generated method stub
		return model.rvp.uPaymentTm(model.rCheckouts[this.id].currentCustomer.paymentType);
	}

	@Override
	public void startingEvent() {
		// TODO Auto-generated method stub
		this.icCustomer = model.rCheckouts[this.id].currentCustomer;	
	}

	@Override
	protected void terminatingEvent() {
		// TODO Auto-generated method stub
		model.rCheckouts[id].status = Checkout.Status.NOT_BUSY;
		if (model.getClock() - icCustomer.startWait > 15) model.output.numLongWait++;
		model.output.numServed++;
		icCustomer = null;
		model.rCheckouts[id].currentCustomer = null;
		
	}

}
