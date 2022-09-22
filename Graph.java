import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Graph {

  private int countNodes;
  private int countEdges;
  private int[][] adjMatrix;

  public Graph(int numNodes) {
    this.countNodes = numNodes;
    this.countEdges = 0;
    this.adjMatrix = new int[numNodes][numNodes];
  }

  public Graph(String fileName) throws IOException {
    File file = new File(fileName);
    FileReader reader = new FileReader(file);
    BufferedReader bufferedReader = new BufferedReader(reader);

    // Read header
    String[] line = bufferedReader.readLine().split(" ");
    this.countNodes = (Integer.parseInt(line[0]));
    int fileLines = (Integer.parseInt(line[1]));

    // Create and fill adjMatrix with read edges
    this.adjMatrix = new int[this.countNodes][this.countNodes];
    for (int i = 0; i < fileLines; ++i) {
      String[] edgeInfo = bufferedReader.readLine().split(" ");
      int source = Integer.parseInt(edgeInfo[0]);
      int sink = Integer.parseInt(edgeInfo[1]);
      int weight = Integer.parseInt(edgeInfo[2]);
      addEdge(source, sink, weight);
    }
    bufferedReader.close();
    reader.close();
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

  public void addEdgeUnoriented(int source, int sink, int weight) {
    if (source < 0 || source > this.adjMatrix.length - 1 ||
        sink < 0 || sink > this.adjMatrix.length - 1 ||
        weight <= 0) {
      System.err.printf("Invalid edge: %d %d %d\n", source, sink, weight);
      return;
    }
    this.countEdges += 2;
    this.adjMatrix[source][sink] = weight;
    this.adjMatrix[sink][source] = weight;
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

  public int lowestDegree() {
    int lowest = this.countNodes;
    for (int i = 0; i < this.adjMatrix.length; ++i) {
      int degreeNodeI = this.degree(i);
      if (lowest > degreeNodeI)
        lowest = degreeNodeI;
    }
    return lowest;
  }

  public Graph complement() {
    Graph g2 = new Graph(this.countNodes);
    for (int i = 0; i < this.adjMatrix.length; ++i) {
      for (int j = 0; j < this.adjMatrix[i].length; ++j) {
        if (i != j && this.adjMatrix[i][j] == 0) {
          g2.addEdge(i, j, 1);
        }
      }
    }
    return g2;
  }

  public float density() {
    return (float) this.countEdges / (this.countNodes * (this.countNodes - 1));
  }

  public boolean oriented() {
    for (int i = 0; i < this.adjMatrix.length; ++i) {
      for (int j = 0; j < this.adjMatrix[i].length; ++j) {
        if (this.adjMatrix[i][j] != this.adjMatrix[j][i])
          return true;
      }
    }
    return false;
  }

  public ArrayList<Integer> bfs(int s) { // busca em largura
    // initialization
    int[] desc = new int[this.countNodes];
    ArrayList<Integer> Q = new ArrayList<>();
    Q.add(s);
    ArrayList<Integer> R = new ArrayList<>();
    R.add(s);
    desc[s] = 1;
    // main loop
    while (Q.size() > 0) {
      int u = Q.remove(0);
      for (int v = 0; v < this.adjMatrix[u].length; ++v) {
        if (this.adjMatrix[u][v] != 0) { // v é adjacente a u
          if (desc[v] == 0) {
            Q.add(v);
            R.add(v);
            desc[v] = 1;
          }
        }
      }
    }
    return R;
  }

  public int notDescAdj(int u, int[] desc) {
    for (int v = 0; v < this.adjMatrix[u].length; ++v) {
      if (this.adjMatrix[u][v] != 0 && desc[v] == 0)
        return v;
    }
    return -1;
  }

  public ArrayList<Integer> dfs(int s) { // busca em profundidade
    // initialization
    int[] desc = new int[this.countNodes];
    ArrayList<Integer> S = new ArrayList<>();
    S.add(s);
    ArrayList<Integer> R = new ArrayList<>();
    R.add(s);
    desc[s] = 1;
    // main loop
    while (S.size() > 0) {
      int u = S.get(S.size() - 1);
      int v = notDescAdj(u, desc);
      if (v != -1) {
        S.add(v);
        R.add(v);
      } else {
        S.remove(S.size() - 1);
      }
    }
    return R;
  }

  public boolean connected() {
    return this.bfs(0).size() == this.countNodes;
  }

  public ArrayList<Integer> dfsRec(int s) {
    int[] desc = new int[this.countNodes];
    ArrayList<Integer> R = new ArrayList<>();
    dfsRecAux(s, desc, R);
    return R;
  }

  public void dfsRecAux(int u, int[] desc, ArrayList<Integer> R) {
    desc[u] = 1;
    R.add(u);
    for (int v = 0; v < this.adjMatrix[u].length; ++v) {
      if (this.adjMatrix[u][v] != 0 && desc[v] == 0) {
        dfsRecAux(v, desc, R);
      }
    }
  }

  public ArrayList<Integer> topSort() {
    int[] desc = new int[this.countNodes];
    ArrayList<Integer> R = new ArrayList<>();
    for (int v = 0; v < this.adjMatrix.length; ++v) {
      if (desc[v] == 0)
        topSortAux(v, desc, R);
    }
    return R;
  }

  public void topSortAux(int u, int[] desc, ArrayList<Integer> R) {
    desc[u] = 1;
    for (int v = 0; v < this.adjMatrix[u].length; ++v) {
      if (this.adjMatrix[u][v] != 0 && desc[v] == 0)
        topSortAux(v, desc, R);
    }
    R.add(0, u);
  }

  public int[] connectedComp() {
    int[] desc = new int[this.countNodes];
    int comp = 0;
    for (int v = 0; v < this.adjMatrix.length; ++v) {
      if (desc[v] == 0) {
        comp += 1;
        connectedCompAux(v, desc, comp);
      }
    }
    return desc;
  }

  public void connectedCompAux(int u, int[] desc, int comp) {
    desc[u] = comp;
    for (int v = 0; v < this.adjMatrix[u].length; ++v) {
      if (this.adjMatrix[u][v] != 0 && desc[v] == 0)
        connectedCompAux(v, desc, comp);
    }
  }

  public boolean hasCycleOriented() {
    // based on bfs
    int[] desc = new int[this.countNodes];
    for (int s = 0; s < this.countNodes; ++s) {
      if (desc[s] == 0) {
        ArrayList<Integer> Q = new ArrayList<>();
        Q.add(s);
        desc[s] = 1;
        while (Q.size() > 0) {
          int u = Q.remove(0);
          for (int v = 0; v < this.adjMatrix[u].length; ++v) {
            if (this.adjMatrix[u][v] != 0) { 
              if (desc[v] == 0) {
                Q.add(v);
                desc[v] = 1;
              }
              else {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
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