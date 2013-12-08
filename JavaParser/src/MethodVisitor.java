import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import japa.parser.ast.body.*;
import japa.parser.ast.visitor.*;


public class MethodVisitor extends VoidVisitorAdapter{
	
	List<String> methodNames = new ArrayList<String>(), parameterNames = new ArrayList<String>();
	HashMap<String, List<String>> methodParameters = new HashMap<String, List<String>>();
	
	public MethodVisitor(){
		
	}
	
	public void visit(MethodDeclaration method, Object object){
		
		List<Parameter> parameters;
		
		methodNames.add(method.getName());
		
		if(method.getParameters() != null){
			
			parameters = method.getParameters();
			
			for(Parameter parameter : parameters){
				parameterNames.add(parameter.toString());
			}
			
			methodParameters.put(method.getName(), parameterNames);
			
		}
		
	}
	
	public List<String> returnMethodList(){
		
		return methodNames;
		
	}
	
	public HashMap<String, List<String>> returnMethodParameters(){
		
		return methodParameters;
	}
	
}
