#include "CouplingTest.h"

#include <QLocalSocket>
#include <QTimer>

#include <iostream>
#include <string>


CouplingTest::CouplingTest() {
    std::cout << "Coupling Test" << std::endl;

    counter_ = 0;

    timer_ = new QTimer(this);
    QObject::connect(timer_, SIGNAL(timeout()), this, SLOT(handleTimer()));

    localSocket_ = new QLocalSocket(this);
    QObject::connect(localSocket_, SIGNAL(connected()), this, SLOT(handleConnected()));
    localSocket_->connectToServer("/tmp/ble_coupling");
}

void CouplingTest::handleConnected() {
    std::cout << "connected" << std::endl;

    timer_->start(2000);
}

void CouplingTest::handleTimer() {
    std::cout << "timer" << std::endl;

    std::vector<char> buffer;

    ++counter_;
    if (counter_ % 4 == 0) {
        buffer.push_back((char)ClearOccupantNames);
    }
    else if (counter_ % 7 == 0) {
        std::string name = "Room Name";

        buffer.push_back((char)UpdateRoomName);
        buffer.push_back((char)name.length());
        for (int i = 0; i < name.length(); ++i)
            buffer.push_back(name[i]);
    }
    else {
        std::string name = "Some occupant name";

        buffer.push_back((char)UpdateOccupantName);
        buffer.push_back((char)name.length());
        for (int i = 0; i < name.length(); ++i)
             buffer.push_back(name[i]);
    }

    localSocket_->write(&buffer[0], buffer.size());
}
