#!/bin/sh

gradle diffChangelog
gradle update
gradle bootRun -Penv=prod