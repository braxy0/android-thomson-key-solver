#include "discuskeygen.h"

DiscusKeygen::DiscusKeygen(WifiNetwork * router ) : KeygenThread(router){}

void DiscusKeygen::run(){
    unsigned int routerSSIDint = strtol( router->getSSIDsubpart().toUtf8().data(), NULL , 16);
    QString result;
    result.setNum((routerSSIDint- 0xD0EC31)>>2);
    result = "YW0" + result;
    results.append(result);
}
