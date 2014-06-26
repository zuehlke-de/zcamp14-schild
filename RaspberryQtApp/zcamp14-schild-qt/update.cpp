#include "update.h"
#include <QApplication>

#include <iostream>


Update::Update()
{
    m_roomName = "";
}

void Update::ReceiveRoomName(QString roomName) {
    std::cout << "Update::ReceiveRoomName()" << std::endl;
    m_roomName = roomName;
}

void Update::ClearOccupantNames() {
    std::cout << "Update::ClearOccupantNames()" << std::endl;
    m_occupantNames.clear();
}

void Update::ReceiveOccupantName(QString occupantName) {
    std::cout << "Update::ReceiveOccupantName(): " << occupantName.toUtf8().constData() << std::endl;
    m_occupantNames.append(occupantName);
}

QString Update::getRoomName() {
    return m_roomName;
}

QStringList Update::getOccupantNames() {
    return m_occupantNames;
}
