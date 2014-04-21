package IR.Tree;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import IR.ClassDescriptor;
import IR.Descriptor;
import IR.MethodDescriptor;
import IR.NameDescriptor;
import IR.Program;
import IR.SymbolTable;
import IR.SymbolTableDescriptor;
import IR.TypeDescriptor;
import IR.VarDescriptor;

/***
 * Implement the check function for each type of AST Node
 * 
 */
public class SemanticCheck
{
	private SymbolTable nameTable = new SymbolTable();
	private BuildAST program;
	
	public SemanticCheck(BuildAST program2)
	{
		program = program2;
	}
	
	public void run()
	{
		// get an array of the class names
		Object[] tempClassNames = program.program.getClasses().getNamesSet().toArray();
		String[] classNames = Arrays.copyOf(tempClassNames, tempClassNames.length, String[].class);
		SymbolTable classes = program.program.getClasses();
		
		// check for duplicate class names
		checkClassNames(classNames, classes);
		
		// step into each class to check fields and methods
		for(int i = 0; i < classNames.length; i++)
		{
			// get one of the class descriptors
			ClassDescriptor classDesc = (ClassDescriptor) classes.get(classNames[i]);
			// check fields for duplicates
			checkFieldNames(classDesc);
			
			// get all the names for the method descriptors
			SymbolTable methods = classDesc.getMethodDescriptorTable();
			Object[] tempMethodsNames = methods.getNamesSet().toArray();
			String[] methodNames = Arrays.copyOf(tempMethodsNames, tempMethodsNames.length, String[].class);
			
			// check method descriptor for duplicate names
			checkMethodNames(methods, methodNames, classDesc);
			
			// step into the body of the methods
			for(int j = 0; j < methodNames.length; j++)
			{
				// check the method body for duplicate vars
				checkVariableNames((MethodDescriptor)methods.get(methodNames[j]), classDesc);
			}
		}
	}
	
	public void checkClassNames(String[] classNames, SymbolTable classes)
	{
		// check class names for duplicates
		for(int i = 0; i < classNames.length; i++)
		{
			if(classes.getDescriptorsSet(classNames[i]).size() > 1)
			{
				throw new Error("[ERROR_01] '" + classNames[i] + "' identifier has duplicate.");
			}
			
			// create new symbol table inside of name table for classes
			nameTable.add(new SymbolTableDescriptor(classNames[i], nameTable));
		}
	}
	
	public void checkFieldNames(ClassDescriptor classDesc)
	{
		// get names of all fields
		SymbolTable fields = classDesc.getFieldDescriptorTable();
		Object[] tempFieldNames = fields.getNamesSet().toArray();
		String[] fieldNames = Arrays.copyOf(tempFieldNames, tempFieldNames.length, String[].class);
		
		// create a new symbol table for all fields
		SymbolTableDescriptor classSymbolTable = ((SymbolTableDescriptor)nameTable.get(classDesc.getSymbol()));
		classSymbolTable.addToSymbolTable(new SymbolTableDescriptor("Field", ((SymbolTableDescriptor)nameTable.get(classDesc.getSymbol())).getSymbolTable()));
		
		SymbolTable FieldSymbolTable = ((SymbolTableDescriptor)classSymbolTable.getSymbolTable().get("Field")).getSymbolTable();
		
		// check fields for duplicates
		for(int i = 0; i < fieldNames.length; i++)
		{
			if(fields.getDescriptorsSet(fieldNames[i]).size() > 1)
			{
				throw new Error("[ERROR_01] '" + fieldNames[i] + "' identifier has duplicate @'" + classDesc.getSymbol() + "'.");
			}
			
			// add field names to symbol table
			FieldSymbolTable.add(new NameDescriptor(fields.get(fieldNames[i]).getSymbol()));
		}
	}
	
	public void checkMethodNames(SymbolTable methods, String[] methodNames, ClassDescriptor classDesc)
	{
		// create a new symbol table for all methods
		SymbolTableDescriptor classSymbolTable = ((SymbolTableDescriptor)nameTable.get(classDesc.getSymbol()));
		
		// check methods for duplicates
		for(int i = 0; i < methodNames.length; i++)
		{
			if(methods.getDescriptorsSet(methodNames[i]).size() > 1)
			{
				throw new Error("[ERROR_01] '" + methodNames[i] + "' identifier has duplicate @'" + classDesc.getSymbol() + "'.");
			}
			
			// add method names to symbol table
			classSymbolTable.addToSymbolTable(new SymbolTableDescriptor(methodNames[i], classSymbolTable.getSymbolTable()));
		}
	}
	
	public void checkVariableNames(MethodDescriptor method, ClassDescriptor classDesc)
	{
		
		// create a new symbol table for all vars
		SymbolTableDescriptor classSymbolTable = ((SymbolTableDescriptor)nameTable.get(classDesc.getSymbol()));
		SymbolTableDescriptor methodSymbolTable = ((SymbolTableDescriptor)classSymbolTable.getSymbolTable().get(method.getSymbol()));
		
		methodSymbolTable.addToSymbolTable(new SymbolTableDescriptor("Vars", methodSymbolTable.getSymbolTable()));
		
		SymbolTable VarSymbolTable = ((SymbolTableDescriptor)methodSymbolTable.getSymbolTable().get("Vars")).getSymbolTable();
		
		// fill vector with block statement nodes
		Vector<BlockStatementNode> blockStatementVector = ((BlockNode)method.getASTTree()).getblockStatementVector();
		
		SymbolTable tempVarSymbolTable = new SymbolTable();
		
		for(int i = 0; i < blockStatementVector.size(); i++)
		{
			if(blockStatementVector.elementAt(i) instanceof DeclarationNode)
			{
				String name = ((DeclarationNode)blockStatementVector.elementAt(i)).getName();
				TypeDescriptor type = new TypeDescriptor(null, ((DeclarationNode)blockStatementVector.elementAt(i)).getType());
				ExpressionNode expression = ((DeclarationNode)blockStatementVector.elementAt(i)).getInitializer();
				
				// add vars to temp symbol table
				tempVarSymbolTable.add(new VarDescriptor(name, type, expression));
			}
		}
		
		// check vars for duplicates
		Object[] tempVarNames = tempVarSymbolTable.getNamesSet().toArray();
		String[] varNames = Arrays.copyOf(tempVarNames, tempVarNames.length, String[].class);
		
		for(int i = 0; i < varNames.length; i++)
		{
			if(tempVarSymbolTable.getDescriptorsSet(varNames[i]).size() > 1)
			{
				throw new Error("[ERROR_01] '" + varNames[i] + "' identifier has duplicate @'" + method.getSymbol() + "'.");
			}
			
			// add var names to symbol table
			VarSymbolTable.add(new NameDescriptor(varNames[i]));
		}
	}
}
