#include "CouplingTest.h"

#include <QApplication>


int main(int argc, char **argv) {
    QApplication application(argc, argv);

    CouplingTest *coupling = new CouplingTest();

    int result = application.exec();

    delete coupling;
    return result;
}
