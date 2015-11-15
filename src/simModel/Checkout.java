package simModel;

import java.util.ArrayList;

class Checkout {
	
	protected enum Status {
		BUSY,
		NOT_BUSY,
		UNATTENDED
	}
	protected Status status;
	protected boolean baggerPresent ;
	
	protected ArrayList<Customer> rCheckoutQueue = new ArrayList<Customer>();
	protected Customer currentCustomer;
	
	public void setCustomer (Customer newCustomer){
		currentCustomer = newCustomer;
	}
	
	public Customer getCustomer (){
		return currentCustomer;
	}
	
}
