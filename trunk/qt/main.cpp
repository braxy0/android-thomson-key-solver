#include <QtGui/QApplication>
#include "routerkeygen.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    RouterKeygen w;
#if defined(Q_WS_S60)
    w.showMaximized();
#else
    w.show();
#endif

    return a.exec();
}
