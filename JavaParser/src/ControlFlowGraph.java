import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.*;


public class ControlFlowGraph {
	
	public static void main(String[] args) {
		ControlFlowGraph CFG = new ControlFlowGraph();

	}

	public ControlFlowGraph(){
		
		System.out.println( System.getenv("CLASSPATH"));
		
		System.out.println(Scene.v().getSootClassPath());
		Scene.v().setSootClassPath(".;" + Scene.v().getSootClassPath());
		System.out.println(Scene.v().getSootClassPath());
		
		String dirPath = null;
		
		File dirs = new File(".");
		try {
			dirPath = dirs.getCanonicalPath() + File.separator+"src"+File.separator + "basicModel.java";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SootClass sootClassTest = Scene.v().loadClassAndSupport(dirPath);
		sootClassTest.setApplicationClass();

		Scene.v().addClass(sootClassTest);
		
		int loopValue = sootClassTest.getMethodCount();
		List<SootMethod> classMethodsList = sootClassTest.getMethods();

		for(int i = 0 ; i < loopValue ; i++){
			SootMethod sootMethodTest = classMethodsList.get(i);
			Body sootMethodBodyTest = sootMethodTest.getActiveBody();

			UnitGraph graphTest = new ExceptionalUnitGraph(sootMethodBodyTest);
			
			Iterator<Unit> iterates = graphTest.iterator();
			while(iterates.hasNext()){
				Unit sootUnitTest = (Unit)iterates.next();
				System.out.println(sootUnitTest.toString());
			}
		}

	}

}
