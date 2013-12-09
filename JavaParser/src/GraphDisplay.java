import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;


public class GraphDisplay {

	public GraphDisplay(){
		
		JFrame frame = new JFrame();
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setPreferredSize(new Dimension(720, 480));
		frame.setMinimumSize(new Dimension(720, 480));
		frame.setSize(new Dimension(720, 480));
		frame.setLocationRelativeTo(null);

		JGraph graph = new JGraph();
		
		graph.setEditable(false);
		graph.setEnabled(false);
		
		JScrollPane scrollPane = new JScrollPane(graph);
		
		frame.add(scrollPane);
		
		frame.pack();
		frame.setVisible(true);

	}



}
