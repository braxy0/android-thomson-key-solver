#include "alicemagicinfo.h"

AliceMagicInfo::AliceMagicInfo(  QString a , int ma[2] , QString s , QString m ) :
        alice(a) , serial(s) , mac(m)
{
    this->magic[0] = ma[0];
    this->magic[1] = ma[1];
}
