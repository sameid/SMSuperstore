package simModel;

class Output 
{
	SMSuperstore model;
	
	protected Output(SMSuperstore md) { model = md; }
    // Use OutputSequence class to define Trajectory and Sample Sequences?

	// Sample Sequences
	double[] numServed = new double[4];
	double[] numLongWait = new double[4];
	double[] propLongWait = new double[4];
	
	//DSOV
	double overallPropLongWait;
	
}
