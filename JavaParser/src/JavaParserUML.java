import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class JavaParserUML{

	MethodVisitor methods;
	VariableVisitor variables;
	ClassVisitor classOrInterface;
	List<String> classes = new ArrayList<String>();
	
	HashMap<String, List<String>> methodListPerClass = new HashMap<String, List<String>>();
	HashMap<String, HashMap<String, String>> fieldListPerClass = new HashMap<String, HashMap<String, String>>();
	HashMap<String, List<String>> parameterListPerMethod = new HashMap<String, List<String>>();
	HashMap<String, String> classesOrInterfaces = new HashMap<String, String>();
	HashMap<String, List<String>> implementationList = new HashMap<String, List<String>>();
	HashMap<String, List<String>> extendingList = new HashMap<String, List<String>>();

	public static void main(String[] args) throws ParseException, IOException{
		JavaParserUML blarg = new JavaParserUML();
	}

	public JavaParserUML() throws ParseException, IOException {
		
		//C:\\Users\\James\\Desktop\\University\\3rd Year University\\Sotirios\\Boston Metro\\src
		
		File file = new File("C:\\Users\\James\\Desktop\\University\\3rd Year University\\Graphics\\Flight Simulator\\flight-sim\\CS309 Flight Simulator\\src");
		
		examineDirectory(file);

	}

	public void examineDirectory(File folder) throws ParseException, IOException {
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				examineDirectory(file);
			} else {
				if(file.getName().endsWith(".java")){
					
					CompilationUnit parsedInput = JavaParser.parse(file);
					String fileName = file.getName().substring(0, file.getName().length()-5);
					
					
					methods = new MethodVisitor();
					variables = new VariableVisitor();
					classOrInterface = new ClassVisitor();
					
					
					classOrInterface.visit(parsedInput, null);
					
					if(classOrInterface.returnExtendList() != null){
					extendingList.put(fileName, classOrInterface.returnExtendList());
					}
					
					if(classOrInterface.returnImplementation() != null){
					implementationList.put(fileName, classOrInterface.returnImplementation());
					}
					
					if(classOrInterface.returnClassOrInterface() != null){
					classesOrInterfaces.put(fileName, classOrInterface.returnClassOrInterface());
					}
					

					variables.visit(parsedInput, null);
					
					if(variables.returnFieldAndType() != null){
						fieldListPerClass.put(fileName, variables.returnFieldAndType());
					}

					
					methods.visit(parsedInput, null);
					
					if(methods.returnMethodList() != null){
						methodListPerClass.put(fileName, methods.returnMethodList());
					}
					
					if(methods.returnMethodParameters() != null){
						parameterListPerMethod.putAll(methods.returnMethodParameters());
					}
					
				}
			}
		}
	}

}
