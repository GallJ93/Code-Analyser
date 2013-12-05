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

		SootClass sootClassTest = Scene.v().loadClassAndSupport("basicModel.java");
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
