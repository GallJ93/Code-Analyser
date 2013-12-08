import java.util.ArrayList;
import java.util.List;

import japa.parser.ast.body.*;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.*;

public class ClassVisitor extends VoidVisitorAdapter{
	
	List<String> extendingList = new ArrayList<String>();
	List<String> implementationList = new ArrayList<String>();
	String cOrI;
	
	public ClassVisitor(){
		
	}
	
	public void visit(ClassOrInterfaceDeclaration classOrInterface, Object object){
		
		List<ClassOrInterfaceType> extendsList = classOrInterface.getExtends();
		List<ClassOrInterfaceType> implementsList = classOrInterface.getImplements();
		
		if(extendsList!=null){
			for(ClassOrInterfaceType extendsName : extendsList){
				extendingList.add(extendsName.getName());
			}
		}
		
		if(implementsList!=null){
			for(ClassOrInterfaceType implementsName : implementsList){
				implementationList.add(implementsName.getName());
			}
		}
		  
		if(classOrInterface.isInterface() == true){
			cOrI = "Interface";
		}
		else{
			cOrI = "Class";
		}
	}

	public List<String> returnExtendList() {
		
		return extendingList;
		
	}
	
	public List<String> returnImplementation(){
		
		return implementationList;
		
	}

	public String returnClassOrInterface() {
		
		return cOrI;
		
	}
}
