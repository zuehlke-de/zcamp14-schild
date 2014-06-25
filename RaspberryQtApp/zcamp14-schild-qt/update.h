#ifndef UPDATE_H
#define UPDATE_H

#include <QApplication>

class Update
{
public:
    Update();

    void ReceiveRoomName(QString roomName);
    void ClearOccupantNames();
    void ReceiveOccupantName(QString occupantName);

    QString getRoomName();
    QStringList getOccupantNames();

private:
    QString m_roomName;
    QStringList m_occupantNames;
};


#endif // UPDATE_H
