package com.Khushi.recommendation_backend.util;

public class CacheMetrics {
    private static int cacheHits=0;
    private static int cacheMisses=0;
    public static synchronized void recordHit(){
        cacheHits++;
    }
    public static synchronized void recordMiss(){
        cacheMisses++;
    }
    public static int getHits(){
        return cacheHits;
    }
    public static int getMisses(){
        return cacheMisses;
    }
    public static double getHitRate(){
        int total=cacheHits+cacheMisses;
        return total==0?0:(cacheHits*100.0/total);
    }
}
