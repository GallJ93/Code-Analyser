import java.util.List;

import japa.parser.ast.body.*;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.*;

public class ClassVisitor extends VoidVisitorAdapter{
	
	public ClassVisitor(){
		
	}
	
	public void visit(ClassOrInterfaceDeclaration classOrInterface, Object object){
		
		
		List<ClassOrInterfaceType> extendsList = classOrInterface.getExtends();
		List<ClassOrInterfaceType> implementsList = classOrInterface.getExtends();
		  
		
		if(classOrInterface.isInterface() == true){
			System.out.println("Interface");
		}
		else{
			System.out.println("Class");
		}
		
		for(ClassOrInterfaceType extendsName : extendsList){
			System.out.println("Extends " + extendsName.getName());
		}
		
		for(ClassOrInterfaceType implementsName : implementsList){
			System.out.println("Implements " + implementsName.getName());
		}
	}
}
