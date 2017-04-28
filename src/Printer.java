import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Printer 
{
	public static void printLine(String directory, Chromosome toPrint)
	{
		try 
		{
			Writer outFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(directory + "_line.txt"), "utf-8"));
		    traversePrintLine(toPrint.numGenes - 1, toPrint, outFile);
		    outFile.close();
		} 
		catch (IOException ex) 
		{
		  System.out.println("BUSTED: Error writing printLine style");
		}
	}
	
	public static void traversePrintLine(int index, Chromosome toPrint, Writer outFile)
	{
		try
		{
			if(toPrint.geneArray[index].type == 'c')	//is it a constant?
			{
				String toWriteDouble = "" + toPrint.geneArray[index].constant;
				outFile.write(" " + toWriteDouble + " ");
				return;
			}
			else if(toPrint.geneArray[index].type == 'v')	//is it a variable?
			{
				outFile.write(" " + Language.varSymbols[toPrint.geneArray[index].varNum] + " ");
				return;
			}
			else if(toPrint.geneArray[index].type == 'u')						
			{
				outFile.write(toPrint.geneArray[index].operator + "(");
				traversePrintLine(toPrint.geneArray[index].ref1, toPrint, outFile);
				outFile.write(")");
				return;
			}
			else
			{
				if(toPrint.geneArray[index].operator.equals("min") || toPrint.geneArray[index].operator.equals("max"))
				{
					outFile.write(toPrint.geneArray[index].operator);					
					outFile.write("(");
					traversePrintLine(toPrint.geneArray[index].ref1, toPrint, outFile);
					outFile.write(", ");
					traversePrintLine(toPrint.geneArray[index].ref2, toPrint, outFile);
					outFile.write(") ");
				}
				else
				{
					outFile.write(" (");
					traversePrintLine(toPrint.geneArray[index].ref1, toPrint, outFile);
					outFile.write(toPrint.geneArray[index].operator);
					traversePrintLine(toPrint.geneArray[index].ref2, toPrint, outFile);
					outFile.write(") ");
				}
				return;
			}
		}
		catch (IOException ex) 
		{
		  System.out.println("BUSTED: Error writing printLine style");
		}
	}
	
	public static void printMulti(String directory, Chromosome toPrint)
	{
		int[] noRepeats = new int[toPrint.numGenes];
		try 
		{
			Writer outFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(directory + "_multi.txt"), "utf-8"));
			traversePrintMulti(toPrint.numGenes - 1, toPrint, outFile, noRepeats);
		    outFile.close();
		} 
		catch (IOException ex) 
		{
		  System.out.println("BUSTED: Error writing printMulti style");
		}
	}
	
	public static void traversePrintMulti(int index, Chromosome toPrint, Writer outFile, int[] noRepeats)
	{
		try
		{
			if(toPrint.geneArray[index].type == 'c')	//is it a constant?
			{
				if(noRepeats[index] != 1)
				{
					noRepeats[index] = 1;
					String toWriteDouble = "" + toPrint.geneArray[index].constant;
					outFile.write("(" + index + ")\t" + toWriteDouble + "\n");
				}
				return;
			}
			else if(toPrint.geneArray[index].type == 'v')	//is it a variable?
			{
				if(noRepeats[index] != 1)
				{
					noRepeats[index] = 1;
					outFile.write("(" + index + ")\t" + Language.varSymbols[toPrint.geneArray[index].varNum] + "\n");
				}
				return;
			}
			else if(toPrint.geneArray[index].type == 'u')						
			{
				if(noRepeats[index] != 1)
				{
					noRepeats[index] = 1;
					traversePrintMulti(toPrint.geneArray[index].ref1, toPrint, outFile, noRepeats);
					outFile.write("(" + index + ")\t" + toPrint.geneArray[index].operator + "(" + toPrint.geneArray[index].ref1 + ")\n");
				}
				return;
			}
			else
			{
				if(noRepeats[index] != 1)
				{
					noRepeats[index] = 1;
					if(toPrint.geneArray[index].ref1 < toPrint.geneArray[index].ref2)	//to order it nicely
					{
						traversePrintMulti(toPrint.geneArray[index].ref1, toPrint, outFile, noRepeats);
						traversePrintMulti(toPrint.geneArray[index].ref2, toPrint, outFile, noRepeats);
					}
					else
					{
						traversePrintMulti(toPrint.geneArray[index].ref2, toPrint, outFile, noRepeats);
						traversePrintMulti(toPrint.geneArray[index].ref1, toPrint, outFile, noRepeats);
					}
					outFile.write("(" + index + ")\t" + toPrint.geneArray[index].operator + " (" + toPrint.geneArray[index].ref1 + ")" + "(" + toPrint.geneArray[index].ref2 + ")\n");
				}
				return;
			}
		}
		catch (IOException ex) 
		{
		  System.out.println("BUSTED: Error writing printLine style");
		}
	}
	
	public static void printCurStat(String directory, Chromosome toPrint)
	{
		try 
		{
			Writer outFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(directory + "_fit.txt"), "utf-8"));
			outFile.write(String.valueOf(toPrint.fitnessValue));
		    outFile.close();
		} 
		catch (IOException ex) 
		{
		  System.out.println("BUSTED: Error writing printLine style");
		}
	}
	
	public static void printStats(String directory, double[][] stats)
	{
		try 
		{
			Writer outFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(directory + "stats.csv"), "utf-8"));
			outFile.write("Best,Avg,StDev\n");
			for(int i = 0 ; i < stats.length; i++)
			{
				for(int j = 0 ; j < stats[i].length ; j++)
				{
					outFile.write(String.valueOf(stats[i][j]));
					if(j != stats[i].length - 1) outFile.write(",");
				}
				outFile.write("\n");
			}
		    outFile.close();
		} 
		catch (IOException ex) 
		{
		  System.out.println("BUSTED: Error writing stats file");
		}
	}
}
