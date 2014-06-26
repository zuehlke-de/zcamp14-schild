#include "qtquick1applicationviewer.h"
#include "Ble.h"
#include "occupantlistmodel.h"
#include "roomname.h"

#include <QApplication>
#include <QDeclarativeContext>
#include <QGraphicsObject>

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    QtQuick1ApplicationViewer viewer;

    Roomname *roomname = new Roomname();
    OccupantListModel *listModel = new OccupantListModel();

    Ble *ble = new Ble();
    QObject::connect(ble, SIGNAL(roomNameUpdate(QString)), roomname, SLOT(ReceiveRoomName(QString)));
    QObject::connect(ble, SIGNAL(occupantNameUpdate(QString)), listModel, SLOT(ReceiveOccupantName(QString)));
    QObject::connect(ble, SIGNAL(occupantNamesInvalidated()), listModel, SLOT(ClearOccupantNames()));

    viewer.rootContext()->setContextProperty("namesModel", listModel);

    viewer.addImportPath(QLatin1String("modules"));
    viewer.setOrientation(QtQuick1ApplicationViewer::ScreenOrientationAuto);
    viewer.setMainQmlFile(QLatin1String("qrc:///main.qml"));
    viewer.showExpanded();

    QObject *rootObject = viewer.rootObject();
    rootObject->setProperty("roomNameText",QVariant("No roomname set"));
    roomname->setParent(rootObject);

    int result = app.exec();

    delete ble;
    return result;
}


