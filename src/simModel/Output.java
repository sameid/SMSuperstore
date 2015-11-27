package simModel;

class Output 
{
	SMSuperstore model;
	
	protected Output(SMSuperstore md) { model = md; }
	
	// Sample Sequences
	double[] numServed = new double[4];
	double[] numLongWait = new double[4];
	double[] propLongWait = new double[4];
	
	//DSOV
	double overallPropLongWait;
	
}
