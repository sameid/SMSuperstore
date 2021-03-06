package simModel;

import simModel.CheckoutCounter.Status;

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
	
	protected final static double[] TIME_SLOTS = {90.0, 240.0, 390.0, 480.0}; 
	
	protected int CanCheckoutServe (){
		
		for (int i = 0 ; i < model.rCheckoutCounters.length ; i++){
			if (model.rCheckoutCounters[i].status == CheckoutCounter.Status.NOT_BUSY && !model.qCheckoutQueues[i].isEmpty()){
				return i;
			}
		}

		return Constants.NONE;
	}
	
	protected int ShouldCashierBag (){
		
		for (int i = 0; i < model.rCheckoutCounters.length ; i++){
			CheckoutCounter c = model.rCheckoutCounters[i];
			if (c.currentCustomer != null 
					&& c.currentCustomer.isServed 
					&& !c.currentCustomer.isBagged
					&& !c.isBagging){
				
				return i;
			}
		}

		return Constants.NONE;
	}
	
	protected int CanCustomerPay (){
		for (int i = 0; i < model.rCheckoutCounters.length ; i++){
			CheckoutCounter c = model.rCheckoutCounters[i];
			if (c.currentCustomer != null 
					&& c.currentCustomer.isServed 
					&& c.currentCustomer.isBagged
					&& !c.currentCustomer.isPayed
					&& c.status == CheckoutCounter.Status.BUSY
					&& c.currentCustomer.paymentType != Customer.PaymentType.CHECK_WITHOUT_CHECK_CASHING_CARD
					&& !c.isPaying){
				return i;
			}
		}
		
		return Constants.NONE;
	}
	
	protected void UpdateOutputs (Customer current){
		int index = 0;
		for(int i = 0 ; i < Constants.NUM_TIME_SLOTS ; i++){
			if (model.getClock() < TIME_SLOTS[i]) {
				index = i;
				break;
			}
		}

		if ((model.getClock() - current.startWait) > 15.0) model.output.numLongWait[index]++;
		model.output.numServed[index]++;
		model.output.propLongWait[index] = model.output.numLongWait[index]/model.output.numServed[index];
		
		double overallNumLongWait = 0;
		double overallNumServed = 0;
		for(int i=0; i < model.output.numLongWait.length; i++ ){
			overallNumLongWait += model.output.numLongWait[i];
			overallNumServed += model.output.numServed[i];
		}
		model.output.overallPropLongWait = overallNumLongWait/overallNumServed;
	}
	
	protected int GetShortestQueue (){
		//Find the CheckoutQueue with the least number of customers in it, that is serving. 
		int min = -1;
		int checkoutSelected = 0;
		for (int i = 0; i < model.rCheckoutCounters.length; i++){
			CheckoutCounter c = model.rCheckoutCounters[i]; 
			
			//If the customer finds a checkout with an idle cashier, it is the appropriate choice
			//A queue can be empty but the cashier busy, so selecting a not busy cashier instead is preferable
			if (c.status == CheckoutCounter.Status.NOT_BUSY && model.qCheckoutQueues[i].isEmpty()){
				checkoutSelected = i;
				break;
			}
			//This if condition will ensure that the Checkout has an employee that is currently serving customers.
			if (c.status != CheckoutCounter.Status.UNATTENDED && !c.isClosing){
				if (model.qCheckoutQueues[i].size() < min || min == -1){
					min = model.qCheckoutQueues[i].size();
					checkoutSelected = i;
				}
			}
		}
		
		return checkoutSelected;
	}
	
	protected void SchedulePersonnel(double time){
		//                                           2:00 pm, 3:30 pm, 6:00 pm,       8:30 pm
		double[] staffChangeTimeSeq = new double[]{0.0,        90.0,       240.0,     390.0};
		
		if(time == staffChangeTimeSeq[0])
	    {
	    	model.rgBaggers.numAvailable = ChangeNumOfBaggers(1);
	    	ChangeNumOfCashiers(1);
	    }
	    else if(time == staffChangeTimeSeq[1]) 
	    {
	    	model.rgBaggers.numAvailable = ChangeNumOfBaggers(2);
	    	ChangeNumOfCashiers(2);
	    }
	    else if(time == staffChangeTimeSeq[2])
	    {
	    	model.rgBaggers.numAvailable = ChangeNumOfBaggers(3);
	    	ChangeNumOfCashiers(3);
	    }
	    else if(time == staffChangeTimeSeq[3])
	    {
	    	model.rgBaggers.numAvailable = ChangeNumOfBaggers(4);
	    	ChangeNumOfCashiers(4);
	    }
	    else System.out.println("Invalid time to schedule employees:"+model.getClock());
	}
	
	private int ChangeNumOfBaggers (int staffChange){
		//Determine the difference in number of baggers between the current scheduling slot and 
		//the previous one. Then just add the difference to rgBaggers.numAvailable.
		//If difference is negative, then the number of baggers will be decreased, as desired.
		int difference = 0;
		//In the case of the initial staff change, simply set the numAvailable accordingly
		if (staffChange == 1){
			return model.rgBaggers.schedule[0];
		}
		else if (staffChange == 2){
			difference = model.rgBaggers.schedule[1];
		}
		else if (staffChange == 3){
			difference = model.rgBaggers.schedule[2] - model.rgBaggers.schedule[0];
		}
		else if (staffChange == 4){
			difference = -model.rgBaggers.schedule[1];
		}
		else {
			System.out.println("Invalid staff change number:"+staffChange);
		}
		return model.rgBaggers.numAvailable + difference;
	}
	
	//The following might look complicated but you just have to understand
	//what may happen during each of the four staff change actions
	private void ChangeNumOfCashiers (int staffChange){
		if (staffChange == 1){
			addCashiers(model.cashierSchedule[0], 1);
		}
		else if (staffChange == 2){
			addCashiers(model.cashierSchedule[1], 2);
		}
		else if (staffChange == 3){
			int difference = model.cashierSchedule[2] - model.cashierSchedule[0];
			if (difference > 0){
				addCashiers (difference, 3);
			}
			else{
				removeCashiers (-difference, 3);
			}
			//all cashiers remaining from the first staff change 
			//will actually swap positions with new cashiers
			swapCashiers ();
		}
		else if (staffChange == 4){
			removeCashiers(model.cashierSchedule[1], 4);
		}
		else {
			System.out.println("Invalid staff change number:"+staffChange);
		}
	}
	
	//The following three methods are helpers for ChangeNumOfCashiers
	
	//cashiers will be added during the first and second staff changes
	//(although it is possible for zero to be added), and possibly during the third staff change
	private void addCashiers (int numToAdd, int staffChange){
		for (int j=0; j<numToAdd; j++){
			int checkoutToAddTo = -1;
			//find the first unattended checkout
			for (int i=0; i<model.rCheckoutCounters.length; i++){
				if (model.rCheckoutCounters[i].status == Status.UNATTENDED){
					checkoutToAddTo = i;
					break;
				}
			}
			if (checkoutToAddTo >= 0 && checkoutToAddTo < model.rCheckoutCounters.length){
				model.rCheckoutCounters[checkoutToAddTo].status = Status.NOT_BUSY;
				model.rCheckoutCounters[checkoutToAddTo].scheduleSlot = staffChange;
			}
			else{
				System.out.println("error: Attempted to add cashier but all checkouts attended");
			}
		}
	}
	
	//Cashiers can be removed during the third staff change and
	//will necessarily be removed during the fourth staff change
	//(although the number removed can be zero)
	//During the third staff change, any removed cashiers will be from the first 
	//staff change, and during the fourth staff change, any removed cashiers will
	//be from the second staff change.
	private void removeCashiers (int numToRemove, int staffChange){
		for (int j=0; j<numToRemove; j++){
			int checkoutToRemoveFrom = -1;
			//find a checkout with an appropriate cashier to remove
			for (int i=0; i<model.rCheckoutCounters.length; i++){
				if (model.rCheckoutCounters[i].scheduleSlot == staffChange-2){
					checkoutToRemoveFrom = i;
					break;
				}
			}
			if (checkoutToRemoveFrom >= 0 && checkoutToRemoveFrom < model.rCheckoutCounters.length){
				model.rCheckoutCounters[checkoutToRemoveFrom].isClosing = true;
				model.rCheckoutCounters[checkoutToRemoveFrom].scheduleSlot = 0;
			}
			else{
				System.out.println("error: Attempted to remove cashier but none found");
			}
		}
	}
	
	//swapping can only occur during the third staff change
	//during this action, cashiers that arrived during the first staff change 
	//will simply change places (checkout counters) with newly arrived cashiers
	//this is done by changing the scheduleSlot attribute from 1 to 3 for applicable checkout counters. 
	private void swapCashiers (){
		for (int i=0; i<model.rCheckoutCounters.length; i++){
			if (model.rCheckoutCounters[i].scheduleSlot == 1){
				model.rCheckoutCounters[i].scheduleSlot = 3;
			}
		}
	}	
}