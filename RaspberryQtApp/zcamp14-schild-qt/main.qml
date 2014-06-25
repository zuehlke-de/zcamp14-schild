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

    ListModel {

    }

    Image {
        id: image1
        x: 250
        y: 10
        width: 60
        height: 60
        source: "logo.png"
    }

    ListView {
        id: listView1
        x: 20
        y: 50
        width: 110
        height: 160
        model: ListModel {
            ListElement {
                name: "Anna Anger"
            }

            ListElement {
                name: "Barbara Bauer"
            }

            ListElement {
                name: "Christian Corn"
            }

            ListElement {
                name: "Dennis Dornbusch"
            }
        }
        delegate: Item {
            width: 300
            height: 20
            Row {
                id: row1

                Text {
                    text: name
                    anchors.verticalCenter: parent.verticalLeft
                }
            }
        }
    }
}
