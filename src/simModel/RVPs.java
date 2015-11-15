package simModel;

import cern.jet.random.Exponential;
import cern.jet.random.Normal;
import cern.jet.random.engine.MersenneTwister;
import simModel.Customer.PaymentType;

class RVPs 
{
	SMSuperstore model; // for accessing the clock
    // Data Models - i.e. random veriate generators for distributions
	// are created using Colt classes, define 
	// reference variables here and create the objects in the
	// constructor with seeds

	/* Random Variate Procedure for Arrivals */
	private Exponential interArrDist;  // Exponential distribution for interarrival times
	private final double WMEAN1=30.0;// !! Customer Arrival every 30 minutes
	
	private Normal numItemDist1;
	private Normal numItemDist2;
	private final double ITEMS_PER_CUSTOMER_MEAN_1 = 27.32;
	private final double ITEMS_PER_CUSTOMER_SD_1 = 8.93;
	private final double ITEMS_PER_CUSTOMER_MEAN_2 = 106.75;
	private final double ITEMS_PER_CUSTOMER_SD_2 = 18.95;
	private final double ALPHA = 0.22;
	
	private Normal checkoutTimeDist;
	private final double CASH_MEAN = 0.95;
	private final double CASH_SD = 0.17;
	private final double CREDIT_MEAN = 1.24;
	private final double CREDIT_SD = 0.21;
	private final double CHECK_MEAN = 1.45;
	private final double CHECK_SD = 0.35;
	private final double NO_CHECK_MEAN = 2.40;
	private final double NO_CHECK_SD = 0.21;
	
	private Normal validationTimeDist;
	private final double VALIDATION_MEAN = 1.25;
	private final double VALIDATION_SD = 0.21;
			
	// Constructor
	protected RVPs(SMSuperstore model, Seeds sd) 
	{ 
		this.model = model; 
		// Set up distribution functions
		interArrDist = new Exponential(1.0/WMEAN1,  new MersenneTwister(sd.seed1));
		
		numItemDist1 = new Normal(ITEMS_PER_CUSTOMER_MEAN_1, ITEMS_PER_CUSTOMER_SD_1, null);//need to hook up seeds
		numItemDist2 = new Normal(ITEMS_PER_CUSTOMER_MEAN_2, ITEMS_PER_CUSTOMER_SD_2, null);
		
		checkoutTimeDist = new Normal(0, 0, null);
		validationTimeDist = new Normal(VALIDATION_MEAN, VALIDATION_SD, null);
	}
	
	
	protected double duC()  // for getting next value of duInput
	{
	    double nxtInterArr;

        nxtInterArr = interArrDist.nextDouble();
	    // Note that interarrival time is added to current
	    // clock value to get the next arrival time.
	    return(nxtInterArr+model.getClock());
	}
	
	protected double uNumItem (){
		double r = (ALPHA*numItemDist1.nextDouble()) +  ((1-ALPHA)*numItemDist2.nextDouble());
		return r;
	}
	
	protected double uCheckoutTm(PaymentType type){
		double r = 0.0;
		if (type == PaymentType.CASH)r = checkoutTimeDist.nextDouble(CASH_MEAN, CASH_SD);
		else if (type == PaymentType.CREDIT)r = checkoutTimeDist.nextDouble(CREDIT_MEAN, CREDIT_SD);
		else if (type == PaymentType.CHECK_WITH_CHECK_CASHING_CARD) r = checkoutTimeDist.nextDouble(CHECK_MEAN, CHECK_SD);
		else if(type == PaymentType.CHECK_WITHOUT_CHECK_CASHING_CARD)r = checkoutTimeDist.nextDouble(NO_CHECK_MEAN, NO_CHECK_SD);
		return r;
	}
	
	protected double uValidationTM(){
		return validationTimeDist.nextDouble();
	}
	
	protected double uBaggingTm(){
		return 0;
	}
	
}
