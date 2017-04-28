import java.io.Serializable;
import java.util.Random;

public class Chromosome implements Serializable
{
	Gene[] geneArray;
	int numGenes;
	double fitnessValue = Double.MAX_VALUE;
	Random RNG;
	
	public Chromosome(int innumGenes, Random inRNG)
	{
		numGenes = innumGenes;
		geneArray = new Gene[numGenes];
		for(int i = 0 ; i < geneArray.length ; i++)
		{
			geneArray[i] = new Gene();
		}
		RNG = inRNG;
	}
	
	public Chromosome(Chromosome toCopy)
	{
		numGenes = toCopy.numGenes;
		fitnessValue = toCopy.fitnessValue;
		RNG = toCopy.RNG;
		geneArray = new Gene[numGenes];
		copyGenes(toCopy.geneArray);
	}
	
	public void copyChromosome(Chromosome toCopy)
	{
		numGenes = toCopy.numGenes;
		//geneArray = new Gene[numGenes];
		fitnessValue = toCopy.fitnessValue;
		RNG = toCopy.RNG;
		for(int i = 0 ; i < geneArray.length ; i++)
		{
			geneArray[i].copyGene(toCopy.geneArray[i]);
		}
	}
	
	public void copyGenes(Gene[] toCopy)
	{
		for(int i = 0 ; i < geneArray.length ; i++)
		{
			geneArray[i] = new Gene(toCopy[i]);
		}
	}
	
	public void genChromosome()
	{
		for(int i = 0 ; i < numGenes ; i++)
		{
			if(i == 0 || i == 1)
			{
				geneArray[i] = makeTerminal();
			}
			else
			{
				geneArray[i] = makeGene(i);
			}
		}
	}
	
	public Gene makeGene(int curIndex)
	{
		//if(RNG.nextDouble() > 0.5)		//how likely should we generate a terminal or an operator? MAYBE THIS SHOULD BE DYNAMIC? MAYBE THIS DOEN'T REALLY MATTER CONSIDERING THIS IS INITILIZATION?
		if(RNG.nextDouble() > 0.5)
		{
			return makeTerminal();
		}
		else
		{
			return makeOperator(curIndex);
		}
	}
	
	//selects whether to return a binary or unary operator. Likelihood is proportional to the number of operators of each type.
	public Gene makeOperator(int curIndex)
	{
		double decider = (RNG.nextDouble() * (Language.binaryOperatorSymbols.length + Language.unaryOperatorSymbols.length));	//get a random number between 0 and the total number of operators (binary + unary)
		decider = decider - Language.unaryOperatorSymbols.length;	//subtract number of unary operators
		if(decider < 0)
		{
			return makeUnaryOperatorGene(curIndex);
		}
		else
		{
			return makeBinaryOperatorGene(curIndex);
		}
	}
	
	public Gene makeTerminal()
	{
		if(RNG.nextDouble() > 0.5)
		{
			return makeConstantGene();		//*** change to add constants
			//return makeVariableGene();
		}
		else
		{
			return makeVariableGene();
		}
	}
	
	public Gene makeVariableGene()
	{
		int whichVariable = RNG.nextInt(Language.varSymbols.length);	//-1 because we want an array index
		return new Gene('v', whichVariable);
	}
	
	public Gene makeConstantGene()	//can alter to have doubles or ints
	{
		double constantValue = (RNG.nextDouble() * (2 * Language.constantLimit)) - Language.constantLimit;
		//double constantValue = RNG.nextInt(10);											
		return new Gene('c', constantValue);
	}
	
	public Gene makeBinaryOperatorGene(int curIndex)
	{
		int whichOperator = RNG.nextInt(Language.binaryOperatorSymbols.length);	//-1 because we want an array index
		String operator = Language.binaryOperatorSymbols[whichOperator];
		int ref1 = RNG.nextInt(curIndex);
		int ref2 = RNG.nextInt(curIndex);
		return new Gene('b', operator, ref1, ref2);
	}
	
	public Gene makeUnaryOperatorGene(int curIndex)
	{
		int whichOperator = RNG.nextInt(Language.unaryOperatorSymbols.length);	//-1 because we want an array index
		String operator = Language.unaryOperatorSymbols[whichOperator];
		int ref1 = RNG.nextInt(curIndex);
		return new Gene('u', operator, ref1, -1);
	}
	
	public static void copyPopulation(Chromosome[] to, Chromosome[] from)
	{
		for(int i = 0 ; i < from.length ; i++)
		{
			//to[i] = new Chromosome(from[i]);
			to[i].copyChromosome(from[i]);
		}
	}
	
	public String toString()
	{
		String s = "" + numGenes + ", " + fitnessValue;
		
		for(int i = 0 ; i < numGenes ; i++)
		{
			s = s + ", " + geneArray[i].toString();
		}
		
		s = s + "\n";
		
		return s;
	}
	
	
}
