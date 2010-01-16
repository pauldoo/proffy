#!/bin/sh

git describe --long --always | sed 's/^/"/' | sed 's/$/"/'

