#ifndef KEYGENTHREAD_H
#define KEYGENTHREAD_H
#include <QCryptographicHash>
#include <QThread>
#include <QTextStream>

class KeygenThread : public QThread
{   
    public:
        KeygenThread();
        QString router;
        QString result;
    protected:
        QCryptographicHash * hash;
};

#endif // KEYGENTHREAD_H
