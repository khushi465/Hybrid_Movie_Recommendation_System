package com.Khushi.recommendation_backend.util;

import java.util.*;

public class LatencyTracker {
    private static final List<Long> latencies=new ArrayList<>();
    public static synchronized  void record(long latency){
        latencies.add(latency);
    }
    public static double getP50(){
        return percentile(50);
    }
    public static double getP95(){
        return percentile(95);
    }
    private static double percentile(int p){
        if(latencies.isEmpty()) return 0;
            List<Long> sorted=new ArrayList<>(latencies);
            Collections.sort(sorted);
            int index=(int) Math.ceil(p/100.0*sorted.size())-1;
            return sorted.get(Math.max(index,0));

    }
    public static int size(){
        return latencies.size();
    }
}
