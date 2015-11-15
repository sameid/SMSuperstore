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
	double totalWait;
	double startWait;
	double paymentTime;
	boolean requiresPreCheck;
	
}
