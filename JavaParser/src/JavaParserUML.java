import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.*;
import japa.parser.ast.body.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class JavaParserUML{

	MethodVisitor methods;
	VariableVisitor variables;

	public static void main(String[] args) throws ParseException, IOException{
		JavaParserUML blarg = new JavaParserUML();
	}

	public JavaParserUML() throws ParseException, IOException {
		
		File file = new File("C:\\Users\\James\\Desktop\\University\\1st Year University\\CS 105 - Programming Foundations\\Eclipse\\CS308 - Gizmoball Prototypes\\src");
		
		examineDirectory(file);

	}

	public void examineDirectory(File folder) throws ParseException, IOException {
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				examineDirectory(file);
			} else {
				if(file.getName().endsWith(".java")){
					CompilationUnit parsedInput = JavaParser.parse(file);
					
					methods = new MethodVisitor();
					variables = new VariableVisitor();
					
					System.out.println();
					System.out.println();
					System.out.println(file.getName().substring(0, file.getName().length()-5));
					variables.visit(parsedInput, null);
					System.out.println();
					methods.visit(parsedInput, null);
				}
			}
		}
	}

}
