import java.util.*;


class GetBridges {
    public static void main(String[] args) {
        int m= 7;
        int n = 7;
        
        int[][] matrix = new int[m][n];
        
        matrix[0] = new int[]{0,1,1,0,0,0,0};
        matrix[1] = new int[]{0,0,1,0,0,0,1};
        matrix[2] = new int[]{0,0,1,1,1,0,1};
        matrix[3] = new int[]{0,0,0,0,0,0,0};
        matrix[4] = new int[]{0,0,0,0,1,1,1};
        matrix[5] = new int[]{0,0,0,0,0,0,0};
        matrix[6] = new int[]{1,1,1,0,1,1,1};
        
        int island_num = 1;
        
        boolean[][] visited = new boolean[m][n];
        
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < n; j++) {
                if(matrix[i][j] == 1 && !visited[i][j]) {
                    dfs(matrix, i, j, visited, island_num);
                    island_num++;
                }
            }
        }
        
        ArrayList<HashMap<Integer, DestinationNode>> adj = new ArrayList<>();
        
        for(int i = 0; i <= 5; i++) {
            adj.add(new HashMap<Integer, DestinationNode>());
        }
        
        bfsOnOutskirtCells(matrix, m, n, adj);
        
        // for(Map.Entry<Integer,DestinationNode> entry : adj.get(1).entrySet()) {
        //      System.out.println("Dest node ="  + entry.getKey());
            
        //     for(int[] subPath: entry.getValue().path) {
        //         System.out.println(subPath[0] +" , "+ subPath[1]);
        //     }
            
           
        //     System.out.println("path = " + entry.getValue().path);
        // }
        
        
    
        getMST(adj, 5);
       
        
    }
    
    
    public static void getMST(ArrayList<HashMap<Integer, DestinationNode>> adj, int N) {
        
        int[] keys = new int[N+1];
        Arrays.fill(keys, Integer.MAX_VALUE);
        
        boolean[] mst = new boolean[N+1];
        
        int[] parent = new int[N+1];
        
        parent[1] = 1;
        
        keys[1] = 0;
        
        for(int i = 0; i < N; i++) {
            
            int node = getMinEdgeNonMstNode(keys, mst, 5);
            
            mst[node] = true;
            
            System.out.println(node + " node added in mst with edge length = " + keys[node]);
            
            if(parent[node] != node) {
                System.out.println("bridges are ");
                // adj.get(parent[node]).get(node).path
                for(int[] bridge : adj.get(parent[node]).get(node).path) {
                    System.out.println(bridge[0] + " , " +  bridge[1]);
                }
            }
            
            for(Map.Entry<Integer, DestinationNode> entry: adj.get(node).entrySet()) {
                
                if(keys[entry.getKey()] > entry.getValue().dist) {
                    parent[entry.getKey()] = node;
                    keys[entry.getKey()] = entry.getValue().dist;
                }
            }
        }
    }
    
    public static int getMinEdgeNonMstNode(int[] keys, boolean[] mst, int N) {
        int res = -1;
      
       for(int i = 1; i <= N; i++) {
           if(mst[i]) continue;
           if(res == -1 || keys[res] > keys[i]) {
               res = i;
              
           }
       }
       return res;
        
        
        
    }
    
    public static void bfsOnOutskirtCells(int[][] matrix, int m, int n, ArrayList<HashMap<Integer, DestinationNode>> adj) {
        
        // get the valid outskirt cell: atleast one neighbour cell is 0
        
        int[] iPos = {0,0,-1,1};
        int[] jPos = {-1,1,0,0};
        
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < n; j++) {
                if(matrix[i][j] > 0) { // an island cell
                
                    for(int k = 0; k < 4; k++) {
                        int nxtI = i + iPos[k];
                        int nxtJ = j + jPos[k];
                        
                        if(nxtI < 0 || nxtJ < 0 || nxtI == matrix.length || nxtJ == matrix[0].length) continue;
                        
                        if(matrix[nxtI][nxtJ] > 0) continue;
                        // System.out.println("outskirt element i = " + i + " j = " + j);
                        fillAdjWithBfs(matrix, i, j, adj);
                        break;
                        
                    }
                    
                    
                }
            }
        }
        
        
        
    }
    
    public static void fillAdjWithBfs(int[][] matrix, int i, int j, ArrayList<HashMap<Integer, DestinationNode>> adj) {
        int island_num = matrix[i][j];
        
        boolean[][] visited = new boolean[matrix.length][matrix[0].length];
        
        int[] iPos = {0,0,-1,1};
        int[] jPos = {-1,1,0,0};
        
        // dont add same source island in the queue
        
        Queue<DestinationNode> q = new LinkedList<>();
        visited[i][j] = true;
        
        for(int k = 0; k < 4; k++) {
            int nxtI = i + iPos[k];
            int nxtJ = j + jPos[k];
            if(nxtI < 0 || nxtJ < 0 || nxtI == matrix.length || nxtJ == matrix[0].length) continue;
            
            if(matrix[nxtI][nxtJ] == 0) {
                visited[nxtI][nxtJ] = true;
                ArrayList<int[]> path = new ArrayList<>();
                path.add(new int[]{nxtI, nxtJ});
                q.add(new DestinationNode(nxtI, nxtJ, 0, path));
            }
        }
       // q.add(new int[] {i,j, 0});
        
        
        while(!q.isEmpty()) {
            DestinationNode top = q.remove();
            
            if(matrix[top.i][top.j] > 1) {
                
                if(matrix[top.i][top.j] == island_num) continue;
                
                // reached a destination node
                
              
            if(adj.get(island_num).get(matrix[top.i][top.j]) == null || 
            adj.get(island_num).get(matrix[top.i][top.j]).dist > top.dist) {
                top.path.remove(top.path.size()-1);
                adj.get(island_num).put(matrix[top.i][top.j], new DestinationNode(top.i, top.j, top.dist, top.path));
            }
                    
                
            } else {
                for(int k = 0; k < 4; k++) {
                    int nxtI = top.i + iPos[k];
                    int nxtJ = top.j + jPos[k];
                        
                    if(nxtI < 0 || nxtJ < 0 || nxtI == matrix.length || nxtJ == matrix[0].length) continue;
                    
                    if(visited[nxtI][nxtJ]) continue;
                    
                    visited[nxtI][nxtJ] = true;
                    
                    ArrayList<int[]> newPath = new ArrayList<>(top.path);
                    
                    newPath.add(new int[]{nxtI,nxtJ});
                    
    
                    q.add(new DestinationNode(nxtI, nxtJ, top.dist+1, newPath));
                    
                }
            }
                
            }
            
        }
        
        public static void dfs(int[][] matrix, int i, int j, boolean[][] visited, int island_num) {
        
        visited[i][j] = true;
        matrix[i][j] = island_num;
        
        int[] iPos = {0,0,-1,1};
        int[] jPos = {-1,1,0,0};
        
        for(int k = 0; k < 4; k++) {
            int nxtI = i + iPos[k];
            int nxtJ = j + jPos[k];
            
            if(nxtI < 0 || nxtJ < 0 || nxtI == matrix.length || nxtJ == matrix[0].length) continue;
            
            if(matrix[nxtI][nxtJ] == 0 || visited[nxtI][nxtJ]) continue;
            
            dfs(matrix, nxtI, nxtJ, visited, island_num);

        }
        
    }
    
}



class DestinationNode{
    public int i;
    public int j; 
    public int dist;
    
    public ArrayList<int[]> path = new ArrayList<>();
    
    public DestinationNode(int i, int j, int dist, 
    ArrayList<int[]> path) {
        
        this.i = i;
        this.j = j;
        this.dist = dist;
        this.path = path;
    }
    
    
}
