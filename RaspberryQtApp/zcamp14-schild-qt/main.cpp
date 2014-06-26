#include "qtquick1applicationviewer.h"
#include "update.h"
#include "Ble.h"

#include <QApplication>
#include <QDeclarativeContext>


const QString roomname = "Raum 1.01";

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);

    Update update;
    update.ReceiveRoomName("Raum 1.01");
    update.ReceiveOccupantName("Anna Anger");
    update.ReceiveOccupantName("Barbara Bauer");
    update.ReceiveOccupantName("Christian Corn");
    update.ReceiveOccupantName("Dennis Dornbusch");

    Ble *ble = new Ble();
    QObject::connect(ble, SIGNAL(roomNameUpdate(QString)), &update, SLOT(ReceiveRoomName(QString)));
    QObject::connect(ble, SIGNAL(occupantNameUpdate(QString)), &update, SLOT(ReceiveOccupantName(QString)));
    QObject::connect(ble, SIGNAL(occupantNamesInvalidated()), &update, SLOT(ClearOccupantNames()));

    QtQuick1ApplicationViewer viewer;

    viewer.rootContext()->setContextProperty("roomName", update.getRoomName());
    viewer.rootContext()->setContextProperty("namesModel",  QVariant::fromValue(update.getOccupantNames()));

    viewer.addImportPath(QLatin1String("modules"));
    viewer.setOrientation(QtQuick1ApplicationViewer::ScreenOrientationAuto);
    viewer.setMainQmlFile(QLatin1String("qrc:///main.qml"));
    viewer.showExpanded();

    int result = app.exec();

    delete ble;
    return result;
}


