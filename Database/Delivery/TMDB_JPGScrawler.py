
""" Author: Dennis, Kun-Lin
    Created: 11/06/2015
    Function Description: Downloading the posters JPG from IMDB for all 20 millions movielens
"""

import urllib2
import pandas
import json

def main():

    # Readin to a python dictionary
    print "[INFO] Python script for JPG Start........"
    print "[INFO] Read in TMDB JPG PATH Mapping"

    Mapping = pandas.read_csv("OMDB_JPG_EMPTY_MOVIE_LIST.csv")

    # Loading TMDB entries
    print ("[INFO] Loading JSON files")

    Tmdb_JSON_file = []
    with open('TMDBTotal.json', 'r') as jsonfile:
        for line in jsonfile:
            Tmdb_JSON_file.append(json.loads(line, encoding = 'UTF-8'))
        

    TMDB_Poster_Prefix  = "https://image.tmdb.org/t/p/original"
    Tmdb_Mapping        = pandas.read_csv("links.csv", header = False)  
    Tmdb_Mapping['tmdbId'] = Tmdb_Mapping['tmdbId'].fillna(1)

    # Open the folders, and files
    Folder_Path = "JPG_REPO_TMDB/"

    # Loop through all movies in MovieLEN

    for iteration in range(1, int(Mapping.count()['movielen_id'])):
        
        print "[INFO] Iteration: ", iteration

        movielens_id    = int(Mapping.ix[iteration]['movielen_id'])

        print Tmdb_Mapping.columns

        tmdb_id = Tmdb_Mapping[Tmdb_Mapping['movieId'] == movielens_id]['tmdbId']

        # Retrieve Poster URL
        incomplete_poster_URL = ""

        for tmdb_record in Tmdb_JSON_file:

            if tmdb_record['id'] == int(tmdb_id):
                incomplete_poster_URL = tmdb_record['poster_path']
                break

        if incomplete_poster_URL is None:
            continue

        poster_URL  = TMDB_Poster_Prefix + incomplete_poster_URL

        filehandler = open(Folder_Path + str(movielens_id) + ".jpg", "wb")
        
        JPG_file = Download_Poster(movielens_id, poster_URL)

        if JPG_file:
            filehandler.write(JPG_file)
            filehandler.close()


def Download_Poster(movielens_id, poster_URL, num_retries = 3):

    print '[INFO] Downloading Poster for movielens id: ', movielens_id

    try:

        Poster_JPG = urllib2.urlopen(poster_URL).read()

    except urllib2.URLError as error:

        print "[ERROR]", error.reason
        print "Sleep for 2 seconds"

        #sleep(2)

        Poster_JPG = None

        if num_retries > 0:
            
            if hasattr(error, 'code') and 500 <= error.code < 600:
                print "[INFO]: Retrying to get Poster: ", poster_URL 
                return Download_Poster(poster_URL, num_retries-1)

        if num_retries == 0:

            print "[ERROR] Error Downloading: ", movielens_id
    
    return Poster_JPG


if __name__ == '__main__':
    main()