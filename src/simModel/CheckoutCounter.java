package simModel;

class CheckoutCounter {
	
	protected enum Status {
		BUSY,
		NOT_BUSY,
		UNATTENDED
	}
	protected Status status;
	
	protected Customer currentCustomer;
	protected boolean baggerPresent = false;
	protected boolean cashierIsBagging = false;
	protected boolean customerIsPaying = false;
	protected boolean isClosing = false;
	protected int scheduleSlot = 0;
	
}
