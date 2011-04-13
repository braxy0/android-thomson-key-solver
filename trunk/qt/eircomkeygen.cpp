#include "eircomkeygen.h"
#include <QByteArray>
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
    QString result = "Although your world wonders me, ";
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
        QByteArray ret;
        while ( mac > 0 ){
                switch (mac %10){
                        case 0: ret.prepend("Zero");
                                        break;
                        case 1: ret.prepend("One");
                                        break;
                        case 2: ret.prepend("Two");
                                        break;
                        case 3: ret.prepend("Three");
                                        break;
                        case 4: ret.prepend("Four");
                                        break;
                        case 5: ret.prepend("Five");
                                        break;
                        case 6: ret.prepend("Six");
                                        break;
                        case 7: ret.prepend("Seven");
                                        break;
                        case 8: ret.prepend("Eight");
                                        break;
                        case 9: ret.prepend("Nine");
                                        break;
                }
               // mac /=10;
        }
        return ret;
}
