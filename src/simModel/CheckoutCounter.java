package simModel;

class CheckoutCounter {
	
	protected enum Status {
		BUSY,
		NOT_BUSY,
		UNATTENDED,
		CLOSING
	}
	protected Status status;
	
	protected Customer currentCustomer;
	protected boolean baggerPresent = false;
	protected boolean cashierIsBagging = false;
	protected boolean customerIsPaying = false;
	
	protected int scheduleSlot = 0;
	
}
