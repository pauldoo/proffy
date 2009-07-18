#!/bin/sh

(cd /mnt/shared && rsync -av --delete --progress "Movies" /media/Toshiba/Arthur/) || exit 1
(cd /mnt/shared && rsync -av --delete --progress "Music" /media/Toshiba/Arthur/) || exit 1
(cd /mnt/vista && rsync -av --delete --progress "Users" /media/Toshiba/Arthur/) || exit 1
(cd /mnt/xp && rsync -av --delete --progress "Documents and Settings" /media/Toshiba/Arthur/) || exit 1

echo "Backups complete."

