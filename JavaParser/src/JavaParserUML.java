import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class JavaParserUML {

	MethodVisitor methods;
	VariableVisitor variables;
	ClassVisitor classOrInterface;
	List<String> classes = new ArrayList<String>();
	CompilationUnit parsedInput;

	HashMap<String, List<String>> methodListPerClass = new HashMap<String, List<String>>();
	HashMap<String, HashMap<String, String>> typeAndFieldListPerClass = new HashMap<String, HashMap<String, String>>();
	HashMap<String, List<String>> parameterListPerMethod = new HashMap<String, List<String>>();
	HashMap<String, String> classesOrInterfaces = new HashMap<String, String>();
	HashMap<String, List<String>> implementationList = new HashMap<String, List<String>>();
	HashMap<String, List<String>> extendingList = new HashMap<String, List<String>>();
	HashMap<String, String> connectionsBetweenClasses = new HashMap<String, String>();

	public static void main(String[] args) throws ParseException, IOException {
		JavaParserUML blarg = new JavaParserUML(args[0]);
		GraphDisplay asssss = new GraphDisplay();
	}

	public JavaParserUML(String argument) throws ParseException, IOException {

		/*
		 * C:\\Users\\James\\Desktop\\University\\3rd Year
		 * University\\Sotirios\\Boston Metro\\src
		 */

		// C:\\Users\\James\\Desktop\\University\\3rd Year
		// University\\Graphics\\Flight Simulator\\flight-sim\\CS309 Flight
		// Simulator\\src

		File file = new File(argument);

		examineDirectory(file);
		discoverRelationships(file);

	}

	public void examineDirectory(File folder) throws ParseException,
			IOException {
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				examineDirectory(file);
			} else {
				if (file.getName().endsWith(".java")) {

					parsedInput = JavaParser.parse(file);
					String fileName = file.getName().substring(0,
							file.getName().length() - 5);

					classes.add(fileName);

					methods = new MethodVisitor();
					variables = new VariableVisitor();
					classOrInterface = new ClassVisitor();

					classOrInterface.visit(parsedInput, null);

					if (classOrInterface.returnExtendList() != null) {
						extendingList.put(fileName,
								classOrInterface.returnExtendList());
					}

					if (classOrInterface.returnImplementation() != null) {
						implementationList.put(fileName,
								classOrInterface.returnImplementation());
					}

					if (classOrInterface.returnClassOrInterface() != null) {
						classesOrInterfaces.put(fileName,
								classOrInterface.returnClassOrInterface());
					}

					variables.visit(parsedInput, null);

					if (variables.returnFieldAndType() != null) {
						typeAndFieldListPerClass.put(fileName,
								variables.returnFieldAndType());
					}

					methods.visit(parsedInput, null);

					if (methods.returnMethodList() != null) {
						methodListPerClass.put(fileName,
								methods.returnMethodList());
					}

					if (methods.returnMethodParameters() != null) {
						parameterListPerMethod.putAll(methods
								.returnMethodParameters());
					}
				}
			}
		}
	}

	public void discoverRelationships(File folder) {

		List<String> classNames = new ArrayList<String>();
		Iterator<String> classNameLoader = typeAndFieldListPerClass.keySet()
				.iterator();

		while (classNameLoader.hasNext()) {
			classNames.add((String) classNameLoader.next());
		}

		Set<String> fieldTypes;

		for (int i = 0; i < typeAndFieldListPerClass.keySet().size(); i++) {

			fieldTypes = typeAndFieldListPerClass.get(classNames.get(i))
					.keySet();

			for (String className : classes) {
				for (String fieldType : fieldTypes) {
					if (fieldType.contains(className)) {
						System.out.println(className + " is used by "
								+ classNames.get(i));
					}
				}
			}
		}
		methodExplorer(folder);
	}

	public void methodExplorer(File folder) {

		methods = new MethodVisitor();

		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				methodExplorer(file);
			} else {
				if (file.getName().endsWith(".java")) {

					String fileName = file.getName().substring(0,
							file.getName().length() - 5);

					methods.visit(parsedInput, classes);

					List<String> results = methods.returnSearchResults();

					if (results != null) {
						System.out.println(fileName + " uses " + results);
					}
				}
			}
		}
	}
}