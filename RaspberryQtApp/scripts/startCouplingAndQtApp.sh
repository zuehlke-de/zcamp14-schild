~/zcamp14-schild/RaspberryQtApp/scripts/killProcesses.sh
kill -9 $(ps aux | grep 'startx' | awk '{print $2}')
rm /tmp/ble_coupling
~/zcamp14-schild/RaspberryQtApp/scripts/makeQtApp.sh
~/zcamp14-schild/RaspberryQtApp/scripts/runQtApp.sh > /dev/null 2>&1 &
sleep 5
~/zcamp14-schild/RaspberryQtApp/scripts/makeAndRunCoupling.sh > /dev/null 2>&1 &
