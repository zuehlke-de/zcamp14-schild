#ifndef UPDATE_H
#define UPDATE_H

#include <QApplication>
#include <QStringList>

class Update : public QObject
{
    Q_OBJECT

public:
    Update();

public slots:
    void ReceiveRoomName(QString roomName);
    void ClearOccupantNames();
    void ReceiveOccupantName(QString occupantName);

public:
    QString getRoomName();
    QStringList getOccupantNames();

private:
    QString m_roomName;
    QStringList m_occupantNames;
};


#endif // UPDATE_H
