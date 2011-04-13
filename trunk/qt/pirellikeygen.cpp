#include "pirellikeygen.h"

PirelliKeygen::PirelliKeygen(WifiNetwork * router ) : KeygenThread(router){
    this->hash = new QCryptographicHash(QCryptographicHash::Md5);
}

void PirelliKeygen::run(){
    char saltMD5[] = {
                            0x22, 0x33, 0x11, 0x34, 0x02,
                            0x81, 0xFA, 0x22, 0x11, 0x41,
                            0x68, 0x11,	0x12, 0x01, 0x05,
                            0x22, 0x71, 0x42, 0x10, 0x66 };
    bool status;
    char macBytes[6];
    for (int i = 0; i < 12; i += 2)
            macBytes[i / 2] = (router->getSSIDsubpart().mid(i,1).toInt(&status, 16) << 4)
                            + router->getSSIDsubpart().mid(i + 1,1).toInt(&status, 16);
    if ( !status )
        return;
    hash->reset();
    hash->addData(macBytes,6);
    hash->addData(saltMD5 , 20);
    QByteArray resultHash = hash->result();
    char key[5];
    /*Grouping in five groups fo five bits*/
    key[0] = ( resultHash.at(0) & 0xF8) >> 3 ;
    key[1] = ( (resultHash.at(0) & 0x07) << 2) | ( (resultHash.at(1)& 0xC0) >>6 );
    key[2] = ( resultHash.at(1) & 0x3E) >> 1;
    key[3] = ( ( resultHash.at(1) & 0x01) << 4) |  ((resultHash.at(2) & 0xF0) >> 4);
    key[4] = ( (resultHash.at(2) & 0x0F) << 1) |  ((resultHash.at(3) & 0x80) >> 7);
    for ( int i = 0 ; i < 5 ; ++i )
            if ( key[i] >= 0x0A )
                    key[i] += 0x57;
    results.append(QString::fromAscii( QByteArray(key,5).toHex().data() ) );
}
