import java.util.Vector;
//THIS CLASS NEEDS HARD CODING BASED ON LANGUAGE
public class Evaluator 
{
	
	public static void calcAllFitnessMSE(Chromosome[] inChromo, double[][] theData)
	{
		for(int i = 0 ; i < inChromo.length ; i++)
		{
			inChromo[i].fitnessValue = fitnessMSE(inChromo[i], theData);
			if(!Double.isFinite(inChromo[i].fitnessValue)) 
			{
				inChromo[i].fitnessValue = Double.MAX_VALUE;
			}
		}
	}
	
	//uses passes the variables array for less overhead
	public static void calcAllFitnessMSE2(Chromosome[] inChromo, double[][] theData)
	{
		double[] variables = new double[Language.varSymbols.length];
		for(int i = 0 ; i < inChromo.length ; i++)
		{
			inChromo[i].fitnessValue = fitnessMSE2(inChromo[i], theData, variables);
			if(!Double.isFinite(inChromo[i].fitnessValue)) 
			{
				inChromo[i].fitnessValue = Double.MAX_VALUE;
			}
		}
	}
	
	public static void calcAllFitnessMSEIterate(Chromosome[] inChromo, double[][] theData)
	{
		double[] variables = new double[Language.varSymbols.length];
		double[] results = new double[inChromo[0].geneArray.length];
		boolean[] isIn = new boolean[inChromo[0].geneArray.length];
		//Vector<Integer> toDo = new Vector<Integer>(inChromo[0].geneArray.length);
		Vector<Integer> toDo = new Vector<Integer>();
		for(int i = 0 ; i < inChromo.length ; i++)
		{
			inChromo[i].fitnessValue = fitnessMSEIterate(inChromo[i], theData, variables, results, isIn, toDo);
			if(!Double.isFinite(inChromo[i].fitnessValue)) 
			{
				inChromo[i].fitnessValue = Double.MAX_VALUE;
			}
		}
	}
	
	public static void calcAllFitnessCOR(Chromosome[] inChromo, double[][] theData)
	{
		for(int i = 0 ; i < inChromo.length ; i++)
		{
			inChromo[i].fitnessValue = fitnessCOR(inChromo[i], theData);
			if(!Double.isFinite(inChromo[i].fitnessValue)) 
			{
				inChromo[i].fitnessValue = Double.MAX_VALUE;
			}
		}
	}
	
	
	//Fitness needs to be calculated first
	public static double calcAvgFitness(Chromosome[] inChromo)
	{
		double avgFit = 0;
		for(int i = 0 ; i < inChromo.length ; i++)
		{
			//avgFit += fitness(inChromo[i]);
			avgFit += inChromo[i].fitnessValue;
		}
		avgFit = avgFit/inChromo.length;
		if(!Double.isFinite(avgFit))	//finite, not infinite!
		{
			avgFit = Double.MAX_VALUE;
		}
		return avgFit;
	}
	
	public static double calcStandardDeviation(Chromosome[] inChromo)
	{
		double avgFit = calcAvgFitness(inChromo);
		double sumSquared = 0;
		for(int i = 0 ; i < inChromo.length ; i++)
		{
			sumSquared = Math.pow(inChromo[i].fitnessValue - avgFit, 2);
		}
		double stDev = Math.sqrt(sumSquared/inChromo.length);
		if(!Double.isFinite(stDev))
		{
			stDev = Double.MAX_VALUE;
		}
		return stDev;
	}
	
	public static double calcStandardDeviation(double[] inA, double inAvg)
	{
		double sumSquared = 0;
		for(int i = 0 ; i < inA.length ; i++)
		{
			sumSquared = Math.pow(inA[i] - inAvg, 2);
		}
		double stDev = Math.sqrt(sumSquared/inA.length);
		if(!Double.isFinite(stDev))
		{
			stDev = Double.MAX_VALUE;
		}
		return stDev;
	}
	
	//returns a COPY of the best chromosome
	public static Chromosome findBestChromosomeMin(Chromosome[] inChromo)
	{
		int bestIndex = 0;
		double bestValue = Double.MAX_VALUE;
		
		for(int i = 0 ; i < inChromo.length ; i++)
		{
			if(inChromo[i].fitnessValue < bestValue)
			{
				bestIndex = i;
				bestValue = inChromo[i].fitnessValue;
			}
		}
		return new Chromosome(inChromo[bestIndex]);
	}
	
	//returns a COPY of the best chromosome
		public static Chromosome findBestChromosomeMax(Chromosome[] inChromo)
		{
			int bestIndex = -1;
			double bestValue = Double.MIN_VALUE;
			
			for(int i = 0 ; i < inChromo.length ; i++)
			{
				if(inChromo[i].fitnessValue > bestValue)
				{
					bestIndex = i;
					bestValue = inChromo[i].fitnessValue;
				}
			}
			return new Chromosome(inChromo[bestIndex]);
		}
	
	public static double fitnessMSE(Chromosome toTest, double[][] theData)//should this be mean squared error?
	{
		double totalSquaredError = 0;
		double[] variables = new double[Language.varSymbols.length];
		double expected;
		for(int i = 0 ; i < theData.length ; i++)
		{
			for(int j = 0 ; j < variables.length ; j++)
			{
				variables[j] = theData[i][j];
			}
			expected = theData[i][theData[i].length - 1];
			totalSquaredError += squaredError(expected, variables, toTest);
		}
		return totalSquaredError/theData.length;
		//return totalSquaredError;
	}
	
	//creates less overhead by taking a reference to the variables array
	public static double fitnessMSE2(Chromosome toTest, double[][] theData, double[] variables)
	{
		double totalSquaredError = 0;
		//double[] variables = new double[Language.varSymbols.length];
		double expected;
		for(int i = 0 ; i < theData.length ; i++)
		{
			for(int j = 0 ; j < variables.length ; j++)
			{
				variables[j] = theData[i][j];
			}
			expected = theData[i][theData[i].length - 1];
			totalSquaredError += squaredError(expected, variables, toTest);
		}
		return totalSquaredError/theData.length;
		//return totalSquaredError;
	}
	
	public static double fitnessMSEIterate(Chromosome toTest, double[][] theData, double[] variables, double[] results, boolean[] isIn, Vector<Integer> toDo)//should this be mean squared error?
	{
		double totalSquaredError = 0;
		double expected;
		for(int i = 0 ; i < theData.length ; i++)
		{
			for(int j = 0 ; j < variables.length ; j++)
			{
				variables[j] = theData[i][j];
			}
			expected = theData[i][theData[i].length - 1];
			toDo.clear();
			totalSquaredError += squaredErrorIterate(expected, variables, toTest, results, isIn, toDo);
		}
		return totalSquaredError/theData.length;
		//return totalSquaredError;
	}
	
	// covariance fitness --- this is busted
	public static double fitnessCOR(Chromosome toTest, double[][] theData)
	{
		double absError = 0;
		double[] variables = new double[Language.varSymbols.length];
		double[] expected = new double[theData.length];
		double eAvg = 0;
		double[] predicted = new double[theData.length];
		double pAvg = 0;
		
		for(int i = 0 ; i < theData.length ; i++)
		{
			for(int j = 0 ; j < variables.length ; j++)
			{
				variables[j] = theData[i][j];
			}
			expected[i] = theData[i][theData[i].length - 1];
			predicted[i] = evaluateChromosome(toTest.numGenes - 1, variables, toTest);
			absError += Math.abs(expected[i] - predicted[i]);
			eAvg += expected[i];
			pAvg += predicted[i];
		}
		
		absError = absError/theData.length;
		eAvg = eAvg/theData.length;
		pAvg = pAvg/theData.length;
		
		double covariance = 0;
		
		for(int i = 0 ; i < theData.length ; i++)
		{
			covariance += ((expected[i] - eAvg) * (predicted[i] - pAvg));
		}
		
		covariance = covariance/(theData.length - 1);
		
		double eStDev = calcStandardDeviation(expected, eAvg);
		double pStDev = calcStandardDeviation(predicted, pAvg);
		
		double correlation = covariance/ (eStDev * pStDev);
		
		correlation = correlation - (0.00001 * absError);	
		//return correlation;
		return  1/(Math.abs(correlation));
	}
	
	public static double dfferenceError(double expected, double[] variables, Chromosome toTest)
	{
		return expected - evaluateChromosome(toTest.numGenes - 1, variables, toTest);
	}
	
	public static double squaredError(double expected, double[] variables, Chromosome toTest)
	{
		//double predicted = evaluateChromosome(variables, toTest);
		//double predicted = evaluateChromosomeIterateAll(variables, toTest);
		double predicted = evaluateChromosome(toTest.numGenes - 1, variables, toTest);
		//return Math.abs(predicted - expected);
		return Math.pow(predicted - expected, 2);
	}
	
	public static double squaredErrorIterate(double expected, double[] variables, Chromosome toTest, double[] results, boolean[] isIn, Vector<Integer> toDo)
	{
		double predicted = evaluateChromosomeIterate(variables, toTest, results, isIn, toDo);
		//double predicted = evaluateChromosomeIterateAll(variables, toTest);
		//double predicted = evaluateChromosome(toTest.numGenes - 1, variables, toTest);
		//return Math.abs(predicted - expected);
		return Math.pow(predicted - expected, 2);
	}
	
	public static double evaluateChromosome(int index, double[] variables, Chromosome toTest)
	{
		switch(toTest.geneArray[index].type)
		{
			case 'c':
				return toTest.geneArray[index].constant;
			case 'v':
				return variables[toTest.geneArray[index].varNum];
			case 'u':
				switch (toTest.geneArray[index].operator) 
				{
					case "e": return Language.e(evaluateChromosome(toTest.geneArray[index].ref1, variables, toTest));
					case "ln": return Language.naturalLogarithm(evaluateChromosome(toTest.geneArray[index].ref1, variables, toTest));
					case "abs": return Language.abs(evaluateChromosome(toTest.geneArray[index].ref1, variables, toTest));
					case "R": return Language.round(evaluateChromosome(toTest.geneArray[index].ref1, variables, toTest));
					case "sin": return Language.sine(evaluateChromosome(toTest.geneArray[index].ref1, variables, toTest));
					case "cos": return Language.cosine(evaluateChromosome(toTest.geneArray[index].ref1, variables, toTest));
					case "tan": return Language.tangent(evaluateChromosome(toTest.geneArray[index].ref1, variables, toTest));
					case "pow": return Language.power(evaluateChromosome(toTest.geneArray[index].ref1, variables, toTest));
					case "N": return Language.N(evaluateChromosome(toTest.geneArray[index].ref1, variables, toTest));
					default: System.out.println("BUSTED: tried to evaluate a unary operator that did not exist: " + toTest.geneArray[index].operator);
						return Double.MAX_VALUE;
				}
			case 'b':
				switch (toTest.geneArray[index].operator) 
				{
					case "+": return Language.addition(evaluateChromosome(toTest.geneArray[index].ref1, variables, toTest), evaluateChromosome(toTest.geneArray[index].ref2, variables, toTest));
					case "-": return Language.subtraction(evaluateChromosome(toTest.geneArray[index].ref1, variables, toTest), evaluateChromosome(toTest.geneArray[index].ref2, variables, toTest));
					case "*": return Language.multiplication(evaluateChromosome(toTest.geneArray[index].ref1, variables, toTest), evaluateChromosome(toTest.geneArray[index].ref2, variables, toTest));
					case "/": return Language.division(evaluateChromosome(toTest.geneArray[index].ref1, variables, toTest), evaluateChromosome(toTest.geneArray[index].ref2, variables, toTest));
					case "max": return Language.maximum(evaluateChromosome(toTest.geneArray[index].ref1, variables, toTest), evaluateChromosome(toTest.geneArray[index].ref2, variables, toTest));
					case "min": return Language.minimum(evaluateChromosome(toTest.geneArray[index].ref1, variables, toTest), evaluateChromosome(toTest.geneArray[index].ref2, variables, toTest));
					default: System.out.println("BUSTED: tried to evaluate a binary operator that did not exist: " + toTest.geneArray[index].operator);
						return Double.MAX_VALUE;
				}
			default: System.out.println("BUSTED: GENE HAD NO PROPER TYPE: " + toTest.geneArray[index].type);
			return Double.MAX_VALUE;
		}
	}
	
	//not as fast as recursion...
	
	public static double evaluateChromosomeIterate(double[] variables, Chromosome toTest, double[] results, boolean[] isIn, Vector<Integer> toDo)
	{
		//double[] results = new double[toTest.geneArray.length];
		//boolean[] isIn = new boolean[toTest.geneArray.length];
		//Vector<Integer> toDo = new Vector<Integer>(toTest.geneArray.length);
		//Vector<Integer> toDo = new Vector<Integer>();

		toDo.add(toTest.numGenes - 1);
		
		//this loop marks which genes actually need to be evaluated
		//it also just adds the result for constants and variables
		int counter = 0;
		int i = 0;
		while(counter < toDo.size())
		{
			i = toDo.elementAt(counter);
			if(!isIn[i])
			{
				switch(toTest.geneArray[i].type)
				{
					case 'c':
						isIn[i] = true;
						//results[i] =  toTest.geneArray[i].constant;
						break;
					case 'v':
						isIn[i] = true;
						//results[i] = variables[toTest.geneArray[i].varNum];
						break;
					case 'u':
						isIn[i] = true;
						toDo.add(toTest.geneArray[i].ref1);
						break;
					case 'b':
						isIn[i] = true;
						toDo.add(toTest.geneArray[i].ref1);
						toDo.add(toTest.geneArray[i].ref2);
						break;
					default: 
						System.out.println("BUSTED: GENE HAD NO PROPER TYPE: " + toTest.geneArray[i].type);
						results[i] = Double.MAX_VALUE;
						break;
				}
			}
			counter++;
		}
		
		
		//this loop does the actual evaluation
		i = 0;
		while(!toDo.isEmpty())
		{
			i = toDo.remove(toDo.size() - 1);
			switch(toTest.geneArray[i].type)
			{
				case 'c':
					results[i] =  toTest.geneArray[i].constant;
					isIn[i] = false;						//resets the isIn array for next run
					break;
				case 'v':
					results[i] = variables[toTest.geneArray[i].varNum];
					isIn[i] = false;
					break;
				case 'u':
					switch (toTest.geneArray[i].operator) 
					{
						case "e": 
							results[i] = Language.e(results[toTest.geneArray[i].ref1]);
							isIn[i] = false;
							break;
						case "ln": 
							results[i] = Language.naturalLogarithm(results[toTest.geneArray[i].ref1]);
							isIn[i] = false;
							break;
						case "abs": 
							results[i] = Language.abs(results[toTest.geneArray[i].ref1]);
							isIn[i] = false;
							break;
						case "R": 
							results[i] = Language.round(results[toTest.geneArray[i].ref1]);
							isIn[i] = false;
							break;
						case "sin": 
							results[i] = Language.sine(results[toTest.geneArray[i].ref1]);
							isIn[i] = false;
							break;
						case "cos": 
							results[i] = Language.cosine(results[toTest.geneArray[i].ref1]);
							isIn[i] = false;
							break;
						case "tan": 
							results[i] = Language.tangent(results[toTest.geneArray[i].ref1]);
							isIn[i] = false;
							break;
						case "pow": 
							results[i] = Language.power(results[toTest.geneArray[i].ref1]);
							isIn[i] = false;
							break;
						case "N": 
							results[i] = Language.N(results[toTest.geneArray[i].ref1]);
							isIn[i] = false;
							break;
						default: 
							System.out.println("BUSTED: tried to evaluate a unary operator that did not exist: " + toTest.geneArray[i].operator);
							results[i] = Double.MAX_VALUE;
							isIn[i] = false;
							break;
					}
					break;
				case 'b':
					switch (toTest.geneArray[i].operator) 
					{
						case "+": 
							results[i] = Language.addition(results[toTest.geneArray[i].ref1], results[toTest.geneArray[i].ref2]);
							isIn[i] = false;
							break;
						case "-": 
							results[i] = Language.subtraction(results[toTest.geneArray[i].ref1], results[toTest.geneArray[i].ref2]);
							isIn[i] = false;
							break;
						case "*": 
							results[i] = Language.multiplication(results[toTest.geneArray[i].ref1], results[toTest.geneArray[i].ref2]);
							isIn[i] = false;
							break;
						case "/": 
							results[i] = Language.division(results[toTest.geneArray[i].ref1], results[toTest.geneArray[i].ref2]);
							isIn[i] = false;
							break;
						default: 
							System.out.println("BUSTED: tried to evaluate a binary operator that did not exist: " + toTest.geneArray[i].operator);
							results[i] = Double.MAX_VALUE;
							isIn[i] = false;
							break;
					}
					break;
				default: 
					System.out.println("BUSTED: GENE HAD NO PROPER TYPE: " + toTest.geneArray[i].type);
					results[i] = Double.MAX_VALUE;
					isIn[i] = false;
					break;
			}
		}
		
		return results[results.length - 1];
	}
	
	public static double evaluateChromosomeIterateAll(double[] variables, Chromosome toTest)
	{
		double[] results = new double[toTest.geneArray.length];
		
		for(int i = 0 ; i < results.length ; i++)
		{
			switch(toTest.geneArray[i].type)
			{
				case 'c':
					results[i] =  toTest.geneArray[i].constant;
					break;
				case 'v':
					results[i] = variables[toTest.geneArray[i].varNum];
					break;
				case 'u':
					switch (toTest.geneArray[i].operator) 
					{
						case "e": 
							results[i] = Language.e(results[toTest.geneArray[i].ref1]);
							break;
						case "ln": 
							results[i] = Language.naturalLogarithm(results[toTest.geneArray[i].ref1]);
							break;
						case "abs": 
							results[i] = Language.abs(results[toTest.geneArray[i].ref1]);
							break;
						case "R": 
							results[i] = Language.round(results[toTest.geneArray[i].ref1]);
							break;
						case "sin": 
							results[i] = Language.sine(results[toTest.geneArray[i].ref1]);
							break;
						case "cos": 
							results[i] = Language.cosine(results[toTest.geneArray[i].ref1]);
							break;
						case "tan": 
							results[i] = Language.tangent(results[toTest.geneArray[i].ref1]);
							break;
						case "pow": 
							results[i] = Language.power(results[toTest.geneArray[i].ref1]);
							break;
						case "N": 
							results[i] = Language.N(results[toTest.geneArray[i].ref1]);
							break;
						default: 
							System.out.println("BUSTED: tried to evaluate a unary operator that did not exist: " + toTest.geneArray[i].operator);
							results[i] = Double.MAX_VALUE;
							break;
					}
					break;
				case 'b':
					switch (toTest.geneArray[i].operator) 
					{
						case "+": 
							results[i] = Language.addition(results[toTest.geneArray[i].ref1], results[toTest.geneArray[i].ref2]);
							break;
						case "-": 
							results[i] = Language.subtraction(results[toTest.geneArray[i].ref1], results[toTest.geneArray[i].ref2]);
							break;
						case "*": 
							results[i] = Language.multiplication(results[toTest.geneArray[i].ref1], results[toTest.geneArray[i].ref2]);
							break;
						case "/": 
							results[i] = Language.division(results[toTest.geneArray[i].ref1], results[toTest.geneArray[i].ref2]);
							break;
						default: 
							System.out.println("BUSTED: tried to evaluate a binary operator that did not exist: " + toTest.geneArray[i].operator);
							results[i] = Double.MAX_VALUE;
							break;
					}
					break;
				default: 
					System.out.println("BUSTED: GENE HAD NO PROPER TYPE: " + toTest.geneArray[i].type);
					results[i] = Double.MAX_VALUE;
					break;
			}
		}
		return results[results.length - 1];
	}
	
}
