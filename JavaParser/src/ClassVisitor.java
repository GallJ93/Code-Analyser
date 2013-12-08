import japa.parser.ast.body.*;
import japa.parser.ast.visitor.*;

public class ClassVisitor extends VoidVisitorAdapter{
	
	public ClassVisitor(){
		
	}
	
	public void visit(ClassOrInterfaceDeclaration classOrInterface, Object object){
		
		if(classOrInterface.isInterface() == true){
			System.out.println("Interface");
		}
		else{
			System.out.println("Class");
		}
		
	}

}
