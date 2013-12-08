import japa.parser.ast.body.*;
import japa.parser.ast.visitor.*;


public class MethodVisitor extends VoidVisitorAdapter{
	
	public MethodVisitor(){
		
	}
	
	public void visit(MethodDeclaration method, Object object){
		
		String parameters;
		
		System.out.print(method.getType() + " " + method.getName() + "(");
		
		if(method.getParameters() != null){
			
			parameters = method.getParameters().toString();
			
			System.out.println(parameters.substring(1, parameters.length()-1) + ")");
		}
		else{
			System.out.println(")");
		}
		
		System.out.println();
		
	}
	
}
