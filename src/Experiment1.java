//Experiment for determining what number of runs will
//provide us with an acceptable confidence interval

import simModel.*;
import cern.colt.Arrays;
import cern.jet.random.engine.*;
import absmodJ.ConfidenceInterval;


class Experiment1
{
	//will compute statistics for the following values of n, to show the necessary number of runs
   public static final int[] RUNS = new int[]{10, 20, 30, 50, 100, 200, 300};
   public static final int NUMRUNS = RUNS[RUNS.length-1] ;
   public static final double CONF_LEVEL = 0.9;   
   
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
	        
	   int i, runsIndex;
       double startTime=0.0, endTime=480.0;
       Seeds[] sds = new Seeds[NUMRUNS];
       SMSuperstore superstore;  // Simulation object
       double [] valuesCase1 = new double[NUMRUNS];
       double [] valuesCase2 = new double[NUMRUNS];
       double [] valuesCase3 = new double[NUMRUNS];
       
       ConfidenceInterval cf;

       // Lets get a set of uncorrelated seeds
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       for(i=0 ; i<NUMRUNS ; i++) sds[i] = new Seeds(rsg);
       
        
       // Case 1
       System.out.println("Case 1 - Very Few Employees");
       System.out.println("Cashier Schedule: " + Arrays.toString(cashierSchedules[0]));
       System.out.println("Bagger Schedule: " + Arrays.toString(baggerSchedules[0]));
       System.out.printf("-------------------------------------------------------------------------------------\n");
       System.out.printf("                                      Case 1\n");
       System.out.printf("-------------------------------------------------------------------------------------\n");
       System.out.printf("       n      Point estimate(ybar(n))  s(n)     zeta   CI Min   CI Max  zeta/ybar(n) \n");
       System.out.printf("-------------------------------------------------------------------------------------\n");

       runsIndex = 0;
       for(i=0 ; i < NUMRUNS ; i++)
       {
	       superstore = new SMSuperstore(startTime,endTime, cashierSchedules[0], baggerSchedules[0], sds[i], false);
	       superstore.runSimulation();
	       valuesCase1[i] = superstore.getOverallPropLongWait();
	       if(i+1 == RUNS[runsIndex]){
	    	   cf = new ConfidenceInterval(Arrays.trimToCapacity(valuesCase1, RUNS[runsIndex]), CONF_LEVEL);
	           System.out.printf("%8d %15.3f %18.3f %8.3f %8.3f %8.3f %12.3f\n",
	         	     RUNS[runsIndex], cf.getPointEstimate(), cf.getVariance(),
	         	     cf.getZeta(), cf.getCfMin(), cf.getCfMax(),
	      	         Math.abs(cf.getZeta()/cf.getPointEstimate()));
	           
	           runsIndex++;
	       }
       }
       
       // Case 2
       System.out.println("\n");
       System.out.println("Case 2 - Moderate Number of Employees");   
       System.out.println("Cashier Schedule: " + Arrays.toString(cashierSchedules[1]));
       System.out.println("Bagger Schedule: " + Arrays.toString(baggerSchedules[1]));
       System.out.printf("-------------------------------------------------------------------------------------\n");
       System.out.printf("                                      Case 2\n");
       System.out.printf("-------------------------------------------------------------------------------------\n");
       System.out.printf("       n      Point estimate(ybar(n))  s(n)     zeta   CI Min   CI Max  zeta/ybar(n) \n");
       System.out.printf("-------------------------------------------------------------------------------------\n");

       runsIndex = 0;
       for(i=0 ; i < NUMRUNS ; i++)
       {
	       superstore = new SMSuperstore(startTime,endTime, cashierSchedules[1], baggerSchedules[1], sds[i], false);
		   superstore.runSimulation();
		   valuesCase2[i] = superstore.getOverallPropLongWait();
	       if(i+1 == RUNS[runsIndex]){
	    	   cf = new ConfidenceInterval(Arrays.trimToCapacity(valuesCase2, RUNS[runsIndex]), CONF_LEVEL);
	           System.out.printf("%8d %15.3f %18.3f %8.3f %8.3f %8.3f %12.3f\n",
	         	     RUNS[runsIndex], cf.getPointEstimate(), cf.getVariance(),
	         	     cf.getZeta(), cf.getCfMin(), cf.getCfMax(),
	      	         Math.abs(cf.getZeta()/cf.getPointEstimate()));
	           
	           runsIndex++;
	       }
       }
		   
       
	   // Case 3
       System.out.println("\n");
	   System.out.println("Case 3 - Lots of employees");
	   System.out.println("Cashier Schedule: " + Arrays.toString(cashierSchedules[2]));
	   System.out.println("Bagger Schedule: " + Arrays.toString(baggerSchedules[2]));
       System.out.printf("-------------------------------------------------------------------------------------\n");
       System.out.printf("                                      Case 3\n");
       System.out.printf("-------------------------------------------------------------------------------------\n");
       System.out.printf("       n      Point estimate(ybar(n))  s(n)     zeta   CI Min   CI Max  zeta/ybar(n) \n");
       System.out.printf("-------------------------------------------------------------------------------------\n");

       runsIndex = 0;
       for(i=0 ; i < NUMRUNS ; i++)
       {
		   superstore = new SMSuperstore(startTime,endTime, cashierSchedules[2], baggerSchedules[2], sds[i], false);
		   superstore.runSimulation();
		   valuesCase3[i] = superstore.getOverallPropLongWait();
	       if(i+1 == RUNS[runsIndex]){
	    	   cf = new ConfidenceInterval(Arrays.trimToCapacity(valuesCase3, RUNS[runsIndex]), CONF_LEVEL);
	           System.out.printf("%8d %15.3f %18.3f %8.3f %8.3f %8.3f %12.3f\n",
	         	     RUNS[runsIndex], cf.getPointEstimate(), cf.getVariance(),
	         	     cf.getZeta(), cf.getCfMin(), cf.getCfMax(),
	      	         Math.abs(cf.getZeta()/cf.getPointEstimate()));
	           
	           runsIndex++;
	       }
       }
   }
}