package metrics;

public class Metrics {
    private long findCalls = 0;
    private long unionCalls = 0;
    private long edgeScans = 0;
    private long comparisons =0;
    private int maxDepth = 0;

    public void incFind() { findCalls++; }
    public void incUnion() { unionCalls++; }
    public void incEdgeScans() { edgeScans++;}
    public void incComparisons() { comparisons++; }

    public void updateMaxDepth(int currentDepth) {
        if (currentDepth >  maxDepth) {
            maxDepth = currentDepth;
        }
    }

    public long getFindCalls() { return findCalls; }
    public long getUnionCalls() { return unionCalls; }
    public long getDsuOps() { return findCalls + unionCalls; }
    public long getEdgeScans() { return edgeScans; }
    public long getComparisons() { return comparisons; }
    public int getMaxDepth() { return maxDepth; }

    public void reset(){
        findCalls = 0;
        unionCalls = 0;
        edgeScans = 0;
        comparisons = 0;
        maxDepth = 0;
    }


}
