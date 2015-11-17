package simModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


class Checkout {
	
	protected enum Status {
		BUSY,
		NOT_BUSY,
		UNATTENDED,
		CLOSING
	}
	protected Status status;
	
	protected Queue<Customer> rCheckoutQueue = new LinkedList<Customer>();
	protected Customer currentCustomer;
	protected boolean baggerPresent = false;
	
	public void setCustomer (Customer newCustomer){
		currentCustomer = newCustomer;
	}
	
	public Customer getCustomer (){
		return currentCustomer;
	}
	
	public void addCustomerToQueue (Customer c){
		rCheckoutQueue.add(c);
	}
	
}
