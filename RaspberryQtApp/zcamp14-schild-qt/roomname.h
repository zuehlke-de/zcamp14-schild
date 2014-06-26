#ifndef ROOMNAME_H
#define ROOMNAME_H

#include <QString>
#include <QObject>
class Roomname : public QObject
{
    Q_OBJECT

public:
    Roomname(QObject *parent = 0) : QObject(parent) { }

public slots:
    void ReceiveRoomName(QString roomName);

};

#endif // ROOMNAME_H
