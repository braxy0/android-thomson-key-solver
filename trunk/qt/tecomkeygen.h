#ifndef TECOMKEYGEN_H
#define TECOMKEYGEN_H
#include "keygenthread.h"

class TecomKeygen : public KeygenThread
{
    public:
        TecomKeygen();
        void run();
};

#endif // TECOMKEYGEN_H
