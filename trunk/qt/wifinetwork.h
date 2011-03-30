#ifndef WIFINETWORK_H
#define WIFINETWORK_H
#include <QString>

class WifiNetwork
{
    QString ssid;
public:
    WifiNetwork(QString ssid);
    QString getSSID() const;
};

#endif // WIFINETWORK_H
