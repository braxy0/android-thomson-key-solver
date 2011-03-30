#include "wifinetwork.h"

WifiNetwork::WifiNetwork(QString r) : ssid(r)
{
}

QString WifiNetwork::getSSID() const {
    return this->ssid;
}
