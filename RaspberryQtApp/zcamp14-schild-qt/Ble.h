#ifndef ZCAMP14_SCHILD_RASPI_BLE_H__
#define ZCAMP14_SCHILD_RASPI_BLE_H__


#include <QObject>


//! Represents the Bluetooth Low Energy receiver/transmitter.
class Ble : public QObject {
    Q_OBJECT

  public:
    Ble();

  signals:
    void roomNameUpdate(QString roomName);
    void occupantNamesInvalidated();
    void occupantNameUpdate(QString occupantName);

  private slots:
    void processTimer();

  private:
    int counter_;
    int roomname_;
};


#endif   // ZCAMP14_SCHILD_RASPI_BLE_H__
