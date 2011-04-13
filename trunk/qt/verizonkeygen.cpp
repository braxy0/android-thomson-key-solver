#include "verizonkeygen.h"

VerizonKeygen::VerizonKeygen( WifiNetwork * router ) : KeygenThread(router) {}

void VerizonKeygen::run(){
    QChar * inverse = new QChar[5];
    inverse[0] = router->getSSID().at(4);
    inverse[1] = router->getSSID().at(3);
    inverse[2] = router->getSSID().at(2);
    inverse[3] = router->getSSID().at(1);
    inverse[4] = router->getSSID().at(0);
    bool test;
    int resultInt = QString::fromRawData(inverse , 5).toInt(&test, 36);
    if ( !test )
    {
        return; //TODO: error message
    }
    QString result;
    result.setNum(resultInt , 16);
    while ( result.size() < 6 )
        result = "0" + result;
    if ( router->getMac().isEmpty() )
    {
        results.append("1801" + result.toUpper());
        results.append("1F90" + result.toUpper());
    }
    else
        results.append(router->getMac().mid(2,4) + result.toUpper());
}
