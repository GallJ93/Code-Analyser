import java.util.List;

import soot.Unit;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardBranchedFlowAnalysis;


public class Analysis extends ForwardBranchedFlowAnalysis{

	public Analysis(UnitGraph graph) {
		super(graph);
		doAnalysis();
	}

	@Override
	protected void flowThrough(Object arg0, Unit arg1, List arg2, List arg3) {
		
	}

	@Override
	protected void copy(Object arg0, Object arg1) {
		FlowSet srcSet = (FlowSet)arg0, destSet = (FlowSet)arg1;
		
		srcSet.copy(destSet);
		
	}

	@Override
	protected Object entryInitialFlow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void merge(Object arg0, Object arg1, Object arg2) {
		
		FlowSet inSet1 =(FlowSet)arg0, inSet2 = (FlowSet)arg1, inSet3 = (FlowSet)arg2;
		
		inSet1.intersection(inSet2, inSet3);
		
	}

	@Override
	protected Object newInitialFlow() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
