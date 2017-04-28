import java.io.Serializable;

public class Gene implements Serializable
{
	char type;		//can be u (unary operator), b (binary operator), v (variable), or c (constant)
	String operator;
	double constant;
	int varNum;
	int ref1;
	int ref2;
	
	public Gene()
	{
		type = '\0';
		operator = "";
		ref1 = -1;
		ref2 = -1;
	}
	
	public Gene(Gene inGene)
	{
		type = inGene.type;
		operator = inGene.operator;
		constant = inGene.constant;
		varNum = inGene.varNum;
		ref1 = inGene.ref1;
		ref2 = inGene.ref2;
	}
	
	
	
	//Constructor for constant gene
	public Gene(char inType, double inConstant)
	{
		type = inType;
		constant = inConstant;
		ref1 = -1;
		ref2 = -1;
	}
	
	//Constructor for variable gene
	public Gene(char inType, int inVarNum)
	{
		type = inType;
		varNum = inVarNum;
		ref1 = -1;
		ref2 = -1;
	}
	
	//Constructor for operator gene
	public Gene(char inType, String inOperator, int inRef1, int inRef2)
	{
		type = inType;
		operator = inOperator;
		ref1 = inRef1;
		ref2 = inRef2;
	}
	
	public void copyGene(Gene inGene)
	{
		type = inGene.type;		//can be u (unary operator), b (binary operator), v (variable), or c (constant)
		operator = inGene.operator;
		constant = inGene.constant;
		varNum = inGene.varNum;
		ref1 = inGene.ref1;
		ref2 = inGene.ref2;
	}
	
	public String toString()
	{
		String s = type + "; " + operator + "; " + constant + "; " + varNum + "; " + ref1 + "; " + ref2;
		return s;
	}
}
