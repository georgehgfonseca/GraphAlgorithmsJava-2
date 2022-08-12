public class Graph {

  private int countNodes;
  private int countEdges;
  private int[][] adjMatrix;

  public Graph(int numNodes) {
    this.countNodes = numNodes;
    this.countEdges = 0;
    this.adjMatrix = new int[numNodes][numNodes];
  }

  public void addEdge(int source, int sink, int weight) {
    if (source < 0 || source > this.adjMatrix.length - 1 ||
        sink < 0 || sink > this.adjMatrix.length - 1 ||
        weight <= 0) {
      System.err.printf("Invalid edge: %d %d %d\n", source, sink, weight);
      return;
    }
    this.countEdges++;
    this.adjMatrix[source][sink] = weight;
  }

  public int degree(int node) {
    if (node < 0 || node > this.adjMatrix.length) {
      System.out.println("Invalid node: " + node);
      return -1;
    }
    int degree = 0;
    for (int j = 0; j < this.adjMatrix[node].length; ++j) {
      if (this.adjMatrix[node][j] != 0)
        degree++;
    }
    return degree;
  }

  public int highestDegree() {
    int highest = 0;
    for (int i = 0; i < this.adjMatrix.length; ++i) {
      int degreeI = degree(i);
      if (degreeI > highest)
        highest = degreeI;
    }
    return highest;
  }

  public String toString() {
    String str = "";
    for (int i = 0; i < this.adjMatrix.length; ++i) {
      for (int j = 0; j < this.adjMatrix[i].length; ++j) {
        str += this.adjMatrix[i][j] + "\t";
      }
      str += "\n";
    }
    return str;
  }

}