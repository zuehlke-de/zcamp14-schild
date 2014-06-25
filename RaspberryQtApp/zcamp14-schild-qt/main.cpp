#include "qtquick1applicationviewer.h"
#include <QApplication>
#include "nameslistmodel.h"
#include <QDeclarativeContext>

const QString roomname = "Raum 1.01";

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);

    QtQuick1ApplicationViewer viewer;

    viewer.rootContext()->setContextProperty("roomName", roomname);
    viewer.rootContext()->setContextProperty("namesModel", new NamesListModel());

    viewer.addImportPath(QLatin1String("modules"));
    viewer.setOrientation(QtQuick1ApplicationViewer::ScreenOrientationAuto);
    viewer.setMainQmlFile(QLatin1String("qrc:///main.qml"));
    viewer.showExpanded();

    return app.exec();
}


