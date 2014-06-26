cd ~/zcamp14-schild
cd RaspberryQtApp/coupling_test
qmake coupling.pro
make
./coupling > /dev/null 2>&1 &
