package simModel;

class Output 
{
	SMSuperstore model;
	
	protected Output(SMSuperstore md) { model = md; }
	
	// Sample Sequences
	double[] numServed = new double[Constants.NUM_TIME_SLOTS];
	double[] numLongWait = new double[Constants.NUM_TIME_SLOTS];
	double[] propLongWait = new double[Constants.NUM_TIME_SLOTS];
	
	//DSOV
	double overallPropLongWait;
	
}
