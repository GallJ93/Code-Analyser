import java.util.List;

import japa.parser.ast.body.*;
import japa.parser.ast.expr.*;
import japa.parser.ast.visitor.*;


public class VariableVisitor extends VoidVisitorAdapter{
	
	public VariableVisitor(){
		
	}
	
	public void visit(FieldDeclaration fields, Object object){
		

		List<VariableDeclarator> variableList = fields.getVariables();
		System.out.println("Type " + fields.getType());
		for(VariableDeclarator variable : variableList){
			System.out.println("Name " + variable.getId().getName());
		}
		
	}

}
