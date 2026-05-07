# all interactions
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity

def compute_cf_scores(interactions, target_user):
    if not interactions:
        return {}
    df=pd.DataFrame(interactions)
    if "score" not in df.columns:
        df["score"] = 0
    else:
        df["score"] = df["score"].fillna(0)
    # User-item matrix
    pivot=df.pivot_table(
        index='userId',
        columns='movieId',
        values='score',
        fill_value=0
    )

    if target_user not in pivot.index:
        return {}
    
    #Cosine similarity bw users
    similarity=cosine_similarity(pivot)

    sim_df=pd.DataFrame(similarity, index=pivot.index, columns=pivot.index)

    user_sim=sim_df[target_user].sort_values(ascending=False)
    scores={}

    # Top similar users(exclude self)
    for other_user, sim in user_sim.iloc[1:6].items():
        movies=pivot.loc[other_user]
        for movie_id, rating in movies.items():
            if rating>0:
                scores[movie_id]=scores.get(movie_id, 0)+sim*rating
    if not scores:
        return {}
    max_score = max(scores.values()) 
    if max_score==0:
        return scores #avoid division
    scores = {k: v / max_score for k, v in scores.items()}
    return scores