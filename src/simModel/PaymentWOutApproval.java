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
		this.icCustomer = model.rCheckoutCounters[this.id].currentCustomer;	
		model.rCheckoutCounters[this.id].isPaying = true;
	}

	@Override
	protected void terminatingEvent() {

		//Now that the Customer has been served, bagged, and has payed, they can leave, and free up the respective CheckoutCounter
		//or if the checkout is closing and the queue is empty, the cashier will leave
		if(model.rCheckoutCounters[id].isClosing && model.rCheckoutQueues[id].isEmpty()){
			model.rCheckoutCounters[id].status = CheckoutCounter.Status.UNATTENDED;
		} 
		else{
			model.rCheckoutCounters[id].status = CheckoutCounter.Status.NOT_BUSY;
		}
		model.rCheckoutCounters[id].currentCustomer.isPayed = true;
		model.rCheckoutCounters[id].isPaying = false;
		
		//The Customers can now leave.
		icCustomer = null;
		model.rCheckoutCounters[id].currentCustomer = null;
		
	}

}
