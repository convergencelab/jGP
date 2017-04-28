import java.util.Random;

public class GeneticOperatorsSolution 
{

	public static void crossover(Chromosome c1, Chromosome c2, Random RNG)
	{
		//int crossPoint = (int)Math.round((Math.random() * (c1.numGenes * 0.8)) + (c1.numGenes * 0.1));	//random number at least 10% in at either side
		int crossPoint = RNG.nextInt(c1.numGenes);
		Gene geneToCopy;
		for(int i = crossPoint ; i < c1.numGenes ; i++)
		{
			geneToCopy = c1.geneArray[i];
			c1.geneArray[i] = c2.geneArray[i];
			c2.geneArray[i] = geneToCopy;
		}
	}
	
	public static void mutate(Chromosome c, Random RNG)
	{
		//int mutatePoint = (int)Math.round(Math.random() * (c.numGenes -1));
		int mutatePoint = RNG.nextInt(c.numGenes);
		if(mutatePoint == 0 || mutatePoint ==1)
		{
			c.geneArray[mutatePoint] = c.makeTerminal();
		}
		else
		{
			c.geneArray[mutatePoint] = c.makeGene(mutatePoint);
		}
	}
}
