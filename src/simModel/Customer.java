package simModel;

class Customer {
	
	int numberOfItems;
	
	enum PaymentType {
			CASH, 
			CREDIT, 
			CHECK_WITH_CHECK_CASHING_CARD,
			CHECK_WITHOUT_CHECK_CASHING_CARD
	}
	
	PaymentType paymentType;

	double startWait;
	double paymentTime;

	protected boolean served = false;
	protected boolean bagged = false;
	protected boolean payed = false;
	
}
