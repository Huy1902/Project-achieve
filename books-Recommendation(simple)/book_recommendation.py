import joblib
import pandas as pd
import sklearn


df = pd.read_csv('dataframe.csv', index_col=0)
model = joblib.load('model.pkl')

def get_recommends(title=""):
    if title not in df.index:
        return []

    book = df.loc[title]
    distance, indice = model.kneighbors([book.values], n_neighbors=6)

    recommended_books = pd.DataFrame({
        'title': df.iloc[indice[0]].index.values,
        'distance': distance[0]
    })
    recommended_books = recommended_books.sort_values(by='distance', ascending=False)
    # Skip the book itself
    return recommended_books['title'].iloc[0:5].values