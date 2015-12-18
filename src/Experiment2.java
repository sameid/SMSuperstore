
//Experiment for actually finding some results

//An acceptable proportion (of customers that have to wait too long)
//of .50 is used 

import simModel.*;
import cern.colt.Arrays;
import cern.jet.random.engine.*;

class Experiment2 {
	public static final int NUMRUNS = 300;
	
	//initial schedules
	public static int[] cashierSchedule = new int[] { 1, 1, 1 };
	public static int[] baggerSchedule = new int[] { 1, 1, 1 };

	public static final double ACCEPTABLE_PROPORTION = .50;

	public static void main(String[] args) {


		int i;
		double startTime = 0.0, endTime = 480.0;
		Seeds[] sds = new Seeds[NUMRUNS];
		SMSuperstore mname; // Simulation object

		// Lets get a set of uncorrelated seeds
		RandomSeedGenerator rsg = new RandomSeedGenerator();
		for (i = 0; i < NUMRUNS; i++)
			sds[i] = new Seeds(rsg);

		//The overall proportion of customers that had to wait too long,
		//derived from the output of the model, it is our main measure of
		//performance for a given schedule
		double overallPropLongWait = 1;
		
		//an integer for counting the number of iterations it takes
		//to find a suitable schedule
		int attempt = 1;
		
		//Sometimes, the algorithm will choose a schedule where no customers are being satisfied in
		//the last time slot, but the overall proportion of customers waiting too long is still acceptable
		//This result, though it technically does satisfy our goals, is not desired.
		//Must have another boolean to check for it.
		boolean customersStuckCondition = true;
		
		//Find a cashier schedule first.
		//Bagger schedule will be set as equal to the cashier schedule
		while (overallPropLongWait >= ACCEPTABLE_PROPORTION || customersStuckCondition) {

			//These two arrays will store the results from all runs
			double[] overallPropsLongWait = new double[NUMRUNS];
			double[][] propsLongWait = new double[NUMRUNS][4];

			// Loop for NUMRUN simulation runs for each attempt

			System.out.println("Iteration " + attempt);
			System.out.println("Cashier Schedule: " + Arrays.toString(cashierSchedule));
			System.out.println("Bagger Schedule: " + Arrays.toString(baggerSchedule));

			for (i = 0; i < NUMRUNS; i++) {
				mname = new SMSuperstore(startTime, endTime, cashierSchedule, baggerSchedule, sds[i], false);
				mname.runSimulation();
				overallPropsLongWait[i] = mname.getOverallPropLongWait();
				propsLongWait[i] = mname.getPropLongWait();
			}

			//here the results of all the runs are averaged and stored in the 
			//appropriate variables, to be used for finding a new schedule
			overallPropLongWait = averageOverall(overallPropsLongWait);
			double[] propLongWait = averageProps(propsLongWait);
			
			//The customer stuck condition is true if virtually none of the customers during
			//the last time slot are being served in a timely manner
			customersStuckCondition = propLongWait[3] >= .99; 
				
			System.out.println("Overall proportion of customers that waited too long: " + overallPropLongWait);
			//System.out.println(Arrays.toString(propLongWait));
			
			if (overallPropLongWait > ACCEPTABLE_PROPORTION || customersStuckCondition) {				

				int slotToAddTo = 0;
				
				for (int j = 0; j < 3; j++) {
					if (propLongWait[j] + propLongWait[j+1] > ACCEPTABLE_PROPORTION*2){
						slotToAddTo = j;
						break;
						}
					}				
						
				cashierSchedule = addToSlot(slotToAddTo, cashierSchedule);
				baggerSchedule = cashierSchedule;
			}
			System.out.println("");

			attempt++;
			
		}
		
		//repeat without changing the cashier schedule
		//to see if a better bagger schedule will be found
		
		baggerSchedule = new int[]{1, 1, 1};
		overallPropLongWait = 1;
		
		while (overallPropLongWait >= ACCEPTABLE_PROPORTION) {

			//These two arrays will store the results from all runs
			double[] overallPropsLongWait = new double[NUMRUNS];
			double[][] propsLongWait = new double[NUMRUNS][4];

			// Loop for NUMRUN simulation runs for each attempt

			System.out.println("Iteration " + attempt);
			System.out.println("Cashier Schedule: " + Arrays.toString(cashierSchedule));
			System.out.println("Bagger Schedule: " + Arrays.toString(baggerSchedule));

			for (i = 0; i < NUMRUNS; i++) {
				mname = new SMSuperstore(startTime, endTime, cashierSchedule, baggerSchedule, sds[i], false);
				mname.runSimulation();
				overallPropsLongWait[i] = mname.getOverallPropLongWait();
				propsLongWait[i] = mname.getPropLongWait();
			}

			//here the results of all the runs are averaged and stored in the 
			//appropriate variables, to be used for finding a new schedule
			overallPropLongWait = averageOverall(overallPropsLongWait);
			double[] propLongWait = averageProps(propsLongWait);
			
			System.out.println("Overall proportion of customers that waited too long: " + overallPropLongWait);
			//System.out.println(Arrays.toString(propLongWait));
			
			if (overallPropLongWait > ACCEPTABLE_PROPORTION) {
				
				int slotToAddTo = 0;
				
				for (int j = 0; j < 3; j++) {
					if (propLongWait[j] + propLongWait[j+1] > ACCEPTABLE_PROPORTION*2){
						slotToAddTo = j;
						break;
						}
					}				
						
				baggerSchedule = addToSlot(slotToAddTo, baggerSchedule);
			}
			System.out.println("");

			attempt++;
		}
		
		System.out.println("Optimum schedule found satisfying that, at most,");
		System.out.println(ACCEPTABLE_PROPORTION*100 + "% of customers will wait more than 15 minutes");
		System.out.println("Final schedules:");
		System.out.println("	Cashier Schedule: " + Arrays.toString(cashierSchedule));
		System.out.println("	Bagger Schedule: " + Arrays.toString(baggerSchedule));
		System.out.println("Total cost of this schedule:");
		double cost = (cashierSchedule[0]+cashierSchedule[1]+cashierSchedule[2])*7.25 + (baggerSchedule[0]+baggerSchedule[1]+baggerSchedule[2])*5.50;
		System.out.println("	$"+cost);
	}

	private static double[] averageProps(double[][] propLongWait) {
		double[] result = new double[4];

		for (int i = 0; i < NUMRUNS; i++) {
			for (int j = 0; j < 4; j++) {
				result[j] += propLongWait[i][j];
			}

		}
		for (int j = 0; j < 4; j++) {
			result[j] = result[j] / NUMRUNS;
		}
		return result;
	}

	//used instead of calculating ybar with ConfidenceInterval.getPointEstimate()
	private static double averageOverall(double[] overallProps){
		double result = 0;
		for (int i = 0; i < NUMRUNS; i++) {
			result += overallProps[i];
		}
		return result/NUMRUNS;
	}
	
	private static int[] addToSlot(int slot, int[] schedule){
		
		if(slot == 1 && schedule[0] + schedule[1] >= 20) slot++;
		
		if(slot == 2 && schedule[1] + schedule[2] >= 20){
			schedule[slot]--;
			return addToSlot(slot-1, schedule);
		}
		
		schedule[slot]++;
		return schedule;
	}
}
