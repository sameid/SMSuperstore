// File: Experiment.java
// Description:

import simModel.*;
import cern.jet.random.engine.*;

// Main Method: Experiments
//
//Validation Experiment
class Experiment
{
   public static void main(String[] args)
   {
       int i, NUMRUNS = 1; 
       double startTime=0.0, endTime=480.0;
       Seeds[] sds = new Seeds[NUMRUNS];
       SMSuperstore mname;  // Simulation object

       // Lets get a set of uncorrelated seeds
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       for(i=0 ; i<NUMRUNS ; i++) sds[i] = new Seeds(rsg);
       
  
       // Loop for NUMRUN simulation runs for each case
       
       // Case 1: Few employees
       System.out.println(" Case 1");
       
       int[] lightCashierSchedule = {4, 1, 3};
       int[] lightBaggerSchedule = {1, 1, 1};
       
       for(i=0 ; i < NUMRUNS ; i++)
       {
          mname = new SMSuperstore(startTime,endTime, lightCashierSchedule, lightBaggerSchedule, sds[i], true);
          mname.runSimulation();
       }
       
//       // Case 1: Lots of employees
//       System.out.println(" Case 2");
//       
//       int[] heavyCashierSchedule = {15, 5, 15};
//       int[] heavyBaggerSchedule = {15, 5, 15};
//       
//       for(i=0 ; i < NUMRUNS ; i++)
//       {
//          mname = new SMSuperstore(startTime,endTime,heavyCashierSchedule, heavyBaggerSchedule, sds[i], true);
//          mname.runSimulation();
//       }
   }
}
