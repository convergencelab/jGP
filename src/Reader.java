import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Reader 
{	
	
	public static String[] readVarNames(String directory)
	{
		return readVarNames(directory, "variableNames.csv");
	}
	
	//Use to read a file with variable names 
	//Not required, only if you want to be fancy
	public static String[] readVarNames(String directory, String variableNames)
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
			System.out.println("Var Name File Not Found");
			System.exit(0);
		}
		catch (IOException e)
		{
			System.out.println("File In Wrong Format");
			System.exit(0);
		}
		return splitString;
	}
	
	public static double[][] readData(String directory)
	{
		return readData(directory, "dataFile.csv");
	}
	
	public static double[][] readData(String directory, String fileName)
	{
		int numberOfLines = 0;
		String inLine;
		String[] splitString;
		double[][] theData;
		
		try 
		{
			BufferedReader inFile = new BufferedReader(new FileReader(directory + fileName));
			while((inLine = inFile.readLine()) != null)
			{
				numberOfLines++;
			}
			inFile.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Data File Not Found");
			System.exit(0);
		}
		catch (IOException e)
		{
			System.out.println("File In Wrong Format");
			System.exit(0);
		}
		
		theData = new double[numberOfLines][];
		
		try 
		{
			BufferedReader inFile = new BufferedReader(new FileReader(directory + fileName));
			for(int i = 0 ; i < numberOfLines ; i++)
			{
				splitString = inFile.readLine().split(",");
				theData[i] = new double[splitString.length];
				for(int j = 0 ; j < splitString.length ; j++)
				{
					theData[i][j] = Double.parseDouble(splitString[j]);
				}
			}
			inFile.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File Not Found");
		}
		catch (IOException e)
		{
			System.out.println("File In Wrong Format");
		}
		
		
		return theData;
	}
	
}
