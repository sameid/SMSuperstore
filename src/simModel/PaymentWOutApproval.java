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
		return model.udp.CanCustomerPay() != Constants.NONE;
	}
	
	@Override
	protected double duration() {
		// TODO Auto-generated method stub
		return model.rvp.uPaymentTm(this.icCustomer.paymentType);
	}

	@Override
	public void startingEvent() {
		// TODO Auto-generated method stub
		this.id = model.udp.CanCustomerPay();
		this.icCustomer = model.rCheckouts[this.id].currentCustomer;	
	}

	@Override
	protected void terminatingEvent() {
		// TODO Auto-generated method stub

		model.rCheckouts[id].status = CheckoutCounter.Status.NOT_BUSY;

		model.udp.UpdateOutputs(this.icCustomer);
		
		icCustomer = null;
		model.rCheckouts[id].currentCustomer = null;
		
	}

}
