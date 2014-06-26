#ifndef ZCAMP14_SCHILD_RASPI_COUPLINGTEST_H__
#define ZCAMP14_SCHILD_RASPI_COUPLINGTEST_H__


#include <QObject>

class QLocalSocket;
class QTimer;


class CouplingTest : public QObject {
    Q_OBJECT

  public:
    enum Opcode { UpdateRoomName = 0,
                  UpdateOccupantName,
                  ClearOccupantNames  };

    CouplingTest();

  private slots:
    void handleConnected();
    void handleTimer();

  private:
    QLocalSocket *localSocket_;
    QTimer       *timer_;
    int          counter_;
};


#endif    // ZCAMP14_SCHILD_RASPI_COUPLINGTEST_H__
