package simModel;

import absmodJ.ConditionalActivity;

class Checkout extends ConditionalActivity{

	SMSuperstore model;
	Customer icCustomer;
	int id;
	
	public Checkout (SMSuperstore model){
		this.model = model;
	}
	
	protected static boolean precondition(SMSuperstore model){
		return model.udp.CanCheckoutServe() != Constants.NONE;
	}
	
	@Override
	protected double duration() {
		return model.rvp.uCheckoutTm(icCustomer.numberOfItems);
	}

	@Override
	public void startingEvent() {
		//We use the UDP.CanCheckoutServe() to get the id of the CheckoutCounter that has an employee that is not busy and there 
		//are Customers in the Queue
		this.id = model.udp.CanCheckoutServe();
		//We then remove a customer from the Queue
		this.icCustomer = model.qCheckoutQueues[id].remove();
		//The Customer that we removed from the Queue is now the currentCustomer of the CheckoutCounter
		model.rCheckoutCounters[id].currentCustomer = this.icCustomer;
		//Update the output parameters based on the fact that the customer is now done waiting
		model.udp.UpdateOutputs(this.icCustomer);
		//The Employee at the CheckoutCounter is now BUSY
		model.rCheckoutCounters[id].status = CheckoutCounter.Status.BUSY;
		
		//If there are any baggers available, we take one of them to bag at the current CheckoutCounter
		if (model.rgBaggers.numAvailable > 0){
			model.rgBaggers.numAvailable--;
			model.rCheckoutCounters[id].baggerPresent = true;
		}
	}

	@Override
	protected void terminatingEvent() {
		//On terminating the Checkout activity, the Customer has now served
		icCustomer.isServed = true;

		//If there was a bagger present at this instance of the activity, the bagger is now made available again
		if (model.rCheckoutCounters[id].baggerPresent){
			model.rgBaggers.numAvailable++;
			model.rCheckoutCounters[id].baggerPresent = false;
	
			//Since there was a bagger present at the CheckoutCounter, they Customer has also been bagged.
			icCustomer.isBagged = true;
			
			//If the Customer is trying to pay with a Check, but does not have their check cashing card, they will need
			//Supervisor approval, and so the Customer will be sent to the SupervisorQueue.
			if(icCustomer.paymentType == Customer.PaymentType.CHECK_WITHOUT_CHECK_CASHING_CARD){
				model.qSupervisorQueue.add(icCustomer);
				
				//The current CheckoutCounter is no longer interested with the current customer, and can continue serving
				//or if they are closing and their queue is empty, the cashier will leave
				if(model.rCheckoutCounters[id].isClosing && model.qCheckoutQueues[id].isEmpty()){
					model.rCheckoutCounters[id].status = CheckoutCounter.Status.UNATTENDED;
				} 
				else{
					model.rCheckoutCounters[id].status = CheckoutCounter.Status.NOT_BUSY;
				}				
				model.rCheckoutCounters[id].currentCustomer = null;
			}
		}
		
	}

}
