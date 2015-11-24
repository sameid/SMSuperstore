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
		// TODO Auto-generated method stub
		return model.rvp.uPaymentTm(model.rSupervisor.currentCustomer.paymentType);
	}

	@Override
	public void startingEvent() {
		// TODO Auto-generated method stub
		icCustomer = model.rSupervisorQueue.remove();
		model.rSupervisor.status = Supervisor.Status.BUSY; 
	}

	@Override
	protected void terminatingEvent() {
		// TODO Auto-generated method stub
		
		model.rSupervisor.status = Supervisor.Status.NOT_BUSY;

		model.udp.UpdateOutputs(this.icCustomer);
		
		icCustomer = null;
		model.rSupervisor.currentCustomer = null;
		
	}

}
