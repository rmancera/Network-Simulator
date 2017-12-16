import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import java.util.Collection;
import javax.swing.JFrame;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.ext.JGraphXAdapter;

public class GraphDisplay extends JFrame {
  
  private  UndirectedGraph<String, DefaultEdge> graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
  
  public void display(){
    JGraphXAdapter<String, DefaultEdge> jgxAdapter;

    jgxAdapter = new JGraphXAdapter <String, DefaultEdge>(graph);
    println(graph.toString());
    mxGraphComponent graphComponent = new mxGraphComponent(jgxAdapter);
    mxGraphModel graphModel = (mxGraphModel)graphComponent.getGraph().getModel(); 
    Collection<Object> cells =  graphModel.getCells().values();
    mxUtils.setCellStyles(graphComponent.getGraph().getModel(), 
    cells.toArray(), mxConstants.STYLE_ENDARROW, mxConstants.NONE);
    getContentPane().add(graphComponent);

    mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
    layout.execute(jgxAdapter.getDefaultParent());
    
    setTitle("Network Topology");
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    setVisible(true);    
    pack();
  }
  
  public void addVertex(String vertex){
    graph.addVertex(vertex);
    println("[Graph][addVertex] adding vertex: " + vertex);
  }
  public void addEdge(String vertex1, String vertex2){
    graph.addEdge(vertex1,vertex2);
    println("[Graph][addEdge] adding edge: " + vertex1 + ", " + vertex2);
  }
  public void removeVertex(String vertex){
    graph.removeVertex(vertex);
    println("[Graph][removeVertex] removing: " + vertex);
    println(graph.toString());
  }
}