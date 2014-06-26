#ifndef OCCUPANTLISTMODEL_H
#define OCCUPANTLISTMODEL_H

#include <QAbstractListModel>
#include <QStringList>

class OccupantListModel : public QAbstractListModel
{
    Q_OBJECT
public:
    OccupantListModel(QObject *parent = 0) : QAbstractListModel(parent) {}

    int rowCount(const QModelIndex &parent = QModelIndex()) const;
    QVariant data(const QModelIndex &index, int role) const;
    void addName(QString name);
    void clearNames();

public slots:
    void ClearOccupantNames();
    void ReceiveOccupantName(QString occupantName);

private:
    QStringList stringList;
};

#endif // OCCUPANTLISTMODEL_H
