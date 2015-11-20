#!/usr/bin/env python

#
# File  :   TmdbCrawler.py
# Author:   Dennis Liang
# Date  :   10/01/2015
#

# Prerequisite installation: tmdbsimple, pandas
# run program in bash ./libraryDependency.py


import tmdbsimple as tmdb
import json as json
import pandas as panda
import os
import sys
import time

def main():

    # Setting working directory with prompting input from users
    print "[INFO] TMDB data retriever engine start"
    print "[WARNING] Make sure TmdbID.csv in your working directory "
    print "[INFO] Enter your working directory full path: "
    
    pathFromUser = sys.stdin.readline()

    print "Your working directory is: ", pathFromUser
    print "Retrieving objects from themoviedb.org.................."

    if pathFromUser != "\n":
        pathFromUser = './'
    
    os.chdir(pathFromUser.lstrip())

    # Retrieving Movies Tmdb ID
    TmdbID = panda.read_csv("links.csv", header = False)
    TmdbID = TmdbID['tmdbId'].fillna(1)

    # Connecting to themoviedb.org with API keys: 0e15eb439909c2e6a7ebbac73e049d34
    tmdb.API_KEY = '0e15eb439909c2e6a7ebbac73e049d34'

    
    try:
        # FileName = rawinput("Enter your file name: ")
        with open('TMDBMovies.json', 'w') as jsonfile:
                jsonfile.close()
    except Exception: 
        print "[Error] File creation failure. "
    

    # Loop through the records in the TMDBLink
    iterat = 0
    for iterator in TmdbID:

        iterat = iterat + 1
        print "iterator: ", iterat

        #if iterat <= 8415:
        #    continue

        # build dataframe
        if panda.isnull(iterator):
            continue

        # Serialize all columns into dictionary if it isn't in its original format
        movie = tmdb.Movies(int(iterator))
        
        # Merging dictionaries
        try:
            movieInfo = movie.info().copy()
            movieInfo.pop("belongs_to_collection", None)

        except Exception:
            print ("[ERROR] Retrieving movie with tmdbID: "), int(iterator)
            continue

        for i in range(len(movie.similar_movies()['results'])):
            movieInfo.update({('similar_movies top %d' % i) : movie.similar_movies()['results'][i]['id']})

        # Fixing Encoding issue for themoviedb.org
        # movieInfo = encodingissuefix(movieInfo)
 
        # Writing movie to JSON file
        try:
            with open('TMDBMovies.json', 'a') as jsonfile:
                json.dump(movieInfo, jsonfile, encoding = 'UTF-8', sort_keys = True, separators=(',', ': '))
                jsonfile.write("\n")
                print ("[INFO] Writing info in JSON format, Processing movie:"), movie.info()['title']
        except Exception: 
            continue

        # Conforming retriving policy: Sleep for 1 seconds per 50 records 
        if iterat == 50:
            iterat = 0
            time.sleep(1)


def encodingissuefix(movieInfo):

    for key, value in movieInfo.iteritems():

        movieInfo.pop(key, None)
        key = key.encode('UTF-8')
        movieInfo[key] = value

        if isinstance(value, unicode) or isinstance(value, str):
            value = value.encode('UTF-8')
            movieInfo.pop(key, None)
            movieInfo[key] = value
            
    return movieInfo


if __name__ == '__main__':
    main()