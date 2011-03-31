#ifndef WIFINETWORK_H
#define WIFINETWORK_H
#include <QString>

class WifiNetwork
{
    private:
        QString ssid;
        QString ssidSubpart;
        QString mac;
        bool supported;
        bool newThomson;
        bool ssidFilter();
        int type;
        void calcEircomMAC();
    public:
        WifiNetwork(QString ssid , QString mac = "");
        QString getSSID() const;
        QString getMac() const;
        QString getSSIDsubpart() const;
        int getType() const;
        bool isSupported() const;
        enum SUPPORTED{
            THOMSON = 0 , DLINK , DISCUS , VERIZON ,
            EIRCOM , PIRELLI , TELSEY , ALICE ,
            COMTREND_4X , HUAWEI, WLAN2 , ONO_WEP ,
            SKY_V1 , WLAN6 ,TECOM , INFOSTRADA
        };
};

#endif // WIFINETWORK_H
