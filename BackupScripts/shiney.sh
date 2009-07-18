#!/bin/sh

(cd / && exec rsync -av --delete --progress "Users" /Volumes/Toshiba/Shiney/) || exit 1

echo "Backups complete."

