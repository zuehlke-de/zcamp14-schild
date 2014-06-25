import QtQuick 1.1

Rectangle {
    visible: true
    width: 320
    height: 240

    Text {
        id: raumname
        text: roomName
        font.bold: true
        font.pixelSize: 18
        x: 20
        y: 20
    }

    ListView {
        id: names
        x: 20
        y: 50
        width: 110
        height: 160
        model: namesModel
        delegate: Text {
            text: Name
        }
    }

    Image {
        id: image1
        x: 250
        y: 10
        width: 60
        height: 60
        source: "logo.png"
    }
}
