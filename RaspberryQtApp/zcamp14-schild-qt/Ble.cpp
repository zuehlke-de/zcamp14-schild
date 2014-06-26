#include "Ble.h"

#include <QTimer>

#include <iostream>
#include <sstream>


Ble::Ble() {
    counter_ = 0;

    QTimer *timer = new QTimer(this);
    QObject::connect(timer, SIGNAL(timeout()), this, SLOT(processTimer()));
    timer->start(1000);
}


void Ble::processTimer() {
    std::cout << "timer!" << std::endl;

    std::stringstream output;
    ++counter_;
    if (counter_ > 4) {
        counter_ = 0;
        emit occupantNamesInvalidated();
    }
    else {
        output << "Name #" << counter_;
        emit occupantNameUpdate(QString(output.str().c_str()));
    }
    if (counter_ == 3) {
            output << "Roomname #" << counter_;
            emit roomNameUpdate(QString(output.str().c_str()));
    }
}


