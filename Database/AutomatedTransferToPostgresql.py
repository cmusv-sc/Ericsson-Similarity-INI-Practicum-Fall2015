#!/usr/bin/env python

#
# File  :   Transfer to Postgresql
# Author:   Dennis Liang
# Date  :   11/01/2015
#


import sys
import subprocess

def main():

	# Setting working directory with prompting input from users
    print "[INFO] Data Transfer Automation start"
    print "[WARNING] Make sure your Postgresql database service open"
    print "[INFO]Transfer objects to database.................."

    # Hardcoding: database IP: 54.218.101.198
    # Hardcoding: database port: 5432
    # Hardcoding: transfer type: json
    # Hardcoding: transfer filename: json test.json
    subprocess.call(['chmod +x pgfutter'], shell = True)
    subprocess.call(['./pgfutter --db "postgres" --host 54.218.101.198 --port 5432 --username postgres --pass a123456 json test.json'], shell = True)



if __name__ == '__main__':
	main()