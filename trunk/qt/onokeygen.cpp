#include "onokeygen.h"

OnoKeygen::OnoKeygen(WifiNetwork * router ) : KeygenThread(router){}


void OnoKeygen::run(){
        if ( router == NULL)
                return;
        if ( router->getSSID().size() != 13 )
        {
                //TODO
                return;
        }
        QString val = "";
        val.setNum(router->getSSID().at(12).digitValue() + 1, 10);
        val = router->getSSID().left(12) + val;
        int pseed[4];
        pseed[0] = 0;
        pseed[1] = 0;
        pseed[2] = 0;
        pseed[3] = 0;
        int randNumber = 0;
        for (int i = 0; i < val.length(); i++)
        {
                pseed[i%4] ^= (int) val.at(i).toAscii();
        }
        randNumber = pseed[0] | (pseed[1] << 8) | (pseed[2] << 16) | (pseed[3] << 24);
        short tmp = 0;
        QString key = "", aux= "";

        for (int j = 0; j < 5; j++)
        {
                randNumber = (randNumber * 0x343fd + 0x269ec3) & 0xffffffff;
                tmp = (short) ((randNumber >> 16) & 0xff);
                aux.setNum(tmp,16);
                key += aux.toUpper();
        }
        results.append(key);
        key = QString::fromAscii(QCryptographicHash::hash(
                                    padto64(val).toAscii() ,
                                    QCryptographicHash::Md5 )
                                          .toHex().data());
        key.truncate(26);
        results.append(key.toUpper());
        return;
}


QString OnoKeygen::padto64( QString val ){
        if ( val == "" )
                return "";
        QString ret = "";
        for ( int i = 0; i < ( 1 + (64 / (val.length())) ) ; ++i)
                ret += val;
        return ret.left(64);
}
