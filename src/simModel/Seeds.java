package simModel;

import cern.jet.random.engine.RandomSeedGenerator;

public class Seeds 
{
	
	
	int[] seeds = new int[8];
	int index = 0;

	public Seeds(RandomSeedGenerator rsg)
	{
		for (int i = 0 ; i < seeds.length; i++) seeds[i] = rsg.nextSeed();
		this.index = 0;
	}
	
	public int getSeed(){
		if (index >= 8) index = 0;
		int r = seeds[index];
		index++;
		return r;
	}
}
