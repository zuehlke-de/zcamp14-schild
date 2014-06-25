#include <QString>
#include <QColor>
#include <QDebug>
#include <QVariant>
#include <QAbstractListModel>

class NamesListModel : public QAbstractListModel
{
    Q_OBJECT

public:

    explicit NamesListModel(QObject *parent = 0) : QAbstractListModel(parent)
    {
        _model<< QString("Anna Anger");
        _model<< QString("Barbara Bauer");
        _model<< QString("Christian Corn");
        _model<< QString("Dennis Dornbusch");

    }

    // enum DataRoles for QAbstractListModel:
    enum DataRoles {
        NameRole = Qt::UserRole + 1
    };

    // addData() method for QAbstractListModel:
    void addData(const QString& entry) {
        beginInsertRows(QModelIndex(), rowCount(), rowCount());
        _model << entry;
        endInsertRows();
    }

    // rowCount() method for QAbstractListModel:
    int rowCount(const QModelIndex & parent = QModelIndex()) const {
        return _model.count();
    }

    // data() required for QAbstractListModel:
    QVariant data(const QModelIndex & index, int role) const {
        if ( !index.isValid() || index.row() < 0 || index.row() >= _model.count() )
            return QVariant();
        QString modelEntry = _model[index.row()];
        if (role == NameRole) {return modelEntry;}
        return QVariant();
    }

    // roleNames() method for QAbstractListModel:
    QHash<int,QByteArray> roleNames() const {
        QHash<int, QByteArray> roles;
        roles[NameRole] = "Name";
        return roles;
    }

private:
    // Below are the model items:
    QList<QString> _model;
};
