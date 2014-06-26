#ifndef ZCAMP14_SCHILD_RASPI_BLE_H__
#define ZCAMP14_SCHILD_RASPI_BLE_H__


#include <QObject>

#include <vector>
#include <string>


class QLocalServer;
class QLocalSocket;


//! Represents the Bluetooth Low Energy receiver/transmitter.
class Ble : public QObject {
    Q_OBJECT

  public:
    enum Opcode { UpdateRoomName = 0,
                  UpdateOccupantName,
                  ClearOccupantNames  };

    Ble();
    ~Ble();

  signals:
    void roomNameUpdate(QString roomName);
    void occupantNamesInvalidated();
    void occupantNameUpdate(QString occupantName);

  private slots:
    void handleNewConnection();
    void handleReadyRead();
    void processTimer();

  private:
    int processCommand();
    int processUpdateRoomName();
    int processUpdateOccupantName();
    int processClearOccupantNames();
    bool ExtractName(std::string *name);

    QLocalServer      *localServer_;
    QLocalSocket      *currentSocket_;
    std::vector<char> buffer_;
    int               bufferFill_;
    int               counter_;
};


#endif   // ZCAMP14_SCHILD_RASPI_BLE_H__
