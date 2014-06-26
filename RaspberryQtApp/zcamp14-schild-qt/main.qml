import QtQuick 1.1

Rectangle {
    visible: true
    width: 320
    height: 240

    FontLoader { id: zuehlke; source: "fonts/AAZuehlke.ttf" }
    FontLoader { id: zuehlkeMedium; source: "fonts/AAZuehlkeMedium.ttf" }
    FontLoader { id: zuehlkeBold; source: "fonts/AAZuehlke-Bold.ttf" }

    property alias roomNameText : raumname.text

    Rectangle {
        width: 320
        height: 50
        y: 5

        Text {
            id: raumname
            font.pixelSize: 32
            font.family: zuehlkeBold.name
            font.bold: true
            color: "#ff830a"
            height: 50
            x: 9
            y: 7

            onTextChanged: {
                lastUpdatedAt.text = "Stand: " + Qt.formatDateTime(new Date(), "dd.MM.yyyy hh:mm")
            }
        }


    }

    ListView {
        id: names
        x: 10
        y: 60
        width: 110
        height: 160
        model: namesModel

        delegate: Text {
            height: 30
            text: display
            font.pixelSize: 24
            font.family: zuehlkeMedium.name

            onTextChanged: {
                lastUpdatedAt.text = "Stand: " + Qt.formatDateTime(new Date(), "dd.MM.yyyy hh:mm")
            }
        }
    }

    Image {
        id: image1
        x: 261
        y: 7
        width: 50
        height: 50
        source: "logo.png"
    }

    Text {
        id: lastUpdatedAt
        x: 198
        y: 224
        color: "grey"
        font.pixelSize: 11
        font.family: zuehlkeMedium.name
    }
}
