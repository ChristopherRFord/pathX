/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PathXGraph;

import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class Road
{
    // THESE ARE THE EDGE'S NODES
    Intersection node1;
    Intersection node2;
    
    public ArrayList<Intersection> intersections = new ArrayList<Intersection>();
    
    // false IF IT'S TWO-WAY, true IF IT'S ONE WAY
    boolean oneWay;
    public boolean closed = false;
    
    // ROAD SPEED LIMIT
    int speedLimit;
    int tempSpeedLimit;

    // ACCESSOR METHODS
    public Intersection getNode1()  {   return node1;       }
    public Intersection getNode2()  {   return node2;       }
    public boolean isOneWay()       {   return oneWay;      }
    public int getSpeedLimit()      {   return speedLimit;  }
    
    
    // MUTATOR METHODS
    public void setNode1(Intersection node1)    {   this.node1 = node1;
                                                    intersections.add(node1);}
    public void setNode2(Intersection node2)    {   this.node2 = node2;
                                                    intersections.add(node2);}
    public void setOneWay(boolean oneWay)       {   this.oneWay = oneWay;           }
    public void setSpeedLimit(int speedLimit)
    {
        this.speedLimit = speedLimit;
        
        if (speedLimit == 10) tempSpeedLimit = 100;
        else if (speedLimit == 20) tempSpeedLimit = 90;
        else if (speedLimit == 30) tempSpeedLimit = 80;
        else if (speedLimit == 40) tempSpeedLimit = 70;
        else if (speedLimit == 50) tempSpeedLimit = 60;
        else if (speedLimit == 60) tempSpeedLimit = 50;
        else if (speedLimit == 70) tempSpeedLimit = 40;
        else if (speedLimit == 80) tempSpeedLimit = 30;
        else if (speedLimit == 90) tempSpeedLimit = 20;
        else if (speedLimit == 100) tempSpeedLimit = 10;
    }
    
    public boolean hasIntersection(Intersection intersection)
    {
        if (node1.equals(intersection))          { return true;  }
        else if (node2 == null)                  { return false; }
        else if (node2.equals(intersection))     { return true;  }
        else                                     { return false; }
    }

    /**
     * Builds and returns a textual representation of this road.
     */
    @Override
    public String toString()
    {
        return node1 + " - " + node2 + "(" + speedLimit + ":" + oneWay + ")";
    }
}
