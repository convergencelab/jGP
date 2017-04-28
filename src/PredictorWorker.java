import java.util.Arrays;
import java.util.Random;


public class PredictorWorker 
{
	static double[][] bestPredictor;
	
	public static void initialize(double[][][] predictors, int predSamples, double[][] theData, Random RNG)
	{
		int copyIndex;
		for(int i = 0 ; i < predictors.length ; i++)
		{
			predictors[i] = new double[predSamples][];
			for(int j = 0 ; j < predictors[i].length ; j++)
			{
				copyIndex = RNG.nextInt(theData.length);
				predictors[i][j] = new double[theData[copyIndex].length];
				predictors[i][j] = Arrays.copyOf(theData[copyIndex], theData[copyIndex].length);
			}
		}
		bestPredictor = Arrays.copyOf(predictors[0], predictors[0].length);		//Just to make sure it's init
	}
	
	public static double[] evaluateAllPredictorFitness(double[][][] predictors, double[][] theData, Chromosome[] trainers)
	{
		double[] allPredictorFitness = new double[predictors.length];
		for(int i = 0 ; i < allPredictorFitness.length ; i++)
		{
			allPredictorFitness[i] = evaluatePredictorFitness(predictors[i], theData, trainers);
		}
		return allPredictorFitness;
	}
	
	//Should this be in "Evaluator" class?
	public static double evaluatePredictorFitness(double[][] predictor, double[][] theData, Chromosome[] trainers)
	{
		double predFitness = 0;

		for(int i = 0 ; i < trainers.length ; i++)
		{
			predFitness += Math.abs(Evaluator.fitnessMSE(trainers[i], theData) - Evaluator.fitnessMSE(trainers[i], predictor));
		}
		//if(!Double.isFinite(predFitness)) //if it's too big, make it MAX_VALUE
		//{
		//	predFitness = Double.MAX_VALUE;
		//}
		return predFitness/trainers.length;
	}
	
	public static int findBestPredictorIndex(double[] allPredictorFitness)
	{
		int bestIndex = 0;
		double bestFitness = Double.MAX_VALUE;
		
		for(int i = 0 ; i < allPredictorFitness.length ; i++)
		{
			if(allPredictorFitness[i] < bestFitness)
			{
				bestFitness = allPredictorFitness[i];
				bestIndex = i;
			}
		}
		//System.out.println(bestFitness);
		return bestIndex;
	}
	
	public static void setBestPredictor(double[][] inBestPredictor)
	//public synchronized static void setBestPredictor(double[][] inBestPredictor)		//DOES IT RELLY NEED TO BE SYNCED? I THINK IT DOES!!, BUT WHEN I DON'T IT's A LITTTLE FASTER
	{
		//bestPredictor = Arrays.copyOf(inBestPredictor, inBestPredictor.length);
		bestPredictor = inBestPredictor;
	}
	
	public static double[][] getBestPredictor()
	{
		return bestPredictor;
	}
	
	
}
