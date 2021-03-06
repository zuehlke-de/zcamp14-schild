#include "occupantlistmodel.h"

#include <QVariant>
#include <iostream>

int OccupantListModel::rowCount(const QModelIndex &parent) const {
    return stringList.count();
}

QVariant OccupantListModel::data(const QModelIndex &index, int role) const {
    if (!index.isValid())
        return QVariant();

    if (index.row() >= stringList.size())
        return QVariant();

    if (role >= 0)
        return stringList.at(index.row());
    else
        return QVariant();
}

void OccupantListModel::addName(QString name) {
    beginInsertRows(QModelIndex(), stringList.count(), stringList.count());
    stringList.append(name);
    endInsertRows();
}

void OccupantListModel::clearNames() {
    int numberOfNamesBeforeClearing = stringList.count();
    beginRemoveRows(QModelIndex(), 0, numberOfNamesBeforeClearing);
    stringList.clear();
    endRemoveRows();
}


void OccupantListModel::ClearOccupantNames() {
    std::cout << "OccupantListModel::ClearOccupantNames()" << std::endl;
    clearNames();
}

void OccupantListModel::ReceiveOccupantName(QString occupantName) {
    std::cout << "OccupantListModel::ReceiveOccupantName()" << std::endl;
    addName(occupantName);
}
