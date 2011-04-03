#include "dlinkkeygen.h"

DlinkKeygen::DlinkKeygen(WifiNetwork * router ) : KeygenThread(router){}

void DlinkKeygen::run(){
    char hash[] =  {
        'X', 'r', 'q', 'a', 'H', 'N',
        'p', 'd', 'S', 'Y', 'w',
        '8', '6', '2', '1', '5'};

    if ( router->getMac().size() < 12 )
    {
        return;
    }
    char * key = new char[21];
    QString mac = router->getMac();
    key[0]=mac.at(11).toAscii();
    key[1]=mac.at(0).toAscii();

    key[2]=mac.at(10).toAscii();
    key[3]=mac.at(1).toAscii();

    key[4]=mac.at(9).toAscii();
    key[5]=mac.at(2).toAscii();

    key[6]=mac.at(8).toAscii();
    key[7]=mac.at(3).toAscii();

    key[8]=mac.at(7).toAscii();
    key[9]=mac.at(4).toAscii();

    key[10]=mac.at(6).toAscii();
    key[11]=mac.at(5).toAscii();

    key[12]=mac.at(1).toAscii();
    key[13]=mac.at(6).toAscii();

    key[14]=mac.at(8).toAscii();
    key[15]=mac.at(9).toAscii();

    key[16]=mac.at(11).toAscii();
    key[17]=mac.at(2).toAscii();

    key[18]=mac.at(4).toAscii();
    key[19]=mac.at(10).toAscii();
    char * newkey = new char[20];
    char t;
    int index = 0;
    for (int i=0; i < 20 ; i++)
    {
        t=key[i];
        if ((t >= '0') && (t <= '9'))
            index = t-'0';
        else
        {
            t= QChar::toUpper((ushort)t);
            if ((t >= 'A') && (t <= 'F'))
                index = t-'A'+10;
            else
            {
                return;
            }
        }
        newkey[i]=hash[index];
    }
    newkey[21] = '\0';
    results.append(QString(newkey));
    delete [] newkey;
    delete [] key;
}
