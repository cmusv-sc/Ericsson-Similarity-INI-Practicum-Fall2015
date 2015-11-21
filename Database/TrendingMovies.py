#!/usr/bin/env python

#
# File  :   TrendingMovies.py
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
    print "[INFO] Enter your working directory full path: "
    
    pathFromUser = sys.stdin.readline()

    print "Your working directory is: ", pathFromUser
    print "Retrieving objects from themoviedb.org.................."

    if pathFromUser != "\n":
        pathFromUser = './'
    
    os.chdir(pathFromUser.lstrip())

    # Connecting to themoviedb.org with API keys: 0e15eb439909c2e6a7ebbac73e049d34
    tmdb.API_KEY = '0e15eb439909c2e6a7ebbac73e049d34'

    
    try:
        # FileName = rawinput("Enter your file name: ")
        with open('TrendingMovies.json', 'w') as jsonfile:
                jsonfile.close()
    except Exception: 
        print "[Error] File creation failure. "
    

    # Serialize all columns into dictionary if it isn't in its original format
    movie           = tmdb.Movies()
    jsonfiletype    = ""
        
    # Latest Movies
    jsonfiletype = "latest"

    try:
        movieInfo = movie.latest()
        movieInfo.update({'type': jsonfiletype})
        movieInfo.pop("belongs_to_collection", None)
    except Exception:
        print ("[ERROR] Retrieving latestest movie")
    
    # Writing movie to JSON file
    try:
        with open('TrendingMovies.json', 'a') as jsonfile:
            json.dump(movieInfo, jsonfile, encoding = 'UTF-8', sort_keys = True, separators=(',', ': '))
            jsonfile.write("\n")
            print ("[INFO] Retrieving latestest movie, Processing movie:"), movie.info()['title']
    except Exception: 
        print "[ERROR] Create File Failed "

    # Upcoming Movies
    for i in range(len(movie.upcoming()['results'])):
    
        jsonfiletype = "upcoming"

        try:
            movieInfo = movie.upcoming()['results'][i]
            movieInfo.update({'type': jsonfiletype})
            movieInfo.pop("belongs_to_collection", None)
        except Exception:
            print ("[ERROR] Retrieving upcoming movie")
            continue
        
        i = i + 1

        # Writing movie to JSON file
        try:
            with open('TrendingMovies.json', 'a') as jsonfile:
                json.dump(movieInfo, jsonfile, encoding = 'UTF-8', sort_keys = True, separators=(',', ': '))
                jsonfile.write("\n")
                print ("[INFO] Retrieving upcoming movie, Processing movie:"), movie.upcoming()['results'][i]['title']
        except Exception: 
            continue

    # Top rated Movies
    for i in range(len(movie.top_rated()['results'])):
    
        jsonfiletype = "top rated"

        try:
            movieInfo = movie.top_rated()['results'][i]
            movieInfo.update({'type': jsonfiletype})
            movieInfo.pop("belongs_to_collection", None)
        except Exception:
            print ("[ERROR] Retrieving top rated movie")
            continue
        
        i = i + 1

        # Writing movie to JSON file
        try:
            with open('TrendingMovies.json', 'a') as jsonfile:
                json.dump(movieInfo, jsonfile, encoding = 'UTF-8', sort_keys = True, separators=(',', ': '))
                jsonfile.write("\n")
                print ("[INFO] Retrieving top rated movie, Processing movie:"), movie.top_rated()['results'][i]['title']
        except Exception: 
            continue

    # Now Playing
    for i in range(len(movie.now_playing()['results'])):
    
        jsonfiletype = "now playing"

        try:
            movieInfo = movie.top_rated()['results'][i]
            movieInfo.update({'type': jsonfiletype})
            movieInfo.pop("belongs_to_collection", None)
        except Exception:
            print ("[ERROR] Retrieving now playing movie")
            continue
        
        i = i + 1

        # Writing movie to JSON file
        try:
            with open('TrendingMovies.json', 'a') as jsonfile:
                json.dump(movieInfo, jsonfile, encoding = 'UTF-8', sort_keys = True, separators=(',', ': '))
                jsonfile.write("\n")
                print ("[INFO] Retrieving now playing movie, Processing movie:"), movie.now_playing()['results'][i]['title']
        except Exception: 
            continue


if __name__ == '__main__':
    main()