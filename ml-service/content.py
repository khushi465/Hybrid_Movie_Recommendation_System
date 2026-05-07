# tf-idf+user interactions
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import pandas as pd

def compute_content_scores(movies, user_interactions):
    if not movies or not user_interactions:
        return {}
    df=pd.DataFrame(movies)

    # Fill missing
    df['genre']=df['genre'].fillna('')
    df['genre'] = df['genre'].str.lower()

    # TF-IDF on genres
    tfidf=TfidfVectorizer(token_pattern=r"[^|]+")
    tfidf_matrix=tfidf.fit_transform(df['genre'])

    cosine_sim=cosine_similarity(tfidf_matrix,tfidf_matrix)

    movie_index={row['id']:idx for idx, row in df.iterrows()}

    scores={m['id']:0 for m in movies}

    watched = set(i.get("movieId") for i in user_interactions if i.get("movieId") is not None)

    #Only user's watched movies
    for mid in watched:
        if mid not in movie_index:
            continue
        idx=movie_index[mid]
        for other_id, other_idx in movie_index.items():
            scores[other_id]+=cosine_sim[idx][other_idx]
            
    if not scores:
        return {}

    max_score = max(scores.values())

    if max_score == 0:
        return scores
    scores = {k: v / max_score for k, v in scores.items()}

    return scores