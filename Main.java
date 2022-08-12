class Main {
  public static void main(String[] args) {
    System.out.println("Hello world!");
    Graph g1 = new Graph(4);
    g1.addEdge(0, 1, 3);
    g1.addEdge(1, 0, 3);
    g1.addEdge(0, 3, 4);
    g1.addEdge(3, 0, 4);
    g1.addEdge(0, 4, 1); // Warning
    System.out.println(g1); 
    g1.degree(0); //2
    g1.degree(1); //1
    g1.degree(2); //0
    g1.degree(3); //1
  }
}