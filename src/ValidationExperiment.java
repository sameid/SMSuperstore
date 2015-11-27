//Validation Experiment

import simModel.*;
import cern.colt.Arrays;
import cern.jet.random.engine.*;

class ValidationExperiment
{
   public static void main(String[] args)
   {
	   int[][] cashierSchedules =
		   {
				   {4,1,3},   //case 1
				   {8,8,9},  //case 2
				   {15,5,15}  //case 3
		   };
	   
	   int[][] baggerSchedules =
		   {
				   {1,1,1},   //case 1
				   {5,5,6},  //case 2
				   {15,5,15}  //case 3
		   };
	   
	   
       //number of runs doesn't matter since we're only validating the model
	   
       double startTime=0.0, endTime=480.0;
       Seeds sds;
       SMSuperstore superstore;  // Simulation object

       // Lets get a set of uncorrelated seeds
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       sds = new Seeds(rsg);
       
        
       // Case 1
       System.out.println("Case 1 - Very Few Employees");
       System.out.println("Cashier Schedule: " + Arrays.toString(cashierSchedules[0]));
       System.out.println("Bagger Schedule: " + Arrays.toString(baggerSchedules[0]));

       superstore = new SMSuperstore(startTime,endTime, cashierSchedules[0], baggerSchedules[0], sds, true);
       superstore.runSimulation();

       
       // Case 2
       System.out.println("Case 2 - Moderate Number of Employees");   
       System.out.println("Cashier Schedule: " + Arrays.toString(cashierSchedules[1]));
       System.out.println("Bagger Schedule: " + Arrays.toString(baggerSchedules[1]));

       superstore = new SMSuperstore(startTime,endTime, cashierSchedules[1], baggerSchedules[1], sds, true);
	   superstore.runSimulation();

       
	   // Case 3
	   System.out.println("Case 3 - Lots of employees");
	   System.out.println("Cashier Schedule: " + Arrays.toString(cashierSchedules[2]));
	   System.out.println("Bagger Schedule: " + Arrays.toString(baggerSchedules[2]));

	   superstore = new SMSuperstore(startTime,endTime, cashierSchedules[2], baggerSchedules[2], sds, true);
	   superstore.runSimulation();
   }
}