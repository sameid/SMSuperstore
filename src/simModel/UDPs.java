package simModel;

class UDPs 
{
	SMSuperstore model;  // for accessing the clock
	
	// Constructor
	protected UDPs(SMSuperstore model) { this.model = model; }

	// Translate User Defined Procedures into methods
    /*-------------------------------------------------
	                       Example
	    protected int ClerkReadyToCheckOut()
        {
        	int num = 0;
        	Clerk checker;
        	while(num < model.NumClerks)
        	{
        		checker = model.Clerks[num];
        		if((checker.currentstatus == Clerk.status.READYCHECKOUT)  && checker.list.size() != 0)
        		{return num;}
        		num +=1;
        	}
        	return -1;
        }
	------------------------------------------------------------*/
	
	protected int CanCheckoutServe (){
		
		for (int i = 0 ; i < model.rCheckouts.length ; i++){
			if (model.rCheckouts[i].status == Checkout.Status.NOT_BUSY && !model.rCheckoutQueues[i].customers.isEmpty()){
				return i;
			}
		}
		
		return -1;
	}
	
	protected int ShouldCashierBag (){
		
		for (int i = 0; i < model.rCheckouts.length ; i++){
			Checkout c = model.rCheckouts[i];
			if (c.currentCustomer != null 
					&& c.currentCustomer.served 
					&& !c.currentCustomer.bagged
					&& model.rgBaggers.numAvailable == 0){
				return i;
			}
		}
		
		return -1;
	}
	
	protected int CanCustomerPay (){
		for (int i = 0; i < model.rCheckouts.length ; i++){
			Checkout c = model.rCheckouts[i];
			if (c.currentCustomer != null 
					&& c.currentCustomer.served 
					&& c.currentCustomer.bagged
					&& c.status == Checkout.Status.BUSY
					&& c.currentCustomer.paymentType != Customer.PaymentType.CHECK_WITHOUT_CHECK_CASHING_CARD){
				return i;
			}
		}
		
		return -1;
	}
	
}
