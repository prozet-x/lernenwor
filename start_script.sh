#!/bin/sh

gradle diffChangelog
gradle update
gradle run