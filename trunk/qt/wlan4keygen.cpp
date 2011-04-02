#include "wlan4keygen.h"

Wlan4Keygen::Wlan4Keygen( WifiNetwork * router ) : KeygenThread(router) ,
                            magic("bcgbghgg"){
    this->hash = new QCryptographicHash(QCryptographicHash::Md5);
}

void Wlan4Keygen::run(){
    if ( router == NULL)
        return;
    if ( router->getMac().size() != 12 )
    {
        //TODO:error messages
        return;
    }
    this->hash->reset();
    if (!router->getMac().startsWith("001FA4"))
        this->hash->addData(magic.toAscii());
    QString macMod = router->getMac().left(8).toUpper() + router->getSSIDsubpart();
    if (!router->getMac().startsWith("001FA4"))
        this->hash->addData(macMod.toAscii());
    else
        this->hash->addData(macMod.toLower().toAscii());
    if (!router->getMac().startsWith("001FA4"))
        this->hash->addData(router->getMac().toAscii());
    QString result = QString::fromAscii(this->hash->result().toHex().data());
    result.truncate(20);
    this->results.append(result.toUpper());
    if ( stopRequested )
        return;
}
