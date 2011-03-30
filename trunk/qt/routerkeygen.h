#ifndef ROUTERKEYGEN_H
#define ROUTERKEYGEN_H
#include "keygenthread.h"

#include <QMainWindow>

namespace Ui {
    class RouterKeygen;
}

class RouterKeygen : public QMainWindow
{
    Q_OBJECT

public:
    explicit RouterKeygen(QWidget *parent = 0);
    ~RouterKeygen();
public slots:
    void setText();
    void update();
private:
    Ui::RouterKeygen *ui;
    KeygenThread * calculator;
};

#endif // ROUTERKEYGEN_H
