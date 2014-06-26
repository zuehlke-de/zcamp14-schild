cd ~/zcamp14-schild
git pull --no-edit origin master
cd RaspberryQtApp/coupling_test
qmake coupling.pro
make
./coupling &
