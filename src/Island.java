import java.util.Random;

public class Island implements Runnable
{
	int POPULATION_SIZE;
	int GENERATIONS;
	int NUMBER_MIGRATIONS;
	double CROSSOVER_RATE;
	double MUTATION_RATE;
	int MUTATION_NUMBER;
	int RING_DISTANCE_RATE;
	
	Random RNG;
	
	Chromosome[] curPop;
	Chromosome[] newPop;
	Chromosome[] swappie;
	double[][] theData;
	
	public Island(int inPOPULATION_SIZE, int inGENERATIONS, int inNUMBER_MIGRATIONS, double inCROSSOVER_RATE, double inMUTATION_RATE, int inMUTATION_NUMBER, int inRING_DISTANCE_RATE, Random inRNG, Chromosome[] inCurPop, Chromosome[] inNewPop, double[][] inTheData)
	{
		POPULATION_SIZE = inPOPULATION_SIZE;
		GENERATIONS = inGENERATIONS;
		NUMBER_MIGRATIONS = inNUMBER_MIGRATIONS;
		CROSSOVER_RATE = inCROSSOVER_RATE;
		MUTATION_RATE = inMUTATION_RATE;
		MUTATION_NUMBER = inMUTATION_NUMBER;
		RING_DISTANCE_RATE = inRING_DISTANCE_RATE;
		
		RNG = inRNG;
		
		curPop = inCurPop;
		newPop = inNewPop;
		
		theData = inTheData;
	}
	
	public void run()
	{
		//System.out.println((GENERATIONS / POPULATION_SIZE));
		//for(int j = 0 ; j < (GENERATIONS / POPULATION_SIZE)/NUMBER_MIGRATIONS; j++)		//should this also have /NUMBER_ISLANDS?
		for(int j = 0 ; j < (GENERATIONS / POPULATION_SIZE); j++)
		{
			//System.out.println("\t" + j);
			mating();
			//if(curPop[0].fitnessValue < lastBestFitness)		//currently not implimented: could be done to stop evolution to allow more migrations
			//{
				//lastBestFitness = curPop[0].fitnessValue;
				//noLearnCount = 0;
			//}
			//else noLearnCount++;
		}
	}
	
	public void mating()
	{
		int first;
		int second;
		//int middle;
		int numToMutate;
		//Evaluator.calcAllFitnessMSE(curPop, theData);
		//Evaluator.calcAllFitnessMSE(curPop, PredictorWorker.bestPredictor);		/////////////////////////////// (I guess technically slower)
		//Evaluator.calcAllFitnessMSEIterate(curPop, PredictorWorker.bestPredictor);		/////////////////////////////// (iterate)
		Evaluator.calcAllFitnessMSE2(curPop, PredictorWorker.bestPredictor);
		newPop[0] = Evaluator.findBestChromosomeMin(curPop);	//elitism
		
		for(int i = 1 ; i < POPULATION_SIZE ; i += 2)		//POPULATION_SIZE must be odd because of eleitism and I'm lazy
		{	
			//if(curPop[middle = RNG.nextInt(POPULATION_SIZE)].fitnessValue < curPop[first = RNG.nextInt(POPULATION_SIZE)].fitnessValue) first = middle;
			//if(curPop[middle = RNG.nextInt(POPULATION_SIZE)].fitnessValue < curPop[second = RNG.nextInt(POPULATION_SIZE)].fitnessValue) second = middle;
			first = selectionMin();
			second = selectionMin();					//use this line to make regular		**************************************************************
			//second = selectionRingMin(first);			//use this line to make RingSpecies **************************************************************
			
			newPop[i].copyChromosome(curPop[first]);
			newPop[i + 1].copyChromosome(curPop[second]);
			//newPop[i] = new Chromosome(curPop[first]);
			//newPop[i+1] = new Chromosome(curPop[second]);
			
			//crossover
			if(RNG.nextInt(100) < CROSSOVER_RATE)
			{
				GeneticOperatorsSolution.crossover(newPop[i], newPop[i+1], RNG);
			}
			//mutation
			if(RNG.nextInt(100) < MUTATION_RATE)
			{
				numToMutate = RNG.nextInt(MUTATION_NUMBER) + 1;	//+1 because bounds is < value    //WAIT, NEXT INT IS APPARENTLY FASTER THAN THE ALTERNATIVES
				for(int j = 0 ; j < numToMutate ; j++)
				{
					GeneticOperatorsSolution.mutate(newPop[i], RNG);
				}
			}
			if(RNG.nextInt(100) < MUTATION_RATE)
			{
				numToMutate = RNG.nextInt(MUTATION_NUMBER + 1);	//+1 because bounds is < value
				for(int j = 0 ; j < numToMutate ; j++)
				{
					GeneticOperatorsSolution.mutate(newPop[i+1], RNG);
				}
			}
		}
		//switch newPop to curPop
		//Chromosome.copyPopulation(curPop, newPop);
		swappie = curPop;
		curPop = newPop;
		newPop = swappie;
	}
	
	public int selectionMin()
	{
		int middle;
		int selected;
		if(curPop[middle = RNG.nextInt(POPULATION_SIZE)].fitnessValue < curPop[selected = RNG.nextInt(POPULATION_SIZE)].fitnessValue) selected = middle;
		return selected;
	}
	
	public int selectionRingMin(int firstSelect)
	{
		int middle;
		int selected;
		
		int RNGcap = POPULATION_SIZE/RING_DISTANCE_RATE;
		// ((RESULT - (RESULT/2)) + firstSelect) % POPULATION_SIZE	
		//if(curPop[middle = ((RNG.nextInt(RNGcap) - RNGcap) + firstSelect) % POPULATION_SIZE].fitnessValue > curPop[selected = ((RNG.nextInt(RNGcap) - RNGcap) + firstSelect) % POPULATION_SIZE].fitnessValue) selected = middle;
		
		middle = ((RNG.nextInt(RNGcap) - RNGcap/2) + firstSelect) % POPULATION_SIZE;
		selected = ((RNG.nextInt(RNGcap) - RNGcap/2) + firstSelect) % POPULATION_SIZE;
		
		if(middle < 0)
		{
			middle = middle + POPULATION_SIZE;		//solves the negative value issue
		}
		if(selected < 0)
		{
			selected = selected + POPULATION_SIZE;	//solves the negative value issue
		}

		if(curPop[middle].fitnessValue < curPop[selected].fitnessValue)
		{
			selected = middle;
		}
		return selected;
	}
	
	// Deterministic Crowding? 
	// "NICHING METHODS FOR GENETIC ALGORITHMS"
}
