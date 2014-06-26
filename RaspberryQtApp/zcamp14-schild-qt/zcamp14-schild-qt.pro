# Additional import path used to resolve QML modules in Creator's code model
QML_IMPORT_PATH =

HEADERS += update.h \
           Ble.h \
    occupantlistmodel.h \
    roomname.h

# The .cpp file which was generated for your project. Feel free to hack it.
SOURCES += main.cpp \
           update.cpp \
           Ble.cpp \
    occupantlistmodel.cpp \
    roomname.cpp

RESOURCES += qml.qrc

# Installation path
# target.path =

# Please do not modify the following two lines. Required for deployment.
include(qtquick1applicationviewer/qtquick1applicationviewer.pri)

# Default rules for deployment.
include(deployment.pri)


OTHER_FILES +=

CONFIG += console

QT += network
