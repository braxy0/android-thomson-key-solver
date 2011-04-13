#include "discuskeygen.h"

DiscusKeygen::DiscusKeygen(WifiNetwork * router ) : KeygenThread(router){}

void DiscusKeygen::run(){
    bool status;
    unsigned int routerSSIDint = router->getSSIDsubpart().toInt( &status , 16);
    if ( !status )
        return;
    QString result;
    result.setNum((routerSSIDint- 0xD0EC31)>>2);
    result = "YW0" + result;
    results.append(result);
}
