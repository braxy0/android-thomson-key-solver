#include "infostradakeygen.h"

InfostradaKeygen::InfostradaKeygen( WifiNetwork * router ) : KeygenThread(router){}

void InfostradaKeygen::run(){

    if ( router == NULL)
            return;
    if ( router->getMac().size() != 12 )
    {
            //TODO:error messages
            return;
    }
    results.append("2"+router->getMac().toUpper());
    return;
}
