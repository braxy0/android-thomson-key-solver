#include "skyv1keygen.h"

SkyV1Keygen::SkyV1Keygen( WifiNetwork * router ) : KeygenThread(router) , ALPHABET("ABCDEFGHIJKLMNOPQRSTUVWXYZ") {}

void SkyV1Keygen::run(){
    if ( router == NULL)
        return;
    if ( router->getMac().size() != 12 )
    {
            //TODO:error messages
            return;
    }
   QByteArray hash = QCryptographicHash::hash( router->getMac().toAscii() ,
                                QCryptographicHash::Md5 );

    if ( stopRequested )
        return;
    QString key = "";
    for ( int i = 1 ; i <= 15 ; i += 2 )
    {
            unsigned char index = hash[i];
            index %= 26;
            key += ALPHABET.at(index);
    }
    this->results.append(key);
}
