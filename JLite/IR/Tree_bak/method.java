package IR.Tree;

import java.util.Vector;

public class method
{
    String name;
    
	Return returnStatement;
	
    Vector<formal_parameter> formal_parameter_list = new Vector<formal_parameter>(1,1); // contains all of the method header components
	Vector<block_statement> block_statement_list = new Vector<block_statement>(1,1); // contains all of the block statements within the method
	
	// Constructor
	public method(String n, Return r)
	{
		name = n;
		returnStatement = r;
	}
	
	// adds a parameter into the formal_parameter_list
	public void addParameter(type t, String n)
	{
		formal_parameter tempParameter = new formal_parameter(t, n);
		formal_parameter_list.add(tempParameter);
	}
	
	// adds an if-statement to the block_statement_list
	public void addIfStatement(String condition, String identifier, int literal)
	{
		block_statement tempBlockStatement = new if_statement(condition, identifier, literal);
		block_statement_list.add(tempBlockStatement);
	}
	
	// adds a while-statement to the block_statement_list
	public void addWhileStatement(String condition, String identifier, int literal)
	{
		block_statement tempBlockStatement = new while_statement(condition, identifier, literal);
		block_statement_list.add(tempBlockStatement);
		
	}
	
	// adds a variable declaration to the block_statement_list
	public void addLocalVarDeclaration(type Type, String identifier, int literal)
	{
		block_statement tempBlockStatement = new local_variable_declaration(Type, identifier, literal);
		block_statement_list.add(tempBlockStatement);
	}
	
	// adds an assignment to the block_statement_list
	public void addAssignment(String identifier, expression Expression)
	{
		block_statement tempBlockStatement = new assignment(identifier, Expression);
		block_statement_list.add(tempBlockStatement);
	}
	
	// adds an add to the block_statement list
	public void addAdd(expression Expression)
	{
		block_statement tempBlockStatement = new Add(Expression);
		block_statement_list.add(tempBlockStatement);
	}
	
	// adds a multiply to the block_statement list
	public void addMultiply(expression Expression)
	{
		block_statement tempBlockStatement = new multiply(Expression);
		block_statement_list.add(tempBlockStatement);
	}
	
	// adds a subtract to the block_statement list
	public void addSubtract(expression Expression)
	{
		block_statement tempBlockStatement = new subtract(Expression);
		block_statement_list.add(tempBlockStatement);
	}
	
	// adds a bitwise_xor to the block_statement list
	public void addBitWiseXor(expression Expression)
	{
		block_statement tempBlockStatement = new bitwise_xor(Expression);
		block_statement_list.add(tempBlockStatement);
	}
	
	// adds a subtract to the block_statement list
	public void addbitwise_or(expression Expression)
	{
		block_statement tempBlockStatement = new bitwise_or(Expression);
		block_statement_list.add(tempBlockStatement);
	}
	
	// adds a create_object to the block_statement_list
	public void addcreateObject(type Type)
	{
		block_statement tempBlockStatement = new create_object(Type);
		block_statement_list.add(tempBlockStatement);
	}

	// adds a bitwise_and to the block_statement_list
	public void addbitwise_and(expression Expression)
	{
		block_statement tempBlockStatement = new bitwise_and(Expression);
		block_statement_list.add(tempBlockStatement);
	}
}