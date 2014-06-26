cd ~/zcamp14-schild
git pull --no-edit origin master
cd RaspberryQtApp/zcamp14-schild-qt
qmake zcamp14-schild-qt.pro
make
startx ./zcamp14-schild-qt &
