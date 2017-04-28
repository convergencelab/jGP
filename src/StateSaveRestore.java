import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.ObjectOutputStream;


public class StateSaveRestore 
{
	public static void saveStateSer(String directory, int saveNumber, Chromosome bestOverAllMigrations, Chromosome[][] curPop, Chromosome[] trainers, double[][][] predictors)
	{
		try 
		{
			ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream(directory + saveNumber + "_ser.svst"));
			outFile.writeObject(bestOverAllMigrations);
			outFile.writeObject(curPop);
			outFile.writeObject(trainers);
			outFile.writeObject(predictors);
			outFile.close();
		} 
		catch (IOException ex) 
		{
		  System.out.println("BUSTED: Error writing save state!");
		}
	}
	
	

	public static void saveStateStr(String directory, int saveNumber, Chromosome bestOverAllMigrations, Chromosome[][] curPop, Chromosome[] trainers, double[][][] predictors)
	{
		try 
		{
			Writer outFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(directory + saveNumber + "_str.svst"), "utf-8"));
		    
			//writes best overall chromosome
			outFile.write(bestOverAllMigrations.toString());
			//writes the chromosomes
			outFile.write(curPop.length + ", " + curPop[0].length + "\n");
			for(int i = 0 ; i < curPop.length ; i++)
			{
				for(int j = 0 ; j < curPop[i].length ; j++)
				{
					outFile.write(curPop[i][j].toString());
				}
			}
			
			//writes the trainers
			outFile.write(trainers.length + "\n");
			for(int i = 0 ; i < trainers.length ; i++)
			{
				outFile.write(trainers[i].toString());
			}
			
			//writes the predictors
			outFile.write(predictors.length + ", " + predictors[0].length + ", " + predictors[0][0].length + "\n");
			for(int i = 0 ; i < predictors.length ; i++)
			{
				for(int j = 0 ; j < predictors[i].length ; j++)
				{
					for(int k = 0 ; k < predictors[i][j].length ; k++)
					{
						outFile.write("" + predictors[i][j][k]);
						if(k != predictors[i][j].length - 1)
						{
							outFile.write(", ");
						}
					}
					outFile.write("\n");
				}				
			}
			
		    outFile.close();
		} 
		catch (IOException ex) 
		{
		  System.out.println("BUSTED: Error writing save state!");
		}
	}

	
	
	public static void deleteStateSer(String directory, int saveNumber)
	{
		try
		{
			Files.deleteIfExists(Paths.get(directory + saveNumber + "_ser.svst"));
		}
		catch(IOException e)
		{
			System.out.println("Something bad happened when trying to delete the _segment.svst file");
		}
	}
	
	
	public static void restoreStateSer(String directory, int saveNumber, Chromosome bestOverAllMigrations, Chromosome[][] curPop, Chromosome[] trainers, double[][][] predictors)
	{
		try 
		{
			ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(directory + saveNumber + "_ser.svst"));
			
			//gets best overall chromosome 
			Chromosome bo = (Chromosome) inFile.readObject();
			bestOverAllMigrations.copyChromosome(bo);
			
			//gets the population
			Chromosome[][] pop = (Chromosome[][]) inFile.readObject();
			for(int i = 0 ; i < curPop.length ; i++)
			{
				for(int j = 0 ; j < curPop[i].length ; j++)
				{
					curPop[i][j] = pop[i][j];
				}
			}
			
			//gets the trainers
			Chromosome[] tra = (Chromosome[]) inFile.readObject();
			for(int i = 0 ; i < trainers.length ; i++)
			{
				trainers[i] = tra[i];
			}
			
			//gets the predictors
			double[][][] prd = (double[][][])inFile.readObject();
			for(int i = 0 ; i < predictors.length ; i++)
			{
				for(int j = 0 ; j < predictors[i].length ; j++)
				{
					for(int k = 0 ; k < predictors[i][j].length ; k++)
					{
						predictors[i][j][k] = prd[i][j][k];
					}
				}
			}
			
			inFile.close();
		} 
		catch (FileNotFoundException e)
		{
			System.out.println("BUSTED: Save state file not found!");
			System.exit(10);
		}
		catch (IOException ex) 
		{
			System.out.println("BUSTED: Error reading save state!");
		  	System.exit(10);
		}
		catch (ClassNotFoundException e) {
			System.out.println("Somehow the class was not found? This really shouldn't ever happen.");
			System.exit(10);
		}
	}
	
	
	/*
	 * 
	 * 
	public static void restoreState()
	{
		String[] splitString = {};
		try 
		{
			BufferedReader inFile = new BufferedReader(new FileReader(directory + variableNames));
			splitString = inFile.readLine().split(",");
			inFile.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Save state not found when trying to restore state");
			System.exit(0);
		}
		catch (IOException e)
		{
			System.out.println("Something bad happened when trying to restore state");
			System.exit(0);
		}
	}
	
	 *
	 *
	 */
}
