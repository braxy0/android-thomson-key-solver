#include <QtGui/QApplication>
#include "routerkeygen.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    RouterKeygen w;
    w.setWindowIcon(QIcon(":/images/icon.png"));
#if defined(Q_WS_S60)
    w.showMaximized();
#else
    w.show();
#endif

    return a.exec();
}
