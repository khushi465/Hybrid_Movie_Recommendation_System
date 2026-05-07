from fastapi import FastAPI
from hybrid import compute_hybrid_scores
import logging
from hybrid import compute_popularity



logger=logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO, force=True)
app=FastAPI()

@app.post("/recommend")
def recommend(data:dict):
    user_id=data["userId"]
    movies=data["movies"]
    interactions=data.get("interactions",[])

    if not movies:
        return []
    # print("Processing recommendations for user ",user_id)
    logger.info(f"Processing recommendations for user {user_id}")
    scores=compute_hybrid_scores(movies, interactions, user_id)

    #watched set
    watched = set(
    i.get("movieId") for i in interactions
    if i.get("userId") == user_id and i.get("movieId") is not None
)

    #detect edge case
    all_watched=len(watched)>=len(movies)

    result=[]
    
    for m in movies:
        mid=m.get("id")
        if mid is None:
            continue

        if not all_watched and mid in watched:
            continue
        result.append({
            "movieId":int(mid),
            "score":float(scores.get(mid, 0))
        })
    #ffallback if empty
    popularity=compute_popularity(interactions)
    if not result:
        result = sorted(
            [{"movieId": m["id"], "score": popularity.get(m["id"], 0)} for m in movies],
            key=lambda x: x["score"],
            reverse=True
)
    
    result.sort(key=lambda x:x["score"], reverse=True)
    return result[:20]
