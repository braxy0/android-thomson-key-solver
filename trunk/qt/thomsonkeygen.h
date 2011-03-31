#ifndef THOMSONKEYGEN_H
#define THOMSONKEYGEN_H
#include "keygenthread.h"

class ThomsonKeygen : public KeygenThread
{
public:
    ThomsonKeygen( WifiNetwork * router );
    void run();
};

#endif // THOMSONKEYGEN_H
