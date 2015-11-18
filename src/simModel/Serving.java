package simModel;

import absmodJ.ConditionalActivity;

class Serving extends ConditionalActivity{

	SMSuperstore model;
	Customer icCustomer;
	int id;
	
	public Serving (SMSuperstore model){
		this.model = model;
	}
	
	protected static boolean precondition(SMSuperstore model){
		return model.udp.CanCheckoutServe() != Constants.NONE;
	}
	
	@Override
	protected double duration() {
		int numOfItems = icCustomer.numberOfItems;
		return model.rvp.uCheckoutTm(numOfItems);
	}

	@Override
	public void startingEvent() {
		// TODO Auto-generated method stub
		this.id = model.udp.CanCheckoutServe();
		icCustomer = model.rCheckoutQueues[id].customers.remove();
		model.rCheckouts[id].currentCustomer = icCustomer;
		model.rCheckouts[id].status = Checkout.Status.BUSY;
		if (model.rgBaggers.numAvailable > 0){
			model.rgBaggers.numAvailable--;
			model.rCheckouts[id].baggerPresent = true;
		}
		
	}

	@Override
	protected void terminatingEvent() {
		// TODO Auto-generated method stub

		icCustomer.served = true;
		
		if (model.rCheckouts[id].baggerPresent){
			model.rgBaggers.numAvailable++;
			model.rCheckouts[id].baggerPresent = false;
			icCustomer.bagged = true;
			
			if(icCustomer.paymentType == Customer.PaymentType.CHECK_WITHOUT_CHECK_CASHING_CARD){
				model.rSupervisorQueue.add(icCustomer);
				model.rCheckouts[id].status = Checkout.Status.NOT_BUSY;
				model.rCheckouts[id].currentCustomer = null;
			}
		}
		
		
		
	}

}
