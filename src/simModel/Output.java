package simModel;

class Output 
{
	SMSuperstore model;
	
	protected Output(SMSuperstore md) { model = md; }
    // Use OutputSequence class to define Trajectory and Sample Sequences
    // Trajectory Sequences

    // Sample Sequences

    // DSOVs available in the OutputSequence objects
    // If seperate methods required to process Trajectory or Sample
    // Sequences - add them here

    // SSOVs
	double[] numServed = new double[4];
	double[] numLongWait = new double[4];
	double[] propLongWait = new double[4];
	
}
