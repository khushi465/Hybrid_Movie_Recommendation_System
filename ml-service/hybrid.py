import time
from collections import defaultdict
from content import compute_content_scores
from  collaborative import compute_cf_scores

def compute_hybrid_scores(movies, interactions, user_id):
    if not movies:
        return {}
    # Split user interactions
    user_interactions=[i for i in interactions if i.get("userId")==user_id]

    #cold start fallback
    if not user_interactions:
        return dict(compute_popularity(interactions))
    
    #ML scores
    content_scores = normalize(compute_content_scores(movies, user_interactions))
    cf_scores = normalize(compute_cf_scores(interactions, user_id))


    #popularity
    popularity=normalize(compute_popularity(interactions))

    #recency
    recency=normalize(compute_recency(interactions))

    interaction_count=len(user_interactions)

    #Dynamic alpha
    if interaction_count<5:
        alpha=0.2
    elif interaction_count<20:
        alpha=0.5
    else:
        alpha=0.8
    
    final_scores={}

    for m in movies:
        mid=m["id"]
        cf=cf_scores.get(mid, 0)
        cb=content_scores.get(mid, 0)
        pop=popularity.get(mid, 0)
        rec=recency.get(mid, 0)
        print(f"Movie {mid} -> CF:{cf:.3f}, CB:{cb:.3f}, POP:{pop:.3f}, REC:{rec:.3f}")

        final_scores[mid]=alpha*cf+(1-alpha)*cb+0.15*pop+0.05*rec
        import random
        final_scores[mid]+=random.uniform(0,0.01)

    return final_scores

#-----------Helpers-------------
def compute_popularity(interactions):
    pop=defaultdict(int)
    for i in interactions:
        pop[i.get("movieId")]+=1
    return pop

def compute_recency(interactions):
    current_time=time.time()
    rec=defaultdict(float)

    for i in interactions:
        ts=i.get("timestamp", current_time)
        if isinstance(ts, list):
            import datetime
            if len(ts)>=7:
                ts[6]=int(ts[6]/1000)
            dt=datetime.datetime(*ts[:7])
            timestamp=dt.timestamp()
        elif isinstance(ts, (int, float)):
            timestamp=ts
        elif isinstance(ts, str) and ts.isdigit():
            timestamp = float(ts)
        else:
            timestamp=current_time

        delta = max(0, current_time - timestamp)
        score = 1 / (1 + delta/1000)
        rec[i.get("movieId")]+=score

    return rec

def normalize(scores):
    if not scores:
        return scores
    max_val=max(scores.values())
    if max_val==0:
        return scores
    return {k:v/max_val for k,v in scores.items()}