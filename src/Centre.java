import java.io.File;
import java.util.Random;

public class Centre 
{

	static int SEED;
	
	static int NUMBER_RUNS;
	static int POPULATION_SIZE;
	static int NUMBER_ISLANDS;
	static int NUMBER_MIGRATIONS;
	static int GENERATIONS;
	static int NUMBER_GENES;
	static double CROSSOVER_RATE;
	static double MUTATION_RATE;
	static int MUTATION_NUMBER;
	static int RING_DISTANCE_RATE;
	
	static int NUMBER_TRAINERS;
	static int NUMBER_PREDICTORS;
	static double PREDICTOR_SIZE;
	//static double PREDICTOR_GENS;
	
	static int SEGMENT;
	
	static Chromosome[][] curPop;
	static Chromosome[][] newPop;
	static Chromosome[] wholePop;
	
	static Thread[] islands;
	
	static Chromosome[] trainers;
	static double[][][] predictors;
	static double[][][] newPredictors;
	
	static double[][] theData;
	
	static double[][] stats;
	
	static Random RNG;
	
	
	//static int[] testingPredictors = new int[122];
	
	public static void main(String[] args) throws InterruptedException 
	{
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		SEED = 1;
		NUMBER_RUNS = 1;
		POPULATION_SIZE = 101;		//per island, i.e. POPULATION_SIZE * NUMBER_ISLANDS		ALSO, needs to be odd for elite (I was lazy)
		NUMBER_ISLANDS = 7;
		NUMBER_MIGRATIONS = 100;//	(CHANGE ME) //10000 was amazing (with 2500gens --- otherwise, wait like a day....)
		GENERATIONS = 10100;// 
		NUMBER_GENES = 40;	//40 worked well // = 16; // = 32;
		CROSSOVER_RATE = 80;		//%			//DONE THIS WAY BECAUSE NEXT INT IS APPARENTLY FASTER THAN DOUBLE (with rand)
		MUTATION_RATE = 10;			//%
		MUTATION_NUMBER = 2;
		RING_DISTANCE_RATE = 5;
		
		NUMBER_TRAINERS = 8;
		NUMBER_PREDICTORS = 10;
		///////////////////////////////
		PREDICTOR_SIZE = 0.25;		//rate based on theData size (NORMALLY 0.1, FOR COMBINATION, 0.01!!)
		//
		//PREDICTOR_GENS = 0.05;      //rate based on GENERATIONS (NOT GENES)
		
		SEGMENT = 0;
		
		///////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String fileName = "outs/";
		for (int i = 0; i < args.length; i++) 
		{
			if(i != 0) fileName = fileName + "_";
			fileName = fileName + args[i];
		}
		
		
		new File(fileName).mkdir();
		fileName = fileName + "/";
		
		stats = new double[NUMBER_RUNS][];
		//tried GAMB, MOTOR, WM
		//theData = Reader.readData("../../../../WCCI2015/Walking/DATA/", "CH-FR-LACC-RAW-RM-5hz-1000-15000.csv");
		//theData = Reader.readData("../roiData/", "MOTOR_100307_3_L33.csv");
		theData = Reader.readData("../roiDATA/", "MOTOR_100307_2_L21_Z.csv");		//CHANGE THE PREDICTOR SIZE IF USING THIS!!!
		Language.setVarSymbolsNumbers(theData[0].length - 1);
		//Language.setVarSymbols(Reader.readVarNames("./", "variableNames2.csv"));
		
		
		//TEST AREA
		//RNG = new Random(SEED);
		//Trainers myTrainer = new Trainers(10);
		//myTrainer.initialize(theData, RNG);
		
		//TEST AREA
		
		ThreadKiller tKiller = new ThreadKiller();
		for(int i = 0 ; i < NUMBER_RUNS ; i++)
		{
			System.out.println(i);
			//SEED = i;
			//SEED = 1;
			SEED = (int)System.currentTimeMillis();
			RNG = new Random(SEED);
			//int noLearnCount = 0;
			//double lastBestFitness = Double.MAX_VALUE;
			generatePopulations();

			Chromosome bestOverAllMigrations = new Chromosome(NUMBER_GENES, RNG);
			bestOverAllMigrations.fitnessValue = Double.MAX_VALUE;
			
			trainers = new Chromosome[NUMBER_TRAINERS];
			predictors = new double[NUMBER_PREDICTORS][][];
			newPredictors = new double[NUMBER_PREDICTORS][][]; 
			combinePops();
			TrainerWorker.initialize(trainers, wholePop, RNG);
			PredictorWorker.initialize(predictors, (int)(theData.length * PREDICTOR_SIZE), theData, RNG);
			PredictorWorker.initialize(newPredictors, (int)(theData.length * PREDICTOR_SIZE), theData, RNG);

			
			//loads the save state if the segment isn't zero
			if(SEGMENT > 0)
			{
				StateSaveRestore.restoreStateSer("./", SEGMENT - 1, bestOverAllMigrations, curPop, trainers, predictors);	
			}
						
			for(int j = 0 ; j < NUMBER_MIGRATIONS ; j++)
			{
				tKiller.setDie(false);		//REVIVEEE
				Thread pool = new Thread(new PredictorPool(NUMBER_PREDICTORS, CROSSOVER_RATE, MUTATION_RATE, MUTATION_NUMBER, RNG, predictors, newPredictors, trainers, theData, tKiller));
				pool.start();
				for(int k = 0 ; k < islands.length ; k++)
				{
					islands[k] = new Thread(new Island(POPULATION_SIZE, GENERATIONS, NUMBER_MIGRATIONS, CROSSOVER_RATE, MUTATION_RATE, MUTATION_NUMBER, RING_DISTANCE_RATE, RNG, curPop[k], newPop[k], theData));
					islands[k].start();
				}
				for(int k = 0 ; k < islands.length ; k++)
				{
					islands[k].join();
				}
				tKiller.setDie(true);		//KILLER
				pool.join();	/////////////////////////////////////
				combinePops();	
				
				Evaluator.calcAllFitnessMSE(wholePop, theData);
				Chromosome curBestResult = Evaluator.findBestChromosomeMin(wholePop);
				if(curBestResult.fitnessValue < bestOverAllMigrations.fitnessValue)
				{
					bestOverAllMigrations = curBestResult;
				}
				//System.out.println(bestOverAllMigrations.fitnessValue);
				/*
				for(int w = 0 ; w < wholePop.length ; w++)
				{
					System.out.print(wholePop[w].fitnessValue + ",");
				}
				System.out.println();
				System.out.println();
				*/
				
				TrainerWorker.newTrainers(trainers, wholePop, predictors);		////////////////////////////////
				shuffle(wholePop);
				separatePops();

				//System.out.println("\t\t\t\t " + PredictorWorker.evaluatePredictorFitness(PredictorWorker.bestPredictor, theData, trainers));
				//for(int k = 0 ; k < PredictorWorker.bestPredictor.length ; k++)
				//{
					//System.out.print("\t\t" + PredictorWorker.bestPredictor[k][0]);
					//testingPredictors[(int)((PredictorWorker.bestPredictor[k][0] + 3) * 20)]++;
				//}
				//System.out.println();
				
			}
			
			//
			StateSaveRestore.saveStateSer("./", SEGMENT, bestOverAllMigrations, curPop, trainers, predictors);
			//StateSaveRestore.saveStateStr("./", SEGMENT, bestOverAllMigrations, curPop, trainers, predictors);
			StateSaveRestore.deleteStateSer("./", SEGMENT - 1);
			//
			
			combinePops();
			Evaluator.calcAllFitnessMSE(wholePop, theData);
			Chromosome curBestResult = Evaluator.findBestChromosomeMin(wholePop);
			if(curBestResult.fitnessValue < bestOverAllMigrations.fitnessValue)
			{
				bestOverAllMigrations = curBestResult;
			}
			//stats[i] = new double[3];
			//stats[i][0] = bestResult.fitnessValue;
			//stats[i][1] = Evaluator.calcAvgFitness(wholePop);
			//stats[i][2] = Evaluator.calcStandardDeviation(wholePop);
			Printer.printLine(fileName + i, bestOverAllMigrations);
			Printer.printMulti(fileName + i, bestOverAllMigrations);
			Printer.printCurStat(fileName + i, bestOverAllMigrations);			//quick fix... I wish I did this a lot earlier (screw you automatic windows updates)
			System.out.println("\t\t" + bestOverAllMigrations.fitnessValue);
			
			
			//for(int k = 0 ; k < testingPredictors.length ; k++)
			//{
			//	System.out.println((((double)k/20)-3) + "\t" + testingPredictors[k]);
			//}
			
		}
		//Printer.printStats(fileName, stats);
	}
	
	
	
	
	public static void generatePopulations()
	{
		curPop = new Chromosome[NUMBER_ISLANDS][];
		newPop = new Chromosome[NUMBER_ISLANDS][];
		
		wholePop = new Chromosome[POPULATION_SIZE * NUMBER_ISLANDS];
		
		islands = new Thread[NUMBER_ISLANDS];
		
		for(int i = 0 ; i < NUMBER_ISLANDS ; i++)
		{
			curPop[i] = new Chromosome[POPULATION_SIZE];
			newPop[i] = new Chromosome[POPULATION_SIZE];
			
			for(int j = 0 ; j < curPop[i].length ; j++)
			{
				curPop[i][j] = new Chromosome(NUMBER_GENES, RNG);
				curPop[i][j].genChromosome();
				newPop[i][j] = new Chromosome(NUMBER_GENES, RNG);
				newPop[i][j].genChromosome();
			}		
		}
	}
	
	
	public static void combinePops()
	{
		int indexCounter = 0;
		
		for(int i = 0 ; i < NUMBER_ISLANDS ; i++)
		{
			for(int j = 0 ; j < curPop[i].length ; j++)
			{
				wholePop[indexCounter++] = curPop[i][j];
			}
		}	
	}
	
	public static void separatePops()
	{
		int indexCounter = 0;
		
		for(int i = 0 ; i < NUMBER_ISLANDS ; i++)
		{
			for(int j = 0 ; j < curPop[i].length ; j++)
			{
				curPop[i][j] = wholePop[indexCounter++];
			}
		}	
	}
	
	public static void shuffle(Chromosome[] inA)
	{
		Chromosome temp;
		int i = inA.length - 1;
		int j;
		
		while(i > 1)
		{
			j = RNG.nextInt(i+1);
			temp = inA[i];
			inA[i] = inA[j];
			inA[j] = temp;
			i--;
		}	
	}
	
	
	
	

}
