#include "tecomkeygen.h"

TecomKeygen::TecomKeygen( WifiNetwork * router ) : KeygenThread(router) {}

void TecomKeygen::run(){
    QString result;
    result = QString::fromAscii(QCryptographicHash::hash(
                                router->getSSID().toUpper().toAscii() ,
                                QCryptographicHash::Sha1 )
                                      .toHex().data());
    result.truncate(26);
    if ( stopRequested )
        return;
    this->results.append(result);
}
