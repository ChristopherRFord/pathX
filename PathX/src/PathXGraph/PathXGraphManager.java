/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PathXGraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;




/**
 *
 * @author Chris
 */
public class PathXGraphManager
{
    private PathXGraph graph;
    
    public PathXGraphManager(ArrayList<Intersection> Intersections, ArrayList<Road> Roads)
    {
        graph = new PathXGraph(Intersections, Roads);
    }
    
    /*
    * DISKTRA'S
    */
    public ArrayList<Road> shortestDijkstraPath(Intersection start, Intersection dest)
    {
        if (start == dest) return null;
        ArrayList<Intersection> dijkstra = graph.Dijkstra(start, dest);
        return graph.returnShortestPathDijkstra(dijkstra);
    }
    
    /*
    * DISKTRA'S
    */
    public ArrayList<Road> shortestNoRLDijkstraPath(Intersection start, Intersection dest)
    {
        if (start == dest) return null;
        ArrayList<Intersection> dijkstra = graph.NoRLDijkstra(start, dest);
        return graph.returnShortestPathDijkstra(dijkstra);
    }
    
        /*
    * DISKTRA'S
    */
    public ArrayList<Road> shortestNoCRDijkstraPath(Intersection start, Intersection dest)
    {
        if (start == dest) return null;
        ArrayList<Intersection> dijkstra = graph.NoCRDijkstra(start, dest);
        return graph.returnShortestPathDijkstra(dijkstra);
    }
    
    public ArrayList<Road> shortestNoRestrictionDijkstra(Intersection start, Intersection dest)
    {
        if (start == dest) return null;
        ArrayList<Intersection> dijkstra = graph.NoRestDijkstra(start, dest);
        return graph.returnShortestPathDijkstra(dijkstra);
    }

    class PathXGraph
    {
        protected ArrayList<Intersection> Intersections;
        protected ArrayList<Road> Roads;
           
        public PathXGraph(ArrayList<Intersection> Intersections, ArrayList<Road> Roads)
        {
            this.Intersections = Intersections;
            this.Roads = Roads;;
        }
        
        /*
        * DIJKSTRA
        */
        protected ArrayList<Intersection> Dijkstra(Intersection start, Intersection target)
        {
            Iterator<Intersection> iIt = Intersections.iterator();
            
            while (iIt.hasNext())
            {
                Intersection temp = iIt.next();
                temp.distance = Double.POSITIVE_INFINITY;
            }
            
            start.distance = 0;
            Queue q = new LinkedList();
            q.add(start);
            
            while (!q.isEmpty())
            {
                Intersection u = (Intersection) q.poll();
                
                if (u.distance == Double.POSITIVE_INFINITY)
                {
                    break;
                }
                
                for (Road r : u.getRoads())
                {
                    Intersection v;
                    
                    if (r.node1 == u)   v = r.node2;
                    else                v = r.node1;
                    
                    if (v == r.node1 && r.oneWay)   continue;
                    
                    int distance = (int)(u.distance + r.tempSpeedLimit);
                    
                    if (distance < v.distance)
                    {
                        v.distance = distance;
                        v.previous = u;
                        q.add(v);
                    }
                }
            }
            
            Intersection temp = target;
            ArrayList<Intersection> returnList = new ArrayList<Intersection>();
            while (temp != null)
            {
                returnList.add(temp);
                temp = temp.previous;
            }
            
            resetDijkstra();
            return returnList;
        }
        
        /*
        * DIJKSTRA no red lights
        */
        protected ArrayList<Intersection> NoRLDijkstra(Intersection start, Intersection target)
        {
            Iterator<Intersection> iIt = Intersections.iterator();
            
            while (iIt.hasNext())
            {
                Intersection temp = iIt.next();
                temp.distance = Double.POSITIVE_INFINITY;
            }
            
            start.distance = 0;
            Queue q = new LinkedList();
            q.add(start);
            
            while (!q.isEmpty())
            {
                Intersection u = (Intersection) q.poll();
                
                if (u.distance == Double.POSITIVE_INFINITY)
                {
                    break;
                }
                
                for (Road r : u.getRoads())
                {
                    Intersection v;
                    
                    if (r.node1 == u)   v = r.node2;
                    else                v = r.node1;
                    
                    if(!v.open)                     continue;
                    if (v == r.node1 && r.oneWay)   continue;
                    
                    int distance = (int)(u.distance + r.tempSpeedLimit);
                    
                    if (distance < v.distance)
                    {
                        v.distance = distance;
                        v.previous = u;
                        q.add(v);
                    }
                }
            }
            
            Intersection temp = target;
            ArrayList<Intersection> returnList = new ArrayList<Intersection>();
            while (temp != null)
            {
                returnList.add(temp);
                temp = temp.previous;
            }
            
            resetDijkstra();
            return returnList;
        }
        
        /*
        * DIJKSTRA no closed roads
        */
        protected ArrayList<Intersection> NoCRDijkstra(Intersection start, Intersection target)
        {
            Iterator<Intersection> iIt = Intersections.iterator();
            
            while (iIt.hasNext())
            {
                Intersection temp = iIt.next();
                temp.distance = Double.POSITIVE_INFINITY;
            }
            
            start.distance = 0;
            Queue q = new LinkedList();
            q.add(start);
            
            while (!q.isEmpty())
            {
                Intersection u = (Intersection) q.poll();
                
                if (u.distance == Double.POSITIVE_INFINITY)
                {
                    break;
                }
                
                for (Road r : u.getRoads())
                {
                    Intersection v;
                    
                    if (r.closed)       continue;
                    if (r.node1 == u)   v = r.node2;
                    else                v = r.node1;
                    
                    if (v == r.node1 && r.oneWay)   continue;
                    
                    int distance = (int)(u.distance + r.tempSpeedLimit);
                    
                    if (distance < v.distance)
                    {
                        v.distance = distance;
                        v.previous = u;
                        q.add(v);
                    }
                }
            }
            
            Intersection temp = target;
            ArrayList<Intersection> returnList = new ArrayList<Intersection>();
            while (temp != null)
            {
                returnList.add(temp);
                temp = temp.previous;
            }
            
            resetDijkstra();
            return returnList;
        }
        
        protected ArrayList<Intersection> NoRestDijkstra(Intersection start, Intersection target)
        {
            Iterator<Intersection> iIt = Intersections.iterator();
            
            while (iIt.hasNext())
            {
                Intersection temp = iIt.next();
                temp.distance = Double.POSITIVE_INFINITY;
            }
            
            start.distance = 0;
            Queue q = new LinkedList();
            q.add(start);
            
            while (!q.isEmpty())
            {
                Intersection u = (Intersection) q.poll();
                
                if (u.distance == Double.POSITIVE_INFINITY)
                {
                    break;
                }
                
                for (Road r : u.getRoads())
                {
                    Intersection v;
                    
                    if (r.node1 == u)   v = r.node2;
                    else                v = r.node1;
                    
                    int distance = (int)(u.distance + r.tempSpeedLimit);
                    
                    if (distance < v.distance)
                    {
                        v.distance = distance;
                        v.previous = u;
                        q.add(v);
                    }
                }
            }
            
            Intersection temp = target;
            ArrayList<Intersection> returnList = new ArrayList<Intersection>();
            while (temp != null)
            {
                returnList.add(temp);
                temp = temp.previous;
            }
            
            resetDijkstra();
            return returnList;
        }
        
        public ArrayList<Road> returnShortestPathDijkstra(ArrayList<Intersection> dijkstra)
        {
            ArrayList<Road> road = new ArrayList<Road>();
            for(int i = dijkstra.size()-1; i > 0; i--)
            {
                Intersection temp1 = dijkstra.get(i);
                Intersection temp2 = dijkstra.get(i-1);
                
                Iterator<Road> rIt = Roads.iterator();
                while (rIt.hasNext())
                {
                    Road tempRoad = rIt.next();
                    
                    if ((temp1 == tempRoad.node1 && temp2 == tempRoad.node2) || (temp2 == tempRoad.node1 && temp1 == tempRoad.node2))
                    {
                        road.add(tempRoad);
                    }
                }
            }
            
            return road;
        }
        
        private void resetDijkstra()
        {
            Iterator<Intersection> iTr = Intersections.iterator();
            while (iTr.hasNext())
            {
                Intersection temp = iTr.next();
                temp.previous = null;
            }
        }
 
    }
}
