import java.util.List;

import japa.parser.ast.body.*;
import japa.parser.ast.expr.*;
import japa.parser.ast.visitor.*;


public class VariableVisitor extends VoidVisitorAdapter{
	
	public VariableVisitor(){
		
	}
	
	public void visit(VariableDeclarationExpr variables, Object object){

		List<VariableDeclarator> variableList = variables.getVars();
		
		for(VariableDeclarator variable : variableList){
			System.out.println(variable.getId().getName() + " " + variable.getId().getName());
		}
		
	}

}
