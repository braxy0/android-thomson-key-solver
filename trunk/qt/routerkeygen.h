#ifndef ROUTERKEYGEN_H
#define ROUTERKEYGEN_H

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

private:
    Ui::RouterKeygen *ui;
};

#endif // ROUTERKEYGEN_H
