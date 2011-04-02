#include "keygenthread.h"

KeygenThread::KeygenThread( WifiNetwork * r ) : router(r), stopRequested(false)
{
}

QVector<QString> KeygenThread::getResults() const{
    return this->results;
}

void KeygenThread::stop(){
   stopRequested = true;
}

bool KeygenThread::isStopped(){
   return this->stopRequested ;
 }
