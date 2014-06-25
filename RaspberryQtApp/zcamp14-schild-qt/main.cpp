#include "qtquick1applicationviewer.h"
#include <QApplication>
#include "nameslistmodel.h"
#include <QDeclarativeContext>

const QString roomname = "Raum 1.01";

int main(int argc, char *argv[])
{
    QStringList dataList;
    dataList.append("Anna Anger");
    dataList.append("Barbara Bauer");
    dataList.append("Christian Corn");
    dataList.append("Dennis Dornbusch");

    QApplication app(argc, argv);

    QtQuick1ApplicationViewer viewer;

    viewer.rootContext()->setContextProperty("roomName", roomname);
    viewer.rootContext()->setContextProperty("namesModel",  QVariant::fromValue(dataList));

    viewer.addImportPath(QLatin1String("modules"));
    viewer.setOrientation(QtQuick1ApplicationViewer::ScreenOrientationAuto);
    viewer.setMainQmlFile(QLatin1String("qrc:///main.qml"));
    viewer.showExpanded();

    return app.exec();
}


