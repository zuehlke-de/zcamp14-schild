#ifndef UPDATE_H
#define UPDATE_H

#include <QApplication>

class Update
{
public:
    Update();

    void ReceiveRoomName(QString roomName);
    void ClearOccupantNames();
    void ReceiveOccupantName(QString roomName);
};

#endif // UPDATE_H
