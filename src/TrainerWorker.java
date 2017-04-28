import java.util.Random;


public class TrainerWorker 
{
	
	
	public static void initialize(Chromosome[] trainers, Chromosome[] population, Random RNG)
	{
		int randomIndex;
		for(int i = 0 ; i < trainers.length ; i++)
		{
			randomIndex = RNG.nextInt(population.length);
			trainers[i] = new Chromosome(population[randomIndex]);	
		}
	}
	
	public static void newTrainers(Chromosome[] trainers, Chromosome[] population, double[][][] predictors)
	{
		double[] variance = new double[population.length];
		double[] predictorFitness = new double[predictors.length];
		double predictorAvgFitness = 0;
		//double[][] tempPredictor = new double[1][];
		
		//this loop calculates the variance for each
		for(int i = 0 ; i < population.length ; i++)
		{
			predictorAvgFitness = 0;
			for(int j = 0 ; j < predictors.length ; j++)
			{
				//tempPredictor[0] = predictors[j];
				predictorFitness[j] = Evaluator.fitnessMSE(population[i], predictors[j]);
				predictorAvgFitness += predictorFitness[j];
			}
			predictorAvgFitness = predictorAvgFitness/predictors.length;
			

			for(int j = 0 ; j < predictors.length ; j++)
			{
				variance[i] += Math.pow(predictorFitness[j] - predictorAvgFitness, 2);
				
				//if(!Double.isFinite(variance[i])) //if it's too big, make it MAX_VALUE
				//{
				//	variance[i] = Double.MAX_VALUE;
				//}
			}
			variance[i] = variance[i]/predictors.length;
		}

		
		//find the one with the max variance
		int maxIndex = 0;
		double maxValue = 0;
		for(int i = 0 ; i < variance.length ; i++)
		{
			if(variance[i] > maxValue)
			{
				maxValue = variance[i];
				maxIndex = i;
			}
		}
		
		//for(int i = 0 ; i < trainers.length ; i++)
		//{
		//	System.out.println("\t\t" + trainers[i]);
		//}
		
		//replace the oldest
		for(int i = trainers.length - 1 ; i > 0 ; i--)
		{
			trainers[i] = trainers[i-1];		
		}
		//trainers[0].copyChromosome(population[maxIndex]);
		trainers[0] = new Chromosome(population[maxIndex]);
				
		//System.out.println("\t\t" + trainers[0]);
	
		//for(int i = 0 ; i < trainers.length ; i++)
		//{
		//	System.out.println("\t\t" + trainers[i]);
		//}
		//System.out.println(maxValue);
	}
	


	
}
