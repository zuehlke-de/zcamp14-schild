#include "qtquick1applicationviewer.h"
#include "update.h"
#include "Ble.h"
#include "occupantlistmodel.h"

#include <QApplication>
#include <QDeclarativeContext>

const QString roomname = "Raum 1.01";

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);

    QStringList names = QStringList() << "Anna Anger" << "Barbara Bauer" << "Christian Corn" << "Dennis Dornbusch";

    Update update;
    OccupantListModel *listModel = new OccupantListModel(names);

    Ble *ble = new Ble();
    QObject::connect(ble, SIGNAL(occupantNamesInvalidated()), listModel, SLOT(ClearOccupantNames()));
    QObject::connect(ble, SIGNAL(occupantNameUpdate(QString)), listModel, SLOT(ReceiveOccupantName(QString)));
    QObject::connect(ble, SIGNAL(roomNameUpdate(QString)), &update, SLOT(ReceiveRoomName(QString)));

    QtQuick1ApplicationViewer viewer;

    viewer.rootContext()->setContextProperty("roomName", "Room 1.01");
    viewer.rootContext()->setContextProperty("namesModel", listModel);

    viewer.addImportPath(QLatin1String("modules"));
    viewer.setOrientation(QtQuick1ApplicationViewer::ScreenOrientationAuto);
    viewer.setMainQmlFile(QLatin1String("qrc:///main.qml"));
    viewer.showExpanded();

    int result = app.exec();

    delete ble;
    return result;
}


