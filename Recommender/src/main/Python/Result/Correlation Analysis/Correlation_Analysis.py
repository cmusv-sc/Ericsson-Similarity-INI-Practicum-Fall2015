#!/usr/bin/env python


import pandas


def grouping(dataframe):

	columns = dataframe.columns

	merged = pandas.merge(pandas.DataFrame(dataframe['s1'].value_counts()), pandas.DataFrame(dataframe['s2'].value_counts()), how = 'outer', left_index = True, right_index = True)

	for i in range(3, len(columns)):
		merged = pandas.merge(merged, pandas.DataFrame(dataframe['s' + str(i)].value_counts()), how = 'outer', left_index = True, right_index = True)		

	return merged

def counting(dataframe):

	count = dataframe['s1']

	for i in range(2, len(dataframe.columns) + 1):
		count = count + dataframe['s' + str(i)]

	return count


def popularity_over_similarity():

	# Popularity
	Popularity = pandas.read_csv("./Popularity/MovieId_Popularity.csv")

	# Cosine
	Cosine = pandas.read_csv("./Misc/Cosine_Counting_Similarity.csv", header = True)
	Cosine.columns = ['movieId', 'Similarity Votes']

	Cosine_distr = pandas.merge(Popularity, Cosine, how = 'left', on = 'movieId')
	Cosine_distr['rating'] = Cosine_distr['rating'].fillna(0)
	Cosine_distr['Similarity Votes'] = Cosine_distr['Similarity Votes'].fillna(0)

	Cosine_distr = Cosine_distr[Cosine_distr['rating'] > 5]

	Cosine_distr_group = Cosine_distr.groupby('Similarity Votes')
	Cosine_cumul = Cosine_distr_group.sum()

	Cosine_cumul = Cosine_cumul.drop('movieId', axis = 1)

	Cosine_cumul.to_csv("./Similarity/Cosine_distr.csv")

	# Pearson
	Pearson = pandas.read_csv("./Misc/Pearson_Counting_Similarity.csv")
	Pearson.columns = ['movieId', 'Similarity Votes']

	Pearson_distr = pandas.merge(Popularity, Pearson, how = 'left', on = 'movieId')
	Pearson_distr['rating'] = Pearson_distr['rating'].fillna(0)
	Pearson_distr['Similarity Votes'] = Pearson_distr['Similarity Votes'].fillna(0)

	Pearson_distr = Pearson_distr[Pearson_distr['rating'] > 5]

	Pearson_distr_group = Pearson_distr.groupby('Similarity Votes')
	Pearson_cumul = Pearson_distr_group.sum()

	Pearson_cumul = Pearson_cumul.drop('movieId', axis = 1)

	Pearson_cumul.to_csv("./Similarity/Pearson_distr.csv")

	# Content All fields
	Content_allfields = pandas.read_csv("./Misc/Content_ALLFIELD_Counting_Similarity.csv")
	Content_allfields.columns = ['movieId', 'Similarity Votes']

	Content_allfields_distr = pandas.merge(Popularity, Content_allfields, how = 'left', on = 'movieId')
	Content_allfields_distr['rating'] = Content_allfields_distr['rating'].fillna(0)
	Content_allfields_distr['Similarity Votes'] = Content_allfields_distr['Similarity Votes'].fillna(0)

	Content_allfields_distr = Content_allfields_distr[Content_allfields_distr['rating'] > 5]

	Content_allfields_distr_group = Content_allfields_distr.groupby('Similarity Votes')
	Content_allfields_distr_cumul = Content_allfields_distr_group.sum()

	Content_allfields_distr_cumul = Content_allfields_distr_cumul.drop('movieId', axis = 1)

	Content_allfields_distr_cumul.to_csv("Content_ALLFIELDS_distr.csv")

	# User Evaluation
	dfu = pandas.read_csv("./Misc/User_Evaluation.csv")
	dfu.head()
	dfugroupby = dfu.groupby('username')
	dfugroupby.head()
	dfugroupby = dfu.groupby('username', 'movieid2')
	dfugroupby = dfu.groupby(['username', 'movieid2'])
	dfugroupby.head()
	dfugroupby.sum()
	dfugroupby = dfu.groupby(['username', 'movieid1'])
	dfugroupby.sum()
	dfugroupsum = dfugroupby.sum()
	dfugroupsum
	dfugroupsum.info
	dfugroupsum.columns
	dfugroupsum.count()
	dfugroupsum.describe()
	dfugroupsum.to_csv("./Misc/Number_Of_Rating_Per_Evaluation_Per_User.csv")
	dfugroupnow = pandas.read_csv("./Misc/Number_Of_Rating_Per_Evaluation_Per_User.csv")
	dfugroupnow.head()
	dfugroupnow['rating'].sum()
	dfugroupnow['rating'].sum() / dfugroupnow.count()
	dfugroupnow.tail()
	dfugroupnow
	dfugroupnow['rating'].max()
	rating = dfugroupnow['rating']
	rating.std
	rating.std()

	# Revenue
	TMDB_Distr = pandas.read_csv("./Misc/TMDB_Counting_Similarity.csv")
	TMDB_Distr = TMDB_Distr[['movieId', 'revenue']]
	TMDB_Distr = TMDB_Distr[TMDB_Distr.revenue.map(str.isdigit)]

	User_Rating = pandas.read_csv("./Misc/Popularity_Similarity_ratingis1.csv")
	User_Rating.columns = ['movieId', 'rating', 'popularity']

	TMDB_Distr_merge = pandas.merge(User_Rating, TMDB_Distr, how = 'left', on = 'movieId')
	TMDB_Distr_merge['revenue'] = TMDB_Distr_merge['revenue'].fillna(0)
	TMDB_Distr_merge = TMDB_Distr_merge[[ 'rating', 'revenue']]

	TMDB_Distr_merge.to_csv("./Similarity/User_distr.csv")

	TMDB_Distr_group = TMDB_Distr_merge.groupby('rating')
	TMDB_cumul = TMDB_Distr_group.sum()
	TMDB_cumul = TMDB_cumul[TMDB_cumul.index != 0]
	TMDB_cumul = TMDB_cumul['revenue'].fillna(0)
	TMDB_cumul = TMDB_cumul[TMDB_cumul['revenue'] != 0]

	TMDB_cumul.to_csv("./Similarity/TMDB_distr.csv")


def main():

	# COSINE
	cosine = pandas.read_csv("./Misc/Recommended_COSINE_ALLFIELD_Similarity.csv")
	cosine.columns = ['movieid', 's1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0','s0']
	cosine = cosine.drop(['s0'], axis = 1)
	cosine.to_csv("./Misc/RECOMMENDED_COSINE_ALLFIELD.csv")

	cosine.columns = ['movieid', 's1', 's2', 's3', 's4', 's5', 's6', 's7', 's8', 's9', 's10', 's11', 's12', 's13', 's14', 's15', 's16', 's17', 's18', 's19', 's20']
	cosine = cosine.set_index(['movieid'])
	cosine_grouping = grouping(cosine)

	cosine_grouping = cosine_grouping.fillna(0)

	cosine_grouping.columns = ['s1', 's2', 's3', 's4', 's5', 's6', 's7', 's8', 's9', 's10', 's11', 's12', 's13', 's14', 's15', 's16', 's17', 's18', 's19']
	cosine_grouping.index.set_names('movieid')

	cosine_grouping = counting(cosine_grouping)

	cosine_grouping.index.set_names("movieid")

	cosine_grouping.to_csv("./Misc/Cosine_Counting_Similarity.csv", header = True)

	# Pearson
	pearson = pandas.read_csv("./Misc/Recommended_Pearson_Similarity.csv")
	pearson.columns = ['movieid', 's1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0','s0']
	pearson = pearson.drop(['s0'], axis = 1)
	pearson.to_csv("./Misc/RECOMMENDED_PEARSON.csv")

	pearson.columns = ['movieid', 's1', 's2', 's3', 's4', 's5', 's6', 's7', 's8', 's9', 's10', 's11', 's12', 's13', 's14', 's15', 's16', 's17', 's18', 's19', 's20']
	pearson = pearson.set_index(['movieid'])
	pearson_grouping = grouping(pearson)

	pearson_grouping = pearson_grouping.fillna(0)

	pearson_grouping.columns = ['s1', 's2', 's3', 's4', 's5', 's6', 's7', 's8', 's9', 's10', 's11', 's12', 's13', 's14', 's15', 's16', 's17', 's18', 's19']
	pearson_grouping.index.set_names('movieid')

	pearson_grouping = counting(pearson_grouping)

	pearson_grouping.to_csv("./Misc/Pearson_Counting_Similarity.csv")

	# Content Filtering
	content_allfields = pandas.read_csv("./Misc/Recommended_CONTENT_ALLFIELDS_Similarity.csv")
	content_allfields.columns = ['movieid', 's1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0', 's1', 's0','s1', 's0', 's1', 's0','s0']
	content_allfields = content_allfields.drop(['s0'], axis = 1)
	content_allfields.to_csv("./Misc/RECOMMENDED_CONTENT_ALLFIELD.csv")

	content_allfields.columns = ['movieid', 's1', 's2', 's3', 's4', 's5', 's6', 's7', 's8', 's9', 's10', 's11', 's12', 's13', 's14', 's15', 's16', 's17', 's18', 's19', 's20']
	content_allfields = content_allfields.set_index(['movieid'])
	content_allfields_grouping = grouping(content_allfields)

	content_allfields_grouping = content_allfields_grouping.fillna(0)

	content_allfields_grouping.columns = ['s1', 's2', 's3', 's4', 's5', 's6', 's7', 's8', 's9', 's10', 's11', 's12', 's13', 's14', 's15', 's16', 's17', 's18', 's19']
	content_allfields_grouping.index.set_names('movieid')

	content_allfields_grouping = counting(content_allfields_grouping)

	content_allfields_grouping.to_csv("./Similarity/Content_ALLFIELD_Counting_Similarity.csv")

	# TMDB Revenue
	TMDB = pandas.read_csv("./Popularity/TMDB_Popularity_Revenue.csv")
	TMDB_Votes = TMDB[['tmdbid', 'title', 'revenue']]
	TMDB_Votes.columns = ['id', 'title', 'revenue']

	links = pandas.read_csv("links.csv")
	links.columns = ['movieId', 'imdbId', 'id']

	TMDB_withID = pandas.merge(TMDB_Votes, links, how = 'left', on = 'id', left_index = True)

	TMDB_withID.to_csv("./Misc/TMDB_Counting_Similarity.csv")

	# TMDB Similarity
	TMDB_Similarity = pandas.read_csv("./Misc/Recommended_TMDB_Similarity.csv")
	TMDB_Similarity = TMDB_Similarity[['tmdbid', '?column?', '?column?.1', '?column?.2', '?column?.3', '?column?.4','?column?.5', '?column?.6', '?column?.7', '?column?.8', '?column?.9', '?column?.10', '?column?.11', '?column?.12', '?column?.13', '?column?.14','?column?.15', '?column?.16', '?column?.17', '?column?.18', '?column?.19']]
	TMDB_Similarity.columns = ['tmdbid', 's1', 's2', 's3', 's4', 's5', 's6', 's7', 's8', 's9', 's10', 's11', 's12', 's13', 's14', 's15', 's16', 's17', 's18', 's19', 's20']
	TMDB_Similarity.to_csv("./Misc/RECOMMENDED_TMDB_Similarity_Final.csv")

if __name__ == '__main__':
	main()