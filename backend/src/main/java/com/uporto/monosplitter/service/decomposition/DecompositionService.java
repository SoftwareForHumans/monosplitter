package com.uporto.monosplitter.service.decomposition;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uporto.monosplitter.consts.ClusteringAlgorithmsConsts;
import com.uporto.monosplitter.model.Commit;
import com.uporto.monosplitter.model.Edge;
import com.uporto.monosplitter.model.GitLog;
import com.uporto.monosplitter.model.Node;
import com.uporto.monosplitter.model.SystemModel;
import com.uporto.monosplitter.service.clustering.algorithm.GirvanNewman;
import com.uporto.monosplitter.service.clustering.algorithm.HierarchicalClustering;
import com.uporto.monosplitter.service.clustering.algorithm.IGirvanNewman;
import com.uporto.monosplitter.service.clustering.algorithm.IHierarchicalClustering;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

@Service
public class DecompositionService implements IDecompositionService {
	// SYSTEM MODEL EXTRACTION
	private ArrayList<Node> nodes;
	private ArrayList<Edge> nodesRelations;
	private ArrayList<String> insertedEdges;
	private int staticAnalysisEdgesSize;
	private double maxStaticAnalysisValue;
	private double staticAnalysisSum;

	// REPOSITORY ANALYSIS
	private ArrayList<Edge> repositoryAnalysisRelations;
	private HashMap<String, double[]> nodesChangeOcurrences;

	// OTHERS
	private final Path projectRootLocation;

	@Autowired
	public DecompositionService() {
		this.nodes = new ArrayList<Node>();
		this.nodesRelations = new ArrayList<Edge>();
		this.insertedEdges = new ArrayList<String>();
		this.staticAnalysisEdgesSize = 0;
		this.maxStaticAnalysisValue = 0.0;
		this.staticAnalysisSum = 0.0;

		this.repositoryAnalysisRelations = null;
		this.nodesChangeOcurrences = null;

		this.projectRootLocation = Paths.get("").toAbsolutePath();
	}

	public void clearVars() {
		this.nodes = new ArrayList<Node>();
		this.nodesRelations = new ArrayList<Edge>();
		this.insertedEdges = new ArrayList<String>();
		this.staticAnalysisEdgesSize = 0;
		this.maxStaticAnalysisValue = 0.0;
		this.staticAnalysisSum = 0.0;

		this.repositoryAnalysisRelations = null;
		this.nodesChangeOcurrences = null;
	}

	public SystemModel getSystemModel(boolean analyseRepository, String gitRepoCheckoutPath, String repoStartDate,
			String repoEndDate, boolean useSimalarity, boolean className, boolean packageName) {

		SystemModel sm = new SystemModel();

		clearVars();
		addNodesToSystemModel();

		if (analyseRepository) {
			this.repositoryAnalysisRelations = new ArrayList<Edge>();
			this.nodesChangeOcurrences = new HashMap<String, double[]>();

			GitLog log = getGitRepositoryLog(gitRepoCheckoutPath, repoStartDate, repoEndDate);
			ArrayList<Node> nodesInCommits = getAllNodesInCommits(log);
			setNodesChangeOcurrences(log.getCommits(), nodesInCommits);

			setEdges();
			this.staticAnalysisEdgesSize = this.nodesRelations.size();

			setAdditionalEdges(nodesInCommits);
			this.nodesRelations.addAll(this.repositoryAnalysisRelations);
		} else {
			setEdges();
			this.staticAnalysisEdgesSize = this.nodesRelations.size();
		}

		sm.setNodes(nodes);

		double staticAnalysisAvarage = this.staticAnalysisSum / this.staticAnalysisEdgesSize;
		for (Edge e : this.nodesRelations) {

			if (e.isRepositoryRelation() && !e.isStaticAnalysisRelation()) {
				if (e.getRepositoryUpdateCorrelation() >= 0.0) {
					double finalWeigth = calculateEdgeWeight(e, staticAnalysisAvarage, useSimalarity, className,
							packageName);

					e.setWeight(finalWeigth);
					sm.getLinks().add(e);
				}
			} else {
				if ((e.getSourceNode().isInterface() || e.getDestinationNode().isInterface())
						&& e.getNrInterfaceCalls() == 0) {
					e.setTotalStaticAnalysisConnections(e.getTotalStaticAnalysisConnections() + 1);
					e.setNrInterfaceCalls(e.getNrInterfaceCalls() + 1);
				}

				double finalWeigth = calculateEdgeWeight(e, staticAnalysisAvarage, useSimalarity, className,
						packageName);

				e.setWeight(finalWeigth);
				sm.getLinks().add(e);

			}
		}

		return sm;
	}

	public double calculateEdgeWeight(Edge e, double staticAnalysisAverage, boolean useNameSimilarity,
			boolean className, boolean packageName) {
		NormalizedLevenshtein l = new NormalizedLevenshtein();
		boolean useStaticAnalysisValue = e.isStaticAnalysisRelation() && e.getTotalStaticAnalysisConnections() > 0;
		boolean useRepositoryCorrelationValue = e.isRepositoryRelation() && e.getRepositoryUpdateCorrelation() > 0;

		String[] stringsToCompare = new String[2];
		stringsToCompare[0] = className && packageName ? e.getSourceNode().getCompleteName()
				: className ? e.getSourceNode().getShortName()
						: packageName
								? e.getSourceNode().getCompleteName()
										.substring(
												0,
												e.getSourceNode().getCompleteName().length()
														- e.getSourceNode().getShortName().length())
								: "";

		stringsToCompare[1] = className && packageName ? e.getDestinationNode().getCompleteName()
				: className ? e.getDestinationNode().getShortName()
						: packageName
								? e.getDestinationNode().getCompleteName()
										.substring(
												0,
												e.getDestinationNode().getCompleteName().length()
														- e.getDestinationNode().getShortName().length())
								: "";

		double staticAnalysisValue; 
		double total = (e.getTotalStaticAnalysisConnections()
				- (e.getNrInstantiations() + e.getNrInterfaceCalls() + e.getNrMethodCalls()))
				+ (e.getNrInterfaceCalls() + e.getNrMethodCalls()
						+ (e.getNrInstantiations() * staticAnalysisAverage));
		if (((int) staticAnalysisAverage * 3) <= total) {
			staticAnalysisValue = 1.0;
		} else {
			staticAnalysisValue = total / ((int) staticAnalysisAverage * 3);
		}

		double staticAnalysisFinalValue = useStaticAnalysisValue ? staticAnalysisValue : 0.0;
		double repositoryCorrelationFinalValue = useRepositoryCorrelationValue ? e.getRepositoryUpdateCorrelation() : 0.0;
		double nameSimilarityFinalValue = useStaticAnalysisValue ? (1 - l.distance(stringsToCompare[0], stringsToCompare[1])) : 0.0;

		return (staticAnalysisFinalValue * 4) + (repositoryCorrelationFinalValue * 2) + (nameSimilarityFinalValue * 6);
	}

	public ArrayList<ArrayList<Node>> getMonolithServiceCuts(SystemModel systemModel, String clusteringAlgorithm,
			int nrClusters, boolean removeNoDependenciesClasses) {
		ArrayList<ArrayList<Node>> serviceCutsList = new ArrayList<ArrayList<Node>>();

		switch (clusteringAlgorithm) {
		case ClusteringAlgorithmsConsts.GIRVAN_NEWMAN_ALGORITHM:
			IGirvanNewman gn = new GirvanNewman();
			serviceCutsList = gn.executeClustering(systemModel.getNodes(), systemModel.getLinks(), nrClusters,
					removeNoDependenciesClasses);
			break;

		case ClusteringAlgorithmsConsts.HIERARCHCAL_CLUSTERING_ALGORITHM:
			IHierarchicalClustering hc = new HierarchicalClustering();
			serviceCutsList = hc.executeClustering(systemModel.getNodes(), systemModel.getLinks(), nrClusters,
					removeNoDependenciesClasses);
			break;

		default:
			break;
		}

		return serviceCutsList;
	}

	private GitLog getGitRepositoryLog(String gitRepoCheckoutPath, String repoStartDate, String repoEndDate) {
		// get log result
		String osOperation = System.getProperty("os.name").toLowerCase().startsWith("windows") ? "cmd.exe" : "sh";
		File executionDir = new File(gitRepoCheckoutPath);
		GitLog log = new GitLog(new ArrayList<Commit>());

		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(osOperation, "/c", "git log --name-only --since=".concat('"' + repoStartDate + '"')
					.concat(" --until=").concat('"' + repoEndDate + '"').concat(" --pretty=format:[%h,%an,%ci,%s]"));
			builder.directory(executionDir);

			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			boolean startNewCommit = true;

			while ((line = reader.readLine()) != null) {
				if (startNewCommit) {
					log.getCommits().add(new Commit());
					startNewCommit = false;
				}

				if (!line.isEmpty()) {
					if (line.substring(0, 1).equalsIgnoreCase("[")
							&& line.substring(line.length() - 1).equalsIgnoreCase("]")) {
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
						String[] content = line.substring(1, line.length() - 1).split(",");

						log.getCommits().get(log.getCommits().size() - 1).setHash(content[0]);
						log.getCommits().get(log.getCommits().size() - 1).setAuthor(content[1]);
						log.getCommits().get(log.getCommits().size() - 1).setDate(dateFormat.parse(content[2]));
						log.getCommits().get(log.getCommits().size() - 1).setMessage(content[3]);
						log.getCommits().get(log.getCommits().size() - 1).setModifiedFiles(new ArrayList<Node>());

					} else {
						if (line.substring(line.length() - 5).equalsIgnoreCase(".java")) {
							Node n = getNodeByCompleteName(line.split("\\.")[0], true);
							if (n != null) {
								log.getCommits().get(log.getCommits().size() - 1).getModifiedFiles().add(n);
							}
						}
					}
				} else {
					startNewCommit = true;
					if (log.getCommits().get(log.getCommits().size() - 1).getModifiedFiles().isEmpty()
							|| log.getCommits().get(log.getCommits().size() - 1).getModifiedFiles() == null) {
						log.getCommits().remove(log.getCommits().size() - 1);
					}
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
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return log;
	}

	private void setNodesChangeOcurrences(ArrayList<Commit> logCommits, ArrayList<Node> nodesInCommits) {

		for (Node file : nodesInCommits) {
			this.nodesChangeOcurrences.put(file.getCompleteName(), new double[logCommits.size()]);
		}

		for (int i = 0; i < logCommits.size(); i++) {
			for (Node mofifiedFile : logCommits.get(i).getModifiedFiles()) {
				this.nodesChangeOcurrences.get(mofifiedFile.getCompleteName())[i] = 1.0;
			}
		}
	}

	private ArrayList<Node> getAllNodesInCommits(GitLog log) {
		ArrayList<Node> nodesInCommits = new ArrayList<Node>();

		for (int i = 0; i < log.getCommits().size(); i++) {
			ArrayList<Node> commitNodes = log.getCommits().get(i).getModifiedFiles();

			for (Node n : commitNodes) {
				if (!nodesInCommits.contains(n)) {

					for (Node node : this.nodes) {
						if (n.getId() == node.getId()) {
							nodesInCommits.add(node);
							break;
						}
					}

				}
			}
		}

		return nodesInCommits;
	}

	private void addNodesToSystemModel() {
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
					this.nodes.add(new Node(index, shortName, completeName));
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
	}

	private void setEdges() {
		boolean analysingRepository = this.nodesChangeOcurrences != null ? true : false;
		PearsonsCorrelation p = new PearsonsCorrelation();

		// STATIC ANALYSIS DATA
		List<ArrayList<String>> nodesRelation = getStaticAnalysisResult();
		ArrayList<String> sourceNodes = nodesRelation.get(0);
		System.out.println(sourceNodes.size());
		ArrayList<String> destinationNodes = nodesRelation.get(1);
		System.out.println(destinationNodes.size());
		ArrayList<String> connectionType = nodesRelation.get(2);
		System.out.println(connectionType.size());
		Node sourceNode = null;
		Node destinationNode = null;

		int nrRelations = sourceNodes.size();

		for (int i = 0; i < nrRelations; i++) {
			if (!sourceNodes.get(i).equalsIgnoreCase(destinationNodes.get(i))) {

				// GET DESTINATION NODE
				if (destinationNode == null
						|| !destinationNode.getCompleteName().equalsIgnoreCase(destinationNodes.get(i))) {
					destinationNode = getNodeByCompleteName(destinationNodes.get(i), false);
				}

				// GET SOURCE NODE
				if (destinationNode != null) {
					if (sourceNode == null || !sourceNode.getCompleteName().equalsIgnoreCase(sourceNodes.get(i))) {
						sourceNode = getNodeByCompleteName(sourceNodes.get(i), false);
					}
				}

				if (sourceNode != null && destinationNode != null) {
					int temp = getExistingRelationIndex(sourceNode, destinationNode);

					// CREATE RELATION BETWEEN NODES IF THAT CONNECTION WASN'T DEFINED YET
					if (temp == -1) {
						sourceNode.setHasDependency(true);
						destinationNode.setHasDependency(true);

						Edge e = new Edge(sourceNode, destinationNode);

						e.setStaticAnalysisRelation(true);
						e.setTotalStaticAnalysisConnections(e.getTotalStaticAnalysisConnections() + 1);
						this.staticAnalysisSum++;

						if (!connectionType.get(i).isEmpty()) {

							switch (connectionType.get(i)) {
							case "(I)":
								e.setNrInterfaceCalls(e.getNrInterfaceCalls() + 1);
								destinationNode.setInterface(true);
								break;
							case "(O)":
								e.setNrInstantiations(e.getNrInstantiations() + 1);
								break;
							case "(M)":
							case "(S)":
								e.setNrMethodCalls(e.getNrMethodCalls() + 1);
								break;
							default:
								break;
							}

						}

						if (analysingRepository) {
							double[] sourceNodeOcurrences = this.nodesChangeOcurrences
									.get(sourceNode.getCompleteName());
							double[] destinationNodeOcurrences = this.nodesChangeOcurrences
									.get(destinationNode.getCompleteName());

							if (sourceNodeOcurrences != null && destinationNodeOcurrences != null) {
								this.insertedEdges.add(sourceNode.getId() + "-" + destinationNode.getId());
								e.setRepositoryRelation(true);
								e.setRepositoryUpdateCorrelation(
										p.correlation(sourceNodeOcurrences, destinationNodeOcurrences));
							}
						}

						if (this.maxStaticAnalysisValue < e.getTotalStaticAnalysisConnections()) {
							this.maxStaticAnalysisValue = e.getTotalStaticAnalysisConnections();
						}

						this.nodesRelations.add(e);
					} else {
						this.nodesRelations.get(temp).setTotalStaticAnalysisConnections(
								this.nodesRelations.get(temp).getTotalStaticAnalysisConnections() + 1);

						if (!connectionType.get(i).isEmpty()) {
							switch (connectionType.get(i)) {
							case "(I)":
								this.nodesRelations.get(temp)
										.setNrInterfaceCalls(this.nodesRelations.get(temp).getNrInterfaceCalls() + 1);
								destinationNode.setInterface(true);
								break;
							case "(O)":
								this.nodesRelations.get(temp)
										.setNrInstantiations(this.nodesRelations.get(temp).getNrInstantiations() + 1);
								break;
							case "(M)":
							case "(S)":
								this.nodesRelations.get(temp)
										.setNrMethodCalls(this.nodesRelations.get(temp).getNrMethodCalls() + 1);
								break;
							default:
								break;
							}
						}

						this.staticAnalysisSum++;
						if (this.maxStaticAnalysisValue < this.nodesRelations.get(temp)
								.getTotalStaticAnalysisConnections()) {
							this.maxStaticAnalysisValue = this.nodesRelations.get(temp)
									.getTotalStaticAnalysisConnections();
						}
					}

				}
			} else {
				if (destinationNode == null
						|| !destinationNode.getCompleteName().equalsIgnoreCase(destinationNodes.get(i))) {
					destinationNode = getNodeByCompleteName(destinationNodes.get(i), false);
				}

				if (!connectionType.get(i).isEmpty() && connectionType.get(i).equals("(I)")) {
					destinationNode.setInterface(true);
				}
			}
		}
	}

	private void setAdditionalEdges(ArrayList<Node> nodesInCommits) {
		PearsonsCorrelation p = new PearsonsCorrelation();

		for (int i = 0; i < nodesInCommits.size(); i++) {
			Node firstNode = nodesInCommits.get(i);

			for (int j = i + 1; j < nodesInCommits.size(); j++) {
				Node secondNode = nodesInCommits.get(j);

				String firstVerificationString = firstNode.getId() + "-" + secondNode.getId();
				int existingFirstEdgeIndex = insertedEdges.indexOf(firstVerificationString);
				String secondVerificationString = secondNode.getId() + "-" + firstNode.getId();
				int existingSecondEdgeIndex = insertedEdges.indexOf(secondVerificationString);

				if (existingFirstEdgeIndex == -1 && existingSecondEdgeIndex == -1) {
					firstNode.setHasDependency(true);
					secondNode.setHasDependency(true);
					Edge newEdge = new Edge(firstNode, secondNode);
					newEdge.setRepositoryRelation(true);
					newEdge.setRepositoryUpdateCorrelation(
							p.correlation(this.nodesChangeOcurrences.get(firstNode.getCompleteName()),
									this.nodesChangeOcurrences.get(secondNode.getCompleteName())));
					this.repositoryAnalysisRelations.add(newEdge);
				} else {
					if (existingFirstEdgeIndex != -1) {
						insertedEdges.remove(existingFirstEdgeIndex);
					}

					if (existingSecondEdgeIndex != -1) {
						insertedEdges.remove(existingSecondEdgeIndex);
					}
				}
			}
		}
	}

	private Node getNodeByCompleteName(String nodeCompleteName, boolean fromRepo) {
		Node nodeToReturn = null;

		for (Node node : this.nodes) {
			String nodeCompleteNameTest = "";

			if (fromRepo) {
				if (nodeCompleteName.length() - node.getCompleteName().length() > 0) {
					nodeCompleteNameTest = nodeCompleteName.replace("/", ".")
							.substring(nodeCompleteName.length() - node.getCompleteName().length());
				}
			} else {
				nodeCompleteNameTest = nodeCompleteName;
			}

			if (node.getCompleteName().equalsIgnoreCase(nodeCompleteNameTest)) {
				nodeToReturn = node;
				break;
			}
		}
		return nodeToReturn;
	}

	private int getExistingRelationIndex(Node sourceNode, Node destinationNode) {
		int output = -1;

		for (int i = 0; i < this.nodesRelations.size(); i++) {
			if ((this.nodesRelations.get(i).getSourceNode().getId() == sourceNode.getId()
					&& this.nodesRelations.get(i).getDestinationNode().getId() == destinationNode.getId())
					|| (this.nodesRelations.get(i).getSourceNode().getId() == destinationNode.getId()
							&& this.nodesRelations.get(i).getDestinationNode().getId() == sourceNode.getId())) {
				output = i;
				break;
			}
		}

		return output;
	}

	private List<ArrayList<String>> getStaticAnalysisResult() {
		List<ArrayList<String>> sourceAndDestination = new ArrayList<ArrayList<String>>();
		ArrayList<String> sourceNodes = new ArrayList<String>();
		ArrayList<String> destinationNodes = new ArrayList<String>();
		ArrayList<String> connectionType = new ArrayList<String>();

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
				String[] sourceTemp = nodesInfo[0].split(":");

				// SOURCE NODE
				String source = sourceTemp[1];
				sourceNodes.add(source);

				// DESTINATION NODE
				if (line.substring(0, 1).equalsIgnoreCase("c")) {
					destinationNodes.add(nodesInfo[1]);
					connectionType.add("");
				} else if (line.substring(0, 1).equalsIgnoreCase("m")) {
					String[] destinationTemp = nodesInfo[1].split(":");
					String destination = destinationTemp[0].substring(3);
					destinationNodes.add(destination);
					connectionType.add(destinationTemp[0].substring(0, 3));
				}
			}
			s.close();

			sourceAndDestination.add(sourceNodes);
			sourceAndDestination.add(destinationNodes);
			sourceAndDestination.add(connectionType);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sourceAndDestination;
	}
}
