kill -9 $(ps aux | grep 'coupling' | awk '{print $2}')
kill -9 $(ps aux | grep 'startx' | awk '{print $2}')
rm /tmp/ble_coupling
~/zcamp14-schild/RaspberryQtApp/scripts/makeAndRunQtApp.sh &
~/zcamp14-schild/RaspberryQtApp/scripts/makeAndRunCoupling.sh &
