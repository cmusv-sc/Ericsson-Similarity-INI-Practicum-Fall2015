#!/usr/bin/env python

#
# File  :   LibraryDependency.py
# Author:   Dennis Liang
# Date  :   10/01/2015
#


import subprocess
import os

def main():
	# Prompt
    print "[INFO] Enter your Admin password: \n"

	# install prerequisite library
    subprocess.call(['curl https://bootstrap.pypa.io/ez_setup.py -o - | sudo python'], shell = True)

    
    # Installation
    subprocess.call(['sudo easy_install pip'], shell = True)
    subprocess.call(['pip install pandas'], shell = True)
    subprocess.call(['pip install tmdbsimple'], shell = True)

if __name__ == '__main__':
	main()