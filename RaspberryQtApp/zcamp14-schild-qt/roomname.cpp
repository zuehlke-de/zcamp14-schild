#include "roomname.h"

#include <iostream>
#include <QString>
#include <QObject>
#include <QDeclarativeContext>
#include <typeinfo>
#include <QTextEdit>

void Roomname::ReceiveRoomName(QString roomName) {
    parent()->setProperty("roomNameText",roomName);
}
