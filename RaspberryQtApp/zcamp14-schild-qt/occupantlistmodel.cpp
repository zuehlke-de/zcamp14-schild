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

    if (role == NameRole)
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
    emit beginRemoveRows(QModelIndex(), 0, numberOfNamesBeforeClearing);
    stringList.clear();
    emit endRemoveRows();
}

QHash<int, QByteArray> OccupantListModel::roleNames() const {
    QHash<int, QByteArray> roles;
    roles[NameRole] = "name";
    return roles;
}


void OccupantListModel::ClearOccupantNames() {
    std::cout << "OccupantListModel::ClearOccupantNames()" << std::endl;
    clearNames();
}

void OccupantListModel::ReceiveOccupantName(QString occupantName) {
    std::cout << "OccupantListModel::ReceiveOccupantName()" << std::endl;
    addName(occupantName);
}
