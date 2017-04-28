import java.util.Arrays;
import java.util.Random;

public class GeneticOperatorsPredictor 
{

	public static void crossover(double[][] c1, double[][] c2, Random RNG)
	{
		int crossPoint = RNG.nextInt(c1.length);
		double[] toCopy = new double[c1[0].length];
		
		for(int i = crossPoint ; i < c1.length ; i++)
		{
			//toCopy = new double[c1[i].length];
			toCopy = Arrays.copyOf(c1[i], c1[i].length);
			c1[i] = Arrays.copyOf(c2[i], c2[i].length);
			c2[i] = Arrays.copyOf(toCopy, toCopy.length);
		}
		
	}
	
	public static void mutate(double[][] c, Random RNG, double[][] theData)
	{
		int mutatePoint = RNG.nextInt(c.length);
		int dataPoint = RNG.nextInt(theData.length);
		c[mutatePoint] = Arrays.copyOf(theData[dataPoint], theData[dataPoint].length);
	}
	
}
