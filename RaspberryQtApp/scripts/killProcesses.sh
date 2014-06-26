kill -9 $(ps aux | grep 'coupling' | awk '{print $2}')
kill -9 $(ps aux | grep 'startx' | awk '{print $2}')
