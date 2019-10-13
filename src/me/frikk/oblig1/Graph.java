package me.frikk.oblig1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Graph {
	private Node[] nodes;
	private List<ArrayList<Node>> components = new ArrayList<ArrayList<Node>>();

	public Graph(Node[] nodes) {
		this.nodes = nodes;
	}

	public static void main(String[] args) {
		Graph graph = buildRandomSparseGraph(11, 201909202359L);
		graph.printInfo();
	}

	public ArrayList<Node> DFS(Node s, ArrayList<Node> nodesInComponent) {
		s.visit();
		nodesInComponent.add(s);
		
		for (Node n : s.getNeighbors()) {
			if (!nodesInComponent.contains(n)) {
				nodesInComponent = DFS(n, nodesInComponent);
			} 
		}
		return nodesInComponent;
	}

	public void DFSFull() {
		for (Node v : this.nodes) {
			if (!v.isVisited()) {
				components.add(DFS(v, new ArrayList<Node>()));
			}
		}
	}

	/**
	 * Oppgave 1A
	 * @return antall sammenhengende komponenter
	 */
    public int numberOfComponents() {
		if (components.isEmpty()) {
			DFSFull();
		}
		return components.size();
	}

	/**
	 * Oppgave 1B
	 * @return tilsvarende urettet graf
	 */
    public Graph transformDirToUndir() {
		int size = nodes.length;
		Node[] undirNodes = new Node[size];

		for (int i = 0; i < size; i++) {
			Node dirNode = nodes[i];
			Node undirNode = new Node(dirNode.getLabel());
			undirNodes[i] = undirNode;
		}

		for (int i = 0; i < size; i++) {
			Node dirNode = nodes[i];
			Node undirNode = undirNodes[i];
			for (Node neighbour : dirNode.getNeighbors()) {
				int label = neighbour.getLabel();
				undirNode.addNeighbor(undirNodes[label]);
			}
		}
	    return new Graph(undirNodes);
	}

	/**
	 * Oppgave 1C
	 * Vi vet at grafen er svakt sammenhengende dersom den underliggende
	 * urettede grafen er sammenhengende. 
	 * 
	 * @return true hvis grafen er svakt sammenhengende
	 */
    public boolean isConnected(){
		Graph undir = this.transformDirToUndir();
		return undir.numberOfComponents() == 1 ? true : false;
	}

	/**
	 * Oppgave 1D
	 * Sorterer nodene for pen utskrift
	 * @return ny graf besående av nodene i den største komponenten
	 */
    public Graph biggestComponent() {
		if (components.isEmpty()) {
			DFSFull();
		}

		ArrayList<Node> biggestComponent = components.get(0);
		
		for (ArrayList<Node> nodes : components) {
			Collections.sort(nodes, new Comparator<Node>() {
				@Override 
				public int compare(Node n, Node v) {
					return Integer.compare(n.getLabel(), v.getLabel());
				}
			});

			if(nodes.size() > biggestComponent.size()) {
				biggestComponent = nodes;
			}
		}

		Node[] biggestCompNodes = biggestComponent.toArray(new Node[biggestComponent.size()]);
	    return new Graph(biggestCompNodes);
	}

	/**
	 * Oppgave 1E
	 * @return nabomatrise til grafen
	 */
	public int[][] buildAdjacencyMatrix() {
		int size = nodes.length;
		int[][] adjMatrix = new int[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if(nodes[i].getNeighbors().contains(nodes[j]) || nodes[i] == nodes[j]) {
					adjMatrix[i][j] = 1;
				} else {
					adjMatrix[i][j] = 0;
				}
			}
		}
	    return adjMatrix;
	}

	public void printInfo() {
		System.out.println("Nodes in graph:");
		this.printNeighbors();
		System.out.println(String.format("\nThis graph contains %d components", this.numberOfComponents()));

		String printString = this.isConnected() 
						? "Graph is connected" 
						: "Graph is not connected";

		System.out.println(printString); 

		System.out.println("\nThis graph's adjacency matrix:");
		int[][] adjMatrix = this.buildAdjacencyMatrix();

		for (int[] row : adjMatrix) {
			System.out.println(Arrays.toString(row));
		}

		Graph biggestComp = this.biggestComponent();
		System.out.println("\nBiggest component in graph:");
		biggestComp.printNeighbors();
	}

	public void printNeighbors() {
		for (Node n1 : nodes) {
			String s = n1.toString() + ": ";
			for (Node n2 : n1.getNeighbors()) {
				s += n2.toString() + " ";
			}
			System.out.println(s.substring(0, s.length() - 1));
		}
	}

	private static Graph buildExampleGraph() {
	    // ukeoppgave
		Node[] nodes = new Node[7];
		for (int i = 0; i < 7; i++) {
			nodes[i] = new Node(i);
		}
		nodes[0].addNeighbor(nodes[1]);
		nodes[0].addNeighbor(nodes[2]);
		nodes[1].addNeighbor(nodes[2]);
		nodes[2].addNeighbor(nodes[3]);
		nodes[2].addNeighbor(nodes[5]);
		nodes[3].addNeighbor(nodes[4]);
		nodes[4].addNeighbor(nodes[5]);
		nodes[5].addNeighbor(nodes[6]);
		return new Graph(nodes);
	}

	private static Graph buildRandomSparseGraph(int numberofV, long seed) {
		java.util.Random tilf = new java.util.Random(seed);
		int tilfeldig1 = 0, tilfeldig2 = 0;
		Node[] nodes = new Node[numberofV];

		for (int i = 0; i < numberofV; i++) {
			nodes[i] = new Node(i);
		}

		for (int i = 0; i < numberofV; i++) {
			tilfeldig1 = tilf.nextInt(numberofV);
			tilfeldig2 = tilf.nextInt(numberofV);
			if (tilfeldig1 != tilfeldig2)
				nodes[tilfeldig1].addNeighbor(nodes[tilfeldig2]);
		}
		return new Graph(nodes);
	}

	private static Graph buildRandomDenseGraph(int numberofV, long seed) {
		java.util.Random tilf = new java.util.Random(seed);
		int tilfeldig1 = 0, tilfeldig2 = 0;
		Node[] nodes = new Node[numberofV];

		for (int i = 0; i < numberofV; i++) {
			nodes[i] = new Node(i);
		}

		for (int i = 0; i < numberofV * numberofV; i++) {
			tilfeldig1 = tilf.nextInt(numberofV);
			tilfeldig2 = tilf.nextInt(numberofV);
			if (tilfeldig1 != tilfeldig2)
				nodes[tilfeldig1].addNeighbor(nodes[tilfeldig2]);
		}
		return new Graph(nodes);
	}

	private static Graph buildRandomDirGraph(int numberofV, long seed) {
		java.util.Random tilf = new java.util.Random(seed);
		int tilfeldig1 = 0, tilfeldig2 = 0;
		Node[] nodes = new Node[numberofV];

		for (int i = 0; i < numberofV; i++) {
			nodes[i] = new Node(i);
		}

		for (int i = 0; i < 2 * numberofV; i++) {
			tilfeldig1 = tilf.nextInt(numberofV);
			tilfeldig2 = tilf.nextInt(numberofV);
			if (tilfeldig1 != tilfeldig2)
				nodes[tilfeldig1].addSuccessor(nodes[tilfeldig2]);
		}
		return new Graph(nodes);
	}
}