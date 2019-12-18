import java.util.ArrayList;
import java.util.LinkedList;
import java.lang.*;
import java.lang.String;


public class NFASimulator {

  private Graph nfa;
  private static String start;
  private static String text;
  private static ArrayList<String> accepts = new ArrayList<String>();
  private static ArrayList<String> transitions = new ArrayList<String>();
  private static ArrayList<String> allNodes = new ArrayList<String>();

  public static class Graph {
    int gLength;
    String startState;
    String[][] gArray;

    Graph(int gLength){
      this.gLength = gLength;
      gArray = new String [gLength][gLength];
      for (int i = 0; i < gLength; i++) {
        for (int k = 0; k < gLength; k++) {
          gArray[i][k] = "";
        }
      }
      startState = start;
    }

    private void addEdge(int src, int dest, String name) {
      gArray[src][dest] = name;
    }

    public String[][] getGArray() {
      return gArray;
    }

    public String getIndex(int a, int b) {
      return gArray[a][b];
    }

  }

  public NFASimulator() {
    nfa = new Graph(allNodes.size());
    initiateGraph(nfa);
    System.out.println(isAccepted(nfa, text));
    System.out.println("start = " + start);
    System.out.println("allNodes = " + allNodes);
    System.out.println("accepts = " + accepts);
    System.out.println("transitions = " + transitions);
  }

  public static void initiateGraph(Graph graph) {
    String temp;
    String src;
    String dest;
    String name;
    for (int i = 0; i < transitions.size(); i++) {
      temp = transitions.get(i);
      if (temp.contains(":")) {
        name = temp.substring( temp.indexOf(":") + 1, temp.indexOf("->") );
        src = temp.substring( 0, temp.indexOf(":") );
        dest = temp.substring( temp.indexOf("->"), temp.length() );
      } else {
        name = "epsilon";
        src = temp.substring( 0, temp.indexOf("->") );
        dest = temp.substring( temp.indexOf("->") + 2, temp.length() );
      }
      for (int k = 0; k < allNodes.size(); k++) {
        for (int j = 0; j < allNodes.size(); j++) {
          if ( allNodes.get(k).equals(src) && allNodes.get(j).equals(dest) ) {
            // addEdge(graph, k, j, name);
            System.out.println("k = " + k + " j = " + j + " name = " + name);
            graph.addEdge(k, j, name);
          }
        }
      }
    }
  }
  //START=q0;ACCEPT=q2,q1 q0:a->q1 q0:a->q2 q0->q2 q0:a->q0

  public static void organizeArgs(String[] splitInputs) {
    String temp;
    //save the transitions
    for (int i = 1; i < splitInputs.length; i++){
      transitions.add(splitInputs[i]);
    }
    String saString = splitInputs[0];
    //save the start node
    start = saString.substring( 6, saString.indexOf(";") );
    allNodes.add(start);
    //save the accept states
    String acceptString = saString.substring( saString.indexOf("ACCEPT=") + 7, saString.length() );
    String[] acceptArr = acceptString.trim().split(",");
    for (String word : acceptArr) {
      accepts.add(word);
      allNodes.add(word);
    }
    //save all nodes
    for (int i = 0; i < transitions.size(); i++) {
      temp = transitions.get(i);

      if (temp.contains(":")) {
        temp = temp.substring(0, temp.indexOf(":"));
        if (!allNodes.contains(temp)) {
          allNodes.add(temp);
        }
      } else {
        temp = temp.substring(0, temp.indexOf("->"));
        if (!allNodes.contains(temp)) {
          allNodes.add(temp);
        }
      }

      temp = transitions.get(i);
      temp = temp.substring( temp.indexOf("->") + 2, temp.length() );
      if (!allNodes.contains(temp)) {
        allNodes.add(temp);
      }
    }
  }

  public static boolean isAccepted(Graph graph, String text) {
    int src = 0;
    for (int i = 0; i < allNodes.size(); i++) {
      if ( allNodes.get(i).equals(start) ) {
        src = i;
      }
    }
    return nextStates(graph, src, text);
  }

  public static boolean nextStates(Graph graph, int src, String text) {
    // String[][] gArray = graph.getGArray();
    for (int i = 0; i < allNodes.size(); i++) {
      if ( graph.getIndex(src, i).equals("epsilon") ) {
        System.out.println("epsilon return");
        return nextStates(graph, i, text);
      }
    }
    if (text.length() == 1) {
      System.out.println("text length 1");
      for (int i = 0; i < allNodes.size(); i++) {
        if ( graph.getIndex(src, i).equals(text) ) {
          System.out.println("gArray = text");
          for (int k = 0; k < accepts.size(); k++) {
            if ( allNodes.get(i).equals(accepts.get(k)) ) {
              return true;
            }
          }
        }
      }
    } else {
      System.out.println("text length 2");
      for (int i = 0; i < allNodes.size(); i++) {
        if ( graph.getIndex(src, i).equals(text.indexOf(0)) ) {
          System.out.println("other return");
          return nextStates( graph, i, text.substring(1, text.length()) );
        }
      }
    }
    return false;
  }

  public static void main ( String[] args ){
    //save the string to be compared
    text = args[1];
    String[] splitInputs = args[0].trim().split("\\s+");
    organizeArgs(splitInputs);
    NFASimulator nfa = new NFASimulator();
    // System.out.println(nfa);
    // isAccepted(graph, text);
    //START=q0;ACCEPT=q2,q1 q0:a->q1 q0:a->q2 q0->q2 q0:a->q0

    //START=q0;ACCEPT=q2,q1 q0:a->q1 q0:a->q2 q0->q2 q0:a->q0
  }


}
