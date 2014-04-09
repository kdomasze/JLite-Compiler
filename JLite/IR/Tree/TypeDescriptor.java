package IR.Tree;

public class TypeDescriptor extends Descriptor
{
	private TypeNode Type;
	
	// Constructor
	public TypeDescriptor(TypeNode t)
	{
		Type = t;
	}

	// get method
	public TypeNode getType()
	{
		return Type;
	}
}
