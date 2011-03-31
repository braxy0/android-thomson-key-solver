#include "eircomkeygen.h"

EircomKeygen::EircomKeygen( WifiNetwork * router ) : KeygenThread(router){}


void EircomKeygen::run(){

    if ( router == NULL)
            return;
    if ( router->getMac().size() != 12 )
    {
            //TODO:error messages
            return;
    }
    bool status = false;
    QString result = dectoString(router->getMac().right(6).toInt(&status,16)
                                 + 0x01000000)
                     + "Although your world wonders me, ";
    if ( !status )
    {
            //TODO:error messages
            return;
    }
    result = QString::fromAscii(QCryptographicHash::hash(
                                result.toAscii() ,
                                QCryptographicHash::Sha1 )
                                      .toHex().data());
    result.truncate(26);
    results.append(result);
    return;
}
 QString EircomKeygen::dectoString( int mac){
        QString ret = "";
        while ( mac > 0 ){
                switch (mac %10){
                        case 0: ret = "Zero" + ret;
                                        break;
                        case 1: ret = "One" + ret;
                                        break;
                        case 2: ret = "Two" + ret;
                                        break;
                        case 3: ret = "Three" + ret;
                                        break;
                        case 4: ret = "Four" + ret;
                                        break;
                        case 5: ret = "Five" + ret ;
                                        break;
                        case 6: ret = "Six" + ret;
                                        break;
                        case 7: ret = "Seven" + ret;
                                        break;
                        case 8: ret = "Eight" + ret;
                                        break;
                        case 9: ret = "Nine" + ret;
                                        break;
                }
                mac /=10;
        }
        return ret;
}
