#include "update.h"
#include <QApplication>

Update::Update()
{
    m_roomName = "";
}

void Update::ReceiveRoomName(QString roomName) {
    m_roomName = roomName;
}

void Update::ClearOccupantNames() {
    m_occupantNames.clear();
}

void Update::ReceiveOccupantName(QString occupantName) {
    m_occupantNames.append(occupantName);
}

QString Update::getRoomName() {
    return m_roomName;
}

QStringList Update::getOccupantNames() {
    return m_occupantNames;
}
