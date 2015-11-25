package simModel;

import absmodJ.ConditionalActivity;

class PaymentWApproval extends ConditionalActivity{

	SMSuperstore model;
	Customer icCustomer;
	
	public PaymentWApproval (SMSuperstore model){
		this.model = model;
	}
	
	protected static boolean precondition (SMSuperstore model){
		return model.rSupervisor.status == Supervisor.Status.NOT_BUSY && !model.rSupervisorQueue.isEmpty();
	}
	
	@Override
	protected double duration() {
		return model.rvp.uPaymentTm(model.rSupervisor.currentCustomer.paymentType);
	}

	@Override
	public void startingEvent() {
		//Simply removing the Customer from the SupervisorQueue, and setting the Supervisor checkout to BUSY
		icCustomer = model.rSupervisorQueue.remove();
		model.rSupervisor.status = Supervisor.Status.BUSY; 
	}

	@Override
	protected void terminatingEvent() {
		//Supervisor is no longer BUSY and can service another Customer
		model.rSupervisor.status = Supervisor.Status.NOT_BUSY;

		//Update the output parameters
		model.udp.UpdateOutputs(this.icCustomer);
		
		//The Customer has now left the store
		icCustomer = null;
		model.rSupervisor.currentCustomer = null;
		
	}

}
