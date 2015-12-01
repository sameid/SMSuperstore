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


	// Constructor
	protected RVPs(SMSuperstore model, Seeds sd) 
	{ 
		this.model = model; 
		// Set up distribution functions
		interArrDist = new Exponential(0.0,  new MersenneTwister(sd.getSeed()));
		priceCheckDist = new Exponential(0.0, new MersenneTwister(sd.getSeed()));
		
		numItemDist1 = new Normal(ITEMS_PER_CUSTOMER_MEAN_1, ITEMS_PER_CUSTOMER_SD_1, new MersenneTwister(sd.getSeed()));//need to hook up seeds
		numItemDist2 = new Normal(ITEMS_PER_CUSTOMER_MEAN_2, ITEMS_PER_CUSTOMER_SD_2, new MersenneTwister(sd.getSeed()));
		
		paymentTimeDist = new Normal(0, 0,  new MersenneTwister(sd.getSeed()));
		
		paymentTypeRandGen = new MersenneTwister(sd.getSeed());
		checkoutTimeDist = new Normal(0,0, new MersenneTwister(sd.getSeed()));
		baggingTimeDist = new Normal(0,0, new MersenneTwister(sd.getSeed()));
		pricecheckRandGen = new MersenneTwister(sd.getSeed());
		
	}
	
	/****************************/
	
	private double[] interArrDistMeans = {
			95.0, 100.0, 120.0, 150.0, 160.0, 150.0, 
			160.0, 110.0,  105.0,  95.0,  125.0, 150.0, 
			155.0, 95.0, 70.0, 60.0
	};
	
	private double[] timePeriods = {
		30.0, 60.0, 90.0, 120.0, 150.0, 180.0, 210.0, 240.0, 270.0, 300.0, 330.0, 360.0, 390.0, 420.0, 450.0, 480.0	
	};

	private Exponential interArrDist;  // Exponential distribution for interarrival times
	
	protected double duC()  // for getting next value of duInput
	{
	    double nxtInterArr;
	    double mean = 0.0;
	    
	    for (int i = 0 ; i < timePeriods.length ; i ++){
	    	if (model.getClock() <= timePeriods[i]){
	    		mean = interArrDistMeans[i];
	    		mean = 60.0/mean;
	    		break;
	    	}
	    }
	    
        nxtInterArr = interArrDist.nextDouble(1.0/mean) + model.getClock();
        if (nxtInterArr > model.closingTime){
        	nxtInterArr = -1.0;
        }
        
	    return(nxtInterArr);
	}
	
	/****************************/
	
	private Normal numItemDist1;
	private Normal numItemDist2;
	private final double ITEMS_PER_CUSTOMER_MEAN_1 = 27.32;
	private final double ITEMS_PER_CUSTOMER_SD_1 = 8.93;
	private final double ITEMS_PER_CUSTOMER_MEAN_2 = 106.75;
	private final double ITEMS_PER_CUSTOMER_SD_2 = 18.95;
	private final double ALPHA = 0.22;
	
	protected int uNumItem (){
		double r = (ALPHA*numItemDist1.nextDouble()) +  ((1-ALPHA)*numItemDist2.nextDouble());
		return (int) r;
	}
	
	/****************************/
	
	private final double PROP_CASH_LESS_20 = 0.45;
	private final double PROP_CREDIT_LESS_20 = 0.25;
	private final double PROP_CHECK_LESS_20 = 0.30;
	
	private final double PROP_CASH_MORE_20 = 0.20;
	private final double PROP_CREDIT_MORE_20 = 0.35;
	private final double PROP_CHECK_MORE_20 = 0.45;
	
	private final double PROP_CASHING_CARD = 0.73;
	private final double PROP_NO_CASHING = 0.27;
	
	MersenneTwister paymentTypeRandGen;
	
	protected Customer.PaymentType uPaymentType(int numOfItems){
		double randNum = paymentTypeRandGen.nextDouble();
		Customer.PaymentType type = null;
		if (numOfItems <= 20){
			if (randNum <= PROP_CASH_LESS_20) type = Customer.PaymentType.CASH;
			else if (randNum > PROP_CASH_LESS_20 && randNum <= PROP_CASH_LESS_20+PROP_CREDIT_LESS_20) type = Customer.PaymentType.CREDIT;
			else if (randNum > PROP_CASH_LESS_20+PROP_CREDIT_LESS_20) type = Customer.PaymentType.CHECK_WITH_CHECK_CASHING_CARD;//need to fix
		}
		else {
			if (randNum <= PROP_CASH_MORE_20) type = Customer.PaymentType.CASH;
			else if (randNum > PROP_CASH_MORE_20 && randNum <= PROP_CASH_MORE_20+PROP_CREDIT_MORE_20) type = Customer.PaymentType.CREDIT;
			else if (randNum > PROP_CASH_MORE_20+PROP_CREDIT_MORE_20) type = Customer.PaymentType.CHECK_WITH_CHECK_CASHING_CARD;//need to fix
		}
		
		if (type == Customer.PaymentType.CHECK_WITH_CHECK_CASHING_CARD){
			randNum = paymentTypeRandGen.nextDouble();
			if (randNum > PROP_CASHING_CARD ) type = Customer.PaymentType.CHECK_WITHOUT_CHECK_CASHING_CARD;
		}
		return type;
		
	}

	/****************************/
	
	private Normal checkoutTimeDist;
	private final double CHECKOUT_MEAN = 0.05;
	private final double CHECKOUT_STANDARD_DEVIATION = 0.0125;
	
	MersenneTwister pricecheckRandGen;
	private final double PROP_PRICE_CHECK = 0.013;
	private final double PROP_NO_CHECK = 0.987;
	
	private Exponential priceCheckDist;
	private final double PRICE_CHECK_MEAN = 2.2;
	
	protected double uCheckoutTm(int numOfItems){
		
		double addedTime = 0.0;
		for (int i = 0 ; i < numOfItems ; i++){
			double randNum = pricecheckRandGen.nextDouble(); 
			if (randNum <= PROP_PRICE_CHECK) addedTime += priceCheckDist.nextDouble(1.0/PRICE_CHECK_MEAN);
		}
		
		return (numOfItems * (checkoutTimeDist.nextDouble(CHECKOUT_MEAN, CHECKOUT_STANDARD_DEVIATION))) + addedTime;
	}

	/****************************/
	
	private Normal paymentTimeDist;
	private final double CASH_MEAN = 0.95;
	private final double CASH_SD = 0.17;
	private final double CREDIT_MEAN = 1.24;
	private final double CREDIT_SD = 0.21;
	private final double CHECK_MEAN = 1.45;
	private final double CHECK_SD = 0.35;
	private final double NO_CHECK_MEAN = 2.40;
	private final double NO_CHECK_SD = 0.21;
	
	protected double uPaymentTm(PaymentType type){
		double r = 0.0;
		if (type == PaymentType.CASH)r = paymentTimeDist.nextDouble(CASH_MEAN, CASH_SD);
		else if (type == PaymentType.CREDIT)r = paymentTimeDist.nextDouble(CREDIT_MEAN, CREDIT_SD);
		else if (type == PaymentType.CHECK_WITH_CHECK_CASHING_CARD) r = paymentTimeDist.nextDouble(CHECK_MEAN, CHECK_SD);
		else if (type == PaymentType.CHECK_WITHOUT_CHECK_CASHING_CARD)r = paymentTimeDist.nextDouble(NO_CHECK_MEAN, NO_CHECK_SD);
		return r;
	}

	/****************************/
	
	private Normal baggingTimeDist;
	private final double BAGGING_MEAN = 0.021;
	private final double BAGGING_STANDARD_DEVIATION = 0.0042;//need to fix 20%
	
	protected double uBaggingTm(int numOfItems){
		double totalBaggingTime = 0.0;
		for (int i = 0 ; i < numOfItems ; i++){
			totalBaggingTime += baggingTimeDist.nextDouble(BAGGING_MEAN, BAGGING_STANDARD_DEVIATION);
		}
		return totalBaggingTime;
	}

	/****************************/
	

}
