package simModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import absmodJ.SBNotice;
import absmodJ.ScheduledActivity;
import absmodJ.SequelActivity;
import absmodJ.AOSimulationModel;
import absmodJ.Behaviour;
//
// The Simulation model Class
public class SMSuperstore extends AOSimulationModel
{
	// Constants available from Constants class
	/* Parameter */
        // Define the parameters

	/*-------------Entity Data Structures-------------------*/
	/* Group and Queue Entities */
	// Define the reference variables to the various 
	// entities with scope Set and Unary
	// Objects can be created here or in the Initialise Action
	protected CheckoutCounter[] rCheckouts = new CheckoutCounter[20];
	protected Queue<Customer>[] rCheckoutQueues = (LinkedList<Customer>[]) new LinkedList[20];
	
	protected Supervisor rSupervisor = new Supervisor();
	protected Queue<Customer> rSupervisorQueue = new LinkedList<Customer>();
	
	protected Baggers rgBaggers = new Baggers();

	/* Input Variables */
	// Define any Independent Input Varaibles here
	
	// References to RVP and DVP objects
	protected RVPs rvp;  // Reference to rvp object - object created in constructor
	protected DVPs dvp = new DVPs(this);  // Reference to dvp object
	protected UDPs udp = new UDPs(this);

	// Output object
	protected Output output = new Output(this);
	
	// Output values - define the public methods that return values
	// required for experimentation.


	// Constructor
	public SMSuperstore(double t0time, double tftime, /*define other args,*/ Seeds sd)
	{
		// Initialise parameters here
		
		// Create RVP object with given seed
		rvp = new RVPs(this,sd);
		
		// rgCounter and qCustLine objects created in Initalise Action
		
		// Initialise the simulation model
		initAOSimulModel(t0time,tftime);   

		     // Schedule the first arrivals and employee scheduling
		Initialise init = new Initialise(this);
		scheduleAction(init);  // Should always be first one scheduled.
		//need staff change action
		Arrivals arrivalsAction = new Arrivals(this);
		scheduleAction(arrivalsAction);
		// Schedule other scheduled actions and acitvities here
	}

	/************  Implementation of Data Modules***********/	
	/*
	 * Testing preconditions
	 */
	protected void testPreconditions(Behaviour behObj)
	{
		reschedule (behObj);
		while(scanPreconditions()) {}
		// Check preconditions of Interruptions in Extended Activities
	}
	
	protected boolean scanPreconditions (){
		boolean state = false;
		// Check preconditions of Conditional Activities
		if (Checkout.precondition(this)){
			Checkout act = new Checkout(this);
			act.startingEvent();
			scheduleActivity(act);
			state = true;
		}
		
		if (BaggingByCashier.precondition(this)){
			BaggingByCashier act = new BaggingByCashier(this);
			act.startingEvent();
			scheduleActivity(act);
			state = true;
		}
		
		if (PaymentWApproval.precondition(this)){
			PaymentWApproval act = new PaymentWApproval(this);
			act.startingEvent();
			scheduleActivity(act);
			state = true;
		}
		
		if (PaymentWOutApproval.precondition(this)){
			PaymentWOutApproval act = new PaymentWOutApproval(this);
			act.startingEvent();
			scheduleActivity(act);
			state = true;
		}
		
		return state;
		
	}

	boolean traceflag = false;
	protected void eventOccured()
	{
		if(traceflag)
		{
			// Can add other trace/log code to monitor the status of the system
			// See examples for suggestions on setup logging
			this.showSBL();
		    // PriorityQueue<SBNotice> sbl = this.getCopySBL();
			// explicitShowSBL(sbl);

		}

		// Setup an updateTrjSequences() method in the Output class
		// and call here if you have Trajectory Sets
		// updateTrjSequences() 
	}

	// The following method duplicates the function of the private
	// method showSBL.  Can be used to modify logging of the
	// SBL to filter out some of the events or entries on 
	// the SBL.
	protected void explicitShowSBL(PriorityQueue<SBNotice> sbl)
	{
		int ix;
		SBNotice notice;
		System.out.println("------------SBL----------");
		Object[] sbList = sbl.toArray();
		Arrays.sort(sbList); // Sorts the array
		for (ix = 0; ix < sbList.length; ix++)
		{
			notice = (SBNotice) sbList[ix];
			System.out.print("TimeStamp:" + notice.timeStamp);
			if (notice.behaviourInstance != null) 
			{
				System.out.print(" Activity/Action: "
						         + notice.behaviourInstance.getClass().getName());
				if(notice.behaviourInstance.name != null) 
					System.out.println("("+notice.behaviourInstance.name+")");
			}
			else System.out.print(" Stop Notification ");
			if(ScheduledActivity.class.isInstance(notice.behaviourInstance))
			{
				ScheduledActivity schAct = (ScheduledActivity) notice.behaviourInstance;
				if(schAct.eventSched == ScheduledActivity.EventScheduled.STARTING_EVENT)
				    System.out.print("   (starting event scheduled)");
				else
				    System.out.print("   (terminating event scheduled)");
			}
			System.out.println();
		}
		System.out.println("----------------------");
	}
	// Standard Procedure to start Sequel Activities with no parameters
	protected void spStart(SequelActivity seqAct)
	{
		seqAct.startingEvent();
		scheduleActivity(seqAct);
	}	
	protected double getClock() {return super.getClock();}
}


