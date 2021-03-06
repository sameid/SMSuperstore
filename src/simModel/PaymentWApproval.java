package simModel;

import absmodJ.ConditionalActivity;

class PaymentWApproval extends ConditionalActivity{

	SMSuperstore model;
	Customer icCustomer;
	
	public PaymentWApproval (SMSuperstore model){
		this.model = model;
	}
	
	protected static boolean precondition (SMSuperstore model){
		return model.rSupervisor.status == Supervisor.Status.NOT_BUSY && !model.qSupervisorQueue.isEmpty();
	}
	
	@Override
	protected double duration() {
		
		return model.rvp.uPaymentTm(icCustomer.paymentType);
	}

	@Override
	public void startingEvent() {
		//Simply removing the Customer from the SupervisorQueue, and setting the Supervisor checkout to BUSY
		icCustomer = model.qSupervisorQueue.remove();
		model.rSupervisor.status = Supervisor.Status.BUSY; 
	}

	@Override
	protected void terminatingEvent() {
		//Supervisor is no longer BUSY and can service another Customer
		model.rSupervisor.status = Supervisor.Status.NOT_BUSY;

		//The Customer has now left the store
		icCustomer = null;
		
	}

}
