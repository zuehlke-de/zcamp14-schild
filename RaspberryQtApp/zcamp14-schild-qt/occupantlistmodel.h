#ifndef OCCUPANTLISTMODEL_H
#define OCCUPANTLISTMODEL_H

#include <QAbstractListModel>
#include <QStringList>

enum AnimalRoles {
    NameRole = Qt::UserRole + 1
};

class OccupantListModel : public QAbstractListModel
{
    Q_OBJECT
public:
    OccupantListModel(const QStringList &strings, QObject *parent = 0)
        : QAbstractListModel(parent), stringList(strings) {}

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
