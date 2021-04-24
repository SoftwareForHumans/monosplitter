package com.uporto.monosplitter.service.decomposition;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uporto.monosplitter.model.Edge;
import com.uporto.monosplitter.model.Node;

@Service
public class DecompositionService implements IDecompositionService {
	private final Path projectRootLocation;

	@Autowired
	public DecompositionService() {
		this.projectRootLocation = Paths.get("").toAbsolutePath();
	}

	public Graph<Node, DefaultWeightedEdge> getMonolith() {
		ArrayList<Node> nodeComponents = getNodes();
		ArrayList<Edge> edges = setEdges(nodeComponents);

		Graph<Node, DefaultWeightedEdge> g = new DefaultUndirectedWeightedGraph<Node, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		for (Node n : nodeComponents) {
			g.addVertex(n);
		}
		
		for (Edge e : edges) {
			DefaultWeightedEdge newEdge = g.addEdge(e.getSourceNode(), e.getDestinationNode());
			g.setEdgeWeight(newEdge, e.getWeight());
		}

		return g;
	}

	public void getMonolithServiceCuts(Graph<Node, DefaultWeightedEdge> monolith) {
		System.out.println("HELLO WORLD GET MONOLITH SERVICE CUTS");
		// TO-DO
		// get clusters by executing clustering algorithm on the graph
		// return service cuts as serviceCutsDto
	}

	public ArrayList<Node> getNodes() {
		ArrayList<Node> nodeComponents = new ArrayList<Node>();
		String osOperation = System.getProperty("os.name").toLowerCase().startsWith("windows") ? "cmd.exe" : "sh";
		File executionDir = new File(projectRootLocation.toString().replace("'\'", "/").concat("/external-libs"));
		File[] files = new File(projectRootLocation.toString().replace("'\'", "/").concat("/upload-dir")).listFiles();
		File projectJAR = null;

		for (File f : files) {
			String[] temp = f.getName().split("\\.");
			String fileType = temp[temp.length - 1];

			if (fileType.equalsIgnoreCase("jar")) {
				projectJAR = f;
				break;
			}
		}

		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(osOperation, "/c", "jar tf ".concat(projectJAR.getPath().toString()));
			builder.directory(executionDir);

			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			int index = 1;
			while ((line = reader.readLine()) != null) {
				String[] jarContent = line.split("\\.");
				if (jarContent[jarContent.length - 1].equalsIgnoreCase("class") && !jarContent[0].contains("$")) {
					String completeName = jarContent[0].replace("/", ".");
					String[] pathSteps = completeName.split("\\.");
					String shortName = pathSteps[pathSteps.length - 1];
					Node n = new Node(index, shortName, completeName);
					nodeComponents.add(n);
					index++;
				}
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				process.destroy();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return nodeComponents;
	}

	public ArrayList<Edge> setEdges(ArrayList<Node> nodes) {
		ArrayList<Edge> edges = new ArrayList<Edge>();

		// STATIC ANALYSIS DATA
		List<ArrayList<String>> nodesRelation = getStaticAnalysisResult();
		ArrayList<String> sourceNodes = nodesRelation.get(0);
		ArrayList<String> destinationNodes = nodesRelation.get(1);

		int nrRelations = sourceNodes.size();

		for (int i = 0; i < nrRelations; i++) {
			if (!sourceNodes.get(i).equalsIgnoreCase(destinationNodes.get(i))) {
				Node destinationNode = getNodeByCompleteName(destinationNodes.get(i), nodes);
				Node sourceNode = destinationNode != null ? getNodeByCompleteName(sourceNodes.get(i), nodes) : null;

				if (sourceNode != null && destinationNode != null) {
					int temp = getExistingRelationIndex(sourceNode, destinationNode, edges);
					if (temp == -1) {
						edges.add(new Edge(sourceNode, destinationNode, 1));
					} else {
						edges.get(temp).setWeight(edges.get(temp).getWeight() + 1);
					}
				}
			}
		}

		return edges;
	}

	public Node getNodeByCompleteName(String nodeCompleteName, ArrayList<Node> nodes) {
		Node nodeToReturn = null;
		int nrNodes = nodes.size();

		for (int i = 0; i < nrNodes; i++) {
			if (nodes.get(i).getCompleteName().equalsIgnoreCase(nodeCompleteName)) {
				nodeToReturn = nodes.get(i);
				break;
			}
		}

		return nodeToReturn;
	}

	public int getExistingRelationIndex(Node sourceNode, Node destinationNode, ArrayList<Edge> edges) {
		int output = -1;

		for (int i = 0; i < edges.size(); i++) {
			if ((edges.get(i).getSourceNode().getId() == sourceNode.getId()
					&& edges.get(i).getDestinationNode().getId() == destinationNode.getId())
					|| (edges.get(i).getSourceNode().getId() == destinationNode.getId()
							&& edges.get(i).getDestinationNode().getId() == sourceNode.getId())) {
				output = i;
				break;
			}
		}

		return output;
	}

	public List<ArrayList<String>> getStaticAnalysisResult() {
		List<ArrayList<String>> sourceAndDestination = new ArrayList<ArrayList<String>>();
		ArrayList<String> sourceNodes = new ArrayList<String>();
		ArrayList<String> destinationNodes = new ArrayList<String>();

		Scanner s = null;
		try {
			File[] files = new File(projectRootLocation.toString().replace("'\'", "/").concat("/upload-dir"))
					.listFiles();
			for (File f : files) {
				if (f.getName().contains("_STATIC_ANALYSIS.txt")) {
					s = new Scanner(f);
					break;
				}
			}

			while (s.hasNext()) {
				String line = s.nextLine();
				String[] nodesInfo = line.split(" ");

				// SOURCE NODE
				String[] sourceTemp = nodesInfo[0].split(":");
				String source = sourceTemp[1];
				sourceNodes.add(source);

				// DESTINATION NODE
				String[] destinationTemp = nodesInfo[1].split(":");
				String destination = destinationTemp[0].substring(3);
				destinationNodes.add(destination);
			}
			s.close();

			sourceAndDestination.add(sourceNodes);
			sourceAndDestination.add(destinationNodes);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sourceAndDestination;
	}
}
