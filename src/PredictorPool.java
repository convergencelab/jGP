import java.util.Arrays;
import java.util.Random;

public class PredictorPool implements Runnable
{
	int PREDICTOR_POPULATION_SIZE;
	//int GENERATIONS;			//THIS GENERATION IS HOW MANY/MIGRATION
	//int NUMBER_MIGRATIONS;
	double CROSSOVER_RATE;
	double MUTATION_RATE;
	int MUTATION_NUMBER;
	
	Random RNG;
	
	double[][][] curPredictor;
	double[][][] newPredictors;
	double[][][] swappie;
	double[][] theData;
	
	Chromosome[] trainers;
	
	ThreadKiller tKiller;
	
	
	public PredictorPool(int inPREDICTOR_POPULATION_SIZE, double inCROSSOVER_RATE, double inMUTATION_RATE, int inMUTATION_NUMBER, Random inRNG, double[][][] inCurPredictors, double[][][] inNewPreductors, Chromosome[] inTrainers, double[][] inTheData, ThreadKiller inKiller)
	{
		PREDICTOR_POPULATION_SIZE = inPREDICTOR_POPULATION_SIZE;
		//GENERATIONS = inGENERATIONS;
		//NUMBER_MIGRATIONS = inNUMBER_MIGRATIONS;
		CROSSOVER_RATE = inCROSSOVER_RATE;
		MUTATION_RATE = inMUTATION_RATE;
		MUTATION_NUMBER = inMUTATION_NUMBER;
		RNG = inRNG;
		curPredictor = inCurPredictors;
		newPredictors = inNewPreductors;
		trainers = inTrainers;
		theData = inTheData;
		
		tKiller = inKiller;
	}
	
	public void run()
	{
		//System.out.println((GENERATIONS/PREDICTOR_POPULATION_SIZE));
		//for(int j = 0 ; j < (GENERATIONS/PREDICTOR_POPULATION_SIZE)/NUMBER_MIGRATIONS ; j++)
		//for(int j = 0 ; j < (GENERATIONS/PREDICTOR_POPULATION_SIZE) ; j++)
		//int count = 0;
		while(!tKiller.getDie())
		{
			//count++;
			mating();
		}
		//System.out.println("\t\t\t"+count);
	}
	
	public void mating()
	{
		int first;
		int second;
		int numToMutate;
		double[] allPredictorFitness = PredictorWorker.evaluateAllPredictorFitness(curPredictor, theData, trainers);
	
		for(int i = 0 ; i < PREDICTOR_POPULATION_SIZE ; i+= 2)
		{
			first = selectMin(allPredictorFitness);
			second = selectMin(allPredictorFitness);
			
			//newPredictors[i] = Arrays.copyOf(curPredictor[first], curPredictor[first].length);
			//newPredictors[i+1] = Arrays.copyOf(curPredictor[second], curPredictor[second].length);
			
			//I THINK THIS IS CORRECT, DOUBLE CHECK!
			
			for(int m = 0 ; m < newPredictors[i].length ; m++)
			{
				for(int n = 0 ; n < newPredictors[i][m].length ; n++)
				{
					newPredictors[i][m][n] = curPredictor[first][m][n];
					newPredictors[i+1][m][n] = curPredictor[second][m][n];
				}
			}
			
			if(RNG.nextInt(100) < CROSSOVER_RATE)
			{
				GeneticOperatorsPredictor.crossover(newPredictors[i], newPredictors[i+1], RNG);
			}
			//mutation
			if(RNG.nextInt(100) < MUTATION_RATE)
			{
				numToMutate = RNG.nextInt(MUTATION_NUMBER) + 1;	//+1 because bounds is < value
				for(int j = 0 ; j < numToMutate ; j++)
				{
					GeneticOperatorsPredictor.mutate(newPredictors[i], RNG, theData);
				}
			}
			if(RNG.nextInt(100) < MUTATION_RATE)
			{
				numToMutate = RNG.nextInt(MUTATION_NUMBER + 1);	//+1 because bounds is < value
				for(int j = 0 ; j < numToMutate ; j++)
				{
					GeneticOperatorsPredictor.mutate(newPredictors[i+1], RNG, theData);
				}
			}
		}
		//curPredictor = Arrays.copyOf(newPredictors, newPredictors.length);
		swappie = curPredictor;
		curPredictor = newPredictors;
		newPredictors = swappie;
		
		PredictorWorker.setBestPredictor(curPredictor[PredictorWorker.findBestPredictorIndex(allPredictorFitness)]);	//Maybe we move this, or delay it?
		//for(int k = 0 ; k < PredictorWorker.bestPredictor.length ; k++)
		//{
		//	System.out.print("\t\t" + PredictorWorker.bestPredictor[k][0]);
		//}
		//System.out.println();
	}
	
	public int selectMin(double[] allPredictorFitness)
	{
		int middle;
		int selected;
		if(allPredictorFitness[middle = RNG.nextInt(PREDICTOR_POPULATION_SIZE)] < allPredictorFitness[selected = RNG.nextInt(PREDICTOR_POPULATION_SIZE)]) selected = middle;
		return selected;
	}
	
}
