#include "qtquick1applicationviewer.h"
#include <QApplication>
#include <QDeclarativeContext>
#include "update.h"

const QString roomname = "Raum 1.01";

int main(int argc, char *argv[])
{
    Update update;
    update.ReceiveRoomName("Raum 1.01");
    update.ReceiveOccupantName("Anna Anger");
    update.ReceiveOccupantName("Barbara Bauer");
    update.ReceiveOccupantName("Christian Corn");
    update.ReceiveOccupantName("Dennis Dornbusch");

    QApplication app(argc, argv);

    QtQuick1ApplicationViewer viewer;

    viewer.rootContext()->setContextProperty("roomName", update.getRoomName());
    viewer.rootContext()->setContextProperty("namesModel",  QVariant::fromValue(update.getOccupantNames()));

    viewer.addImportPath(QLatin1String("modules"));
    viewer.setOrientation(QtQuick1ApplicationViewer::ScreenOrientationAuto);
    viewer.setMainQmlFile(QLatin1String("qrc:///main.qml"));
    viewer.showExpanded();

    return app.exec();
}


