#include "Ble.h"

#include <QLocalServer>
#include <QLocalSocket>
#include <QTimer>

#include <iostream>


Ble::Ble() {
    currentSocket_ = 0;
    buffer_.resize(1024);
    bufferFill_    = 0;
    counter_       = 0;

    localServer_ = new QLocalServer(this);
    QObject::connect(localServer_, SIGNAL(newConnection()), this, SLOT(handleNewConnection()));
    localServer_->listen("/tmp/ble_coupling");

    QTimer *timer = new QTimer(this);
    QObject::connect(timer, SIGNAL(timeout()), this, SLOT(processTimer()));
    //timer->start(1000);
}

Ble::~Ble() {
    if (currentSocket_ != 0)
        currentSocket_->close();
}

void Ble::handleNewConnection() {
    std::cout << "new connection" << std::endl;

    if (currentSocket_ != 0) {
        QObject::disconnect(currentSocket_, 0, 0, 0);
        currentSocket_->close();
        delete currentSocket_;
    }

    bufferFill_ = 0;

    currentSocket_ = localServer_->nextPendingConnection();
    QObject::connect(currentSocket_, SIGNAL(readyRead()), this, SLOT(handleReadyRead()));
    handleReadyRead();
}

void Ble::handleReadyRead() {
    std::cout << "ready read" << std::endl;

    while (true) {
        int toRead = buffer_.size() - bufferFill_;
        if (toRead < 0)
            return;
        std::cout << "to_read=" << toRead << std::endl;
        int numRead = (int)currentSocket_->read(&buffer_[bufferFill_], toRead);
        if (numRead <= 0)
            return;
        bufferFill_ += numRead;

        while (int numConsumed = processCommand()) {
            memmove(&buffer_[0], &buffer_[numConsumed], bufferFill_ - numConsumed);
            bufferFill_ -= numConsumed;
        }
    }
}

void Ble::processTimer() {
    std::cout << "timer!" << std::endl;

    ++counter_;
    if (counter_ > 4) {
        counter_ = 0;
        emit occupantNamesInvalidated();
    }
    else {
        emit occupantNameUpdate(QString("Name #") + QString::number(counter_));
    }
}

int Ble::processCommand() {
    if (!bufferFill_)
        return 0;

    switch((Opcode)buffer_[0]) {
        case UpdateRoomName:
            return processUpdateRoomName();
        case UpdateOccupantName:
            return processUpdateOccupantName();
        case ClearOccupantNames:
            return processClearOccupantNames();
        default:
            return 0;
    }
}

int Ble::processUpdateRoomName() {
    std::string name;
    if (!ExtractName(&name))
        return 0;
    std::cout << "updating room name, length=" << name.length() << ", name=" << name.c_str() << std::endl;
    emit roomNameUpdate(QString::fromUtf8(name.c_str()));
    return 2 + name.length();
}

int Ble::processUpdateOccupantName() {
    std::string name;
    if (!ExtractName(&name))
        return 0;
    std::cout << "updating occupant name, length=" << name.length() << ", name=" << name.c_str() << std::endl;
    emit occupantNameUpdate(QString::fromUtf8(name.c_str()));
    return 2 + name.length();
}

int Ble::processClearOccupantNames() {
    emit occupantNamesInvalidated();
    return 1;
}

bool Ble::ExtractName(std::string *name) {
    name->clear();
    if (bufferFill_ < 1)
        return false;
    int length = (int)buffer_[1];
    if (bufferFill_ < 2 + length)
        return false;

    for (int i = 0; i < length; ++i)
        name->push_back(buffer_[2 + i]);
    return true;
}
