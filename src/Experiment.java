//Additional experiment
//Not used at the moment

import simModel.*;
import cern.colt.Arrays;
import cern.jet.random.engine.*;

class Experiment
{
   public static void main(String[] args)
   {
	   int[][] cashierSchedules =
		   {
				   {10,5,9},   //case 1
				   {},  //case 2
				   {}  //case 3
		   };
	   
	   int[][] baggerSchedules =
		   {
				   {8,5,8},   //case 1
				   {},  //case 2
				   {}  //case 3
		   };
	   
       int i, NUMRUNS = 1;
       double startTime=0.0, endTime=480.0;
       Seeds[] sds = new Seeds[NUMRUNS];
       SMSuperstore mname;  // Simulation object

       // Lets get a set of uncorrelated seeds
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       for(i=0 ; i<NUMRUNS ; i++) sds[i] = new Seeds(rsg);
       
  
       // Loop for NUMRUN simulation runs for each case
       
       // Case 1
       System.out.println(" Case 1");
       System.out.println("Cashier Schedule: " + Arrays.toString(cashierSchedules[0]));
       System.out.println("Bagger Schedule: " + Arrays.toString(baggerSchedules[0]));
       for(i=0 ; i < NUMRUNS ; i++)
       {
          mname = new SMSuperstore(startTime,endTime, cashierSchedules[0], baggerSchedules[0], sds[i], true);
          mname.runSimulation();
       }
   }
}