/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphalgorithms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author Jaret
 */
public class GraphAlgorithms {

    // First row of input file
    public static String[] verticies;
    // Number of verticies
    public static int size;
    // Adjacency Matrix
    public static int[][] adjMatrix;
            
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Read in matrix from file
        try {
            readMatrix();
        }
        catch (IOException e) {
            System.out.println("No Input File Found");
            return;
        }
        // Runs different algorithms
        prim();
        kruskal();
        warshall();
    }
    
    // Reads in matrix
    public static void readMatrix() throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader("adjMatrix.csv"));
        verticies = reader.readLine().split(",");
        size = verticies.length;
        adjMatrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            String[] row = reader.readLine().split(",");
            for (int j = 0; j < size; j++) {
                adjMatrix[i][j] = Integer.parseInt(row[j]);
            }
        }
    }
    
    public static void prim() {
        // Whether vertice has been visited
        int[] selected = new int[size];
        
        selected[0] = 1;
        
        int edges = 0;
        int x;
        int y;
        
        System.out.println("Prim's Algorithm");
        
        while (edges < size - 1) {
            
            int min = Integer.MAX_VALUE/2;
            x = 0;
            y = 0;
        
            // For each edge not in queue
            for (int i = 0; i < size; i++) {
                if (selected[i] == 1) {
                    for (int j = 0; j < size; j++) {
                        if (selected[j] != 1 && adjMatrix[i][j] != 0) {
                            // If weight is less than current minimum, update
                            if (min > adjMatrix[i][j]) {
                                min = adjMatrix[i][j];
                                x = i;
                                y = j;
                            }
                        }
                    }
                }
            }
            
            // Print added edge
            edges++;
            selected[y] = 1;
            System.out.print(verticies[x] + verticies[y] + " ");
        }
        
        System.out.println("\n");
    }
    
    public static void kruskal() {
        // Forest of trees
        HashMap<Integer, Set<Integer>> forest = new HashMap();
        PriorityQueue<Edge> edges = new PriorityQueue();
        ArrayList<Edge> mst = new ArrayList();
        
        // Initialize forest
        for (int i = 0; i < size; i++) {
            Set<Integer> vs = new HashSet<Integer>();
            vs.add(i);
            forest.put(i, vs);
        }
        
        // Get all edges from matrix
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (adjMatrix[i][j] != 0) {
                    edges.add(new Edge(i, j, adjMatrix[i][j]));
                }
            }
        }
        
        while (mst.size() != size-1) {
            // Get edge
            Edge edge = edges.poll();
            Set<Integer> tree1 = forest.get(edge.v1);
            Set<Integer> tree2 = forest.get(edge.v2);
            if (tree1.equals(tree2)) {
                // go to end of loop
                continue;
            }
            // combine two trees and add edge
            mst.add(edge);
            tree1.addAll(tree2);
            for(Integer i : tree1)
            {
                forest.put(i, tree1);
            }
        }
        
        // Print edges
        System.out.println("Kruskal's Algorithm");
        for (Edge edge: mst) {
            System.out.print(verticies[edge.v1] + verticies[edge.v2] + " ");
        }
        System.out.println("\n");
    }
    
    public static void warshall() {
        System.out.println("Floyd-Warshall's Algorithm\n");
        int[][] matrix = new int[size][size];
        // Initialize matrix to infinity and 0 on diagonal
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    matrix[i][j] = 0;
                }
                else {
                    matrix[i][j] = Integer.MAX_VALUE/2;
                }
            }
        }
        
        // Add values from adj matrix
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (adjMatrix[i][j] != 0) {
                    matrix[i][j] = adjMatrix[i][j];
                }
            }
        }
        
        printMatrix(matrix);
        
        // If a value is larger than sum of two possible's, update matrix
        for (int k = 0; k < size; k++) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (matrix[i][j] > matrix[i][k] + matrix[k][j]) {
                        matrix[i][j] = matrix[i][k] + matrix[k][j];
                        printMatrix(matrix);
                    } 
                }
            }
        }
    }
    
    // Print matrix for warshall's algorithm
    public static void printMatrix(int[][] matrix) {
        System.out.print("\t");
        for (int i = 0; i < verticies.length; i++) {
            System.out.print(verticies[i] + "\t");
        }
        System.out.println();
        for (int i = 0; i < matrix.length; i++) {
            System.out.print(verticies[i] + "\t");
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == Integer.MAX_VALUE/2) {
                    System.out.print("∞\t");
                }
                else {
                    System.out.print(matrix[i][j] + "\t");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
   
}
