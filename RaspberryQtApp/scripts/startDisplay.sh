sudo modprobe spi-bcm2708
sudo modprobe fbtft_device name=adafruitts rotate=90
export FRAMEBUFFER=/dev/fb1
