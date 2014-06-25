#include "Ble.h"

#include <QTimer>

#include <iostream>


Ble::Ble() {
    counter_ = 0;

    QTimer *timer = new QTimer(this);
    QObject::connect(timer, SIGNAL(timeout()), this, SLOT(processTimer()));
    timer->start(1000);
}


void Ble::processTimer() {
    std::cout << "timer!" << std::endl;

    ++counter_;
    if (counter_ > 4) {
        counter_ = 0;
        emit occupantNamesInvalidated();
    }
    else {
        emit occupantNameUpdate(QString("Name #") + counter_);
    }
}


