#include "tecomkeygen.h"

TecomKeygen::TecomKeygen()
{
    this->hash = new QCryptographicHash(QCryptographicHash::Sha1);
}

void TecomKeygen::run(){
    this->hash->reset();
    QByteArray result = this->hash->hash(router.toAscii() ,QCryptographicHash::Sha1 );
   this->result = this->result.fromAscii(result.toHex().data());
}
