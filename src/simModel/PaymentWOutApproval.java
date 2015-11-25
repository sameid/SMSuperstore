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
		return model.rvp.uPaymentTm(this.icCustomer.paymentType);
	}

	@Override
	public void startingEvent() {
		//Get the ID of the CheckoutCounter, where the Customer can pay, since they have been served.
		this.id = model.udp.CanCustomerPay();
		this.icCustomer = model.rCheckouts[this.id].currentCustomer;	
		model.rCheckouts[this.id].customerIsPaying = true;
	}

	@Override
	protected void terminatingEvent() {

		//Now that the Customer has been served, bagged, and has payed, they can leave, and free up the respective CheckoutCounter
		model.rCheckouts[id].status = CheckoutCounter.Status.NOT_BUSY;
		model.rCheckouts[id].currentCustomer.payed = true;
		model.rCheckouts[this.id].customerIsPaying = false;
		//Update the output parameters
		model.udp.UpdateOutputs(this.icCustomer);
		
		
		
		//The Customers can now leave.
		icCustomer = null;
		model.rCheckouts[id].currentCustomer = null;
		
	}

}
