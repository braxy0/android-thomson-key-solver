#include "thomsonkeygen.h"
#include "unknown.h"
#include <stdio.h>
ThomsonKeygen::ThomsonKeygen( WifiNetwork * router ) : KeygenThread(router){
    this->hash = new QCryptographicHash(QCryptographicHash::Sha1);
}


void ThomsonKeygen::run(){
    QString result;
    int n = sizeof(dic)/sizeof("AAA");
    char input[13];
    input[0] = 'C';
    input[1] = 'P';
    for( int i = 0 ; i < n; ++i  )
      {
          sprintf( input + 6  , "%02X%02X%02X" , (int)dic[i][0]
                                  , (int)dic[i][1], (int)dic[i][2] );
          if ( stopRequested )
              return;
          for ( int year = 4 ; year <= 9 ; ++year )
          {
              for ( int week = 1 ; week <= 52 ; ++week )
              {
                  input[2] = '0' + year/10;
                  input[3] = '0' + year % 10 ;
                  input[4] = '0' + week / 10;
                  input[5] = '0' + week % 10;
                  hash->reset();
                  hash->addData(input,12);
                  result = QString::fromAscii(hash->result().toHex().data());
                  /*
                  * Had to copy the result backwards to match the corret response
                  * No idea why.. But heard about it in some forum...
                  */
                  if (  result.right(6) == router->getSSID().right(6) )
                  {
                        this->results.append(result.toUpper().left(10));
                  }


              }
          }
      }
}
