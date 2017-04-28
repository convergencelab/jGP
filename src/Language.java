//THIS CLASS NEEDS HARD CODING BASED ON LANGUAGE
public class Language 
{
	
	static String[] varSymbols = {};
	//static String[] varSymbols = {""};
	static String[] binaryOperatorSymbols = {"+", "-", "*", "/"};
	//static String[] binaryOperatorSymbols = {"+", "-", "*", "/", "max", "min"};
	//static String[] unaryOperatorSymbols = {};
	//static String[] unaryOperatorSymbols = {"N"};
	//static String[] unaryOperatorSymbols = {"pow"};
	//static String[] unaryOperatorSymbols = {"e", "ln", "round", "sin", "cos", "tan" };
	
	//static String[] unaryOperatorSymbols = {"e", "abs", "ln", "sin", "cos", "tan"};
	static String[] unaryOperatorSymbols = {"e", "abs", "sin", "cos", "tan"};
	
	//static String[] unaryOperatorSymbols = {"abs"};
	
	static int constantLimit = 20;	//will result in range from +/- this value
	
	public static void setVarSymbolsNumbers(int number)
	{
		varSymbols = new String[number];
		for(int i = 0 ; i < number ; i++)
		{
			varSymbols[i] = "v" + i;
		}
	}
	
	public static void setVarSymbols(String[] inVars)
	{
		varSymbols = new String[inVars.length];
		for(int i = 0 ; i < inVars.length ; i++)
		{
			varSymbols[i] = inVars[i];
		}
	}
	
	//****************
	//UNARY OPERATORS
	//****************	
	public static double e(double x)
	{
		return Math.exp(x);
	}
	
	public static double naturalLogarithm(double x)
	{
		return Math.log(x);
	}
	
	public static double abs(double x)
	{
		return Math.abs(x);
	}
	public static double sine(double x)
	{
		return Math.sin(x);
	}
	
	public static double cosine(double x)
	{
		return Math.cos(x);
	}
	
	public static double tangent(double x)
	{
		return Math.tan(x);
	}
	
	public static double round(double x)
	{
		return Math.round(x);
	}
	
	public static double power(double x)
	{
		return Math.pow(2, x);
	}
	
	public static double N(double x)
	{
		return (x-1);
	}
	
	//****************
	//BINARY OPERATORS
	//****************
	public static double addition(double x1, double x2)
	{
		return x1 + x2;
	}
	
	public static double subtraction(double x1, double x2)
	{
		return x1 - x2;
	}
	
	public static double multiplication(double x1, double x2)
	{
		return x1 * x2;
	}
	
	
	public static double division(double x1, double x2)
	{
		if(x2 != 0)
		{
			return x1 / x2;
		}
		else
		{
			if(x1 >= 0) 
			{
				return Double.MAX_VALUE;
			}
			else
			{
				return Double.MAX_VALUE * -1;
			}
		}
	}
	
	public static double binomialCoefficient(double x1, double x2)
	{
		if(x2 >= 0 && x2 < x1)
		{
			return (factorial(x1, x2)/(factorial(x1 - x2, 1)));
		}
		else
		{
			return 0;
		}
	}
	
	public static double maximum(double x1, double x2)
	{
		return Math.max(x1, x2);
	}

	public static double minimum(double x1, double x2)
	{
		return Math.min(x1, x2);
	}
	
	//OTHER STUFF
	
	public static double factorial(double x1, double x2)
	{
		double toRet = 1;
		
		for(double i = x1 ; i >= x2 ; i--)
		{
			toRet = toRet * i;
		}
		return toRet;
	}
}
