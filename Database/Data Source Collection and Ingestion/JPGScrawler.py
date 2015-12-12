
""" Author: Dennis, Kun-Lin
    Created: 11/06/2015
    Function Description: Downloading the posters JPG from IMDB for all 20 millions movielens
"""

import urllib2
import pandas
import sys
import os

LOG = open('IMDB Poster Logging.txt', "w")

def main():

    # Readin to a python dictionary
    print "[INFO] Python script for JPG Start........"
    print "[INFO] Read in IMDB JPG PATH Mapping"
    print "[INFO] Enter your working directory full path: "
    
    pathFromUser = sys.stdin.readline()

    #print "Your working directory is: ", pathFromUser
    print "Retrieving objects from IMDB.................."

    if pathFromUser != "\n":
        pathFromUser = './'
    
    os.chdir(pathFromUser.lstrip())

    Mapping = pandas.read_csv("IMDB_JPG_PATH.csv")

    # Open the folders, and files
    Folder_Path = "./JPG_REPO/"

    # Loop through all movies in MovieLEN

    for iteration in range(1, int(Mapping.count()['movielen_id'])):

        print "[INFO] Iteration: ", iteration

        movielens_id = str(Mapping.ix[iteration]['movielen_id'])
        poster_URL   = Mapping.ix[iteration]['imdb_poster_path']

        filehandler = open(Folder_Path + movielens_id + ".jpg", "wb")
        
        JPG_file = Download_Poster(movielens_id, poster_URL)

        if JPG_file:
            filehandler.write(JPG_file)
            filehandler.close()


def Download_Poster(movielens_id, poster_URL, num_retries = 3):

    print '[INFO] Downloading Poster for movielens id: ', movielens_id

    try:

        Poster_JPG = urllib2.urlopen(poster_URL).read()

    except urllib2.URLError as error:

        ERROR_CODE = "[ERROR] " + error.reason + " " + str(movielen_id)
        print ERROR_CODE
        LOG = open('IMDB Poster Logging.txt', "a")
        LOG.write(ERROR_CODE)
        LOG.write('\n')
        LOG.close()

        #sleep(2)

        Poster_JPG = None

        if num_retries > 0:
            
            if hasattr(error, 'code') and 500 <= error.code < 600:
                print "[INFO]: Retrying to get Poster: ", poster_URL 
                return Download_Poster(poster_URL, num_retries-1)

        if num_retries == 0:

            ERROR_CODE = "[ERROR] " + "Retry Timeout" + " " + str(movielen_id)
            print ERROR_CODE
            LOG = open('IMDB Poster Logging.txt', "a")
            LOG.write(ERROR_CODE)
            LOG.write('\n')
            LOG.close()
        
    return Poster_JPG


if __name__ == '__main__':
    main()