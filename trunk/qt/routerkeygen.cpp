#include "routerkeygen.h"
#include "ui_routerkeygen.h"

RouterKeygen::RouterKeygen(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::RouterKeygen)
{
    ui->setupUi(this);
}

RouterKeygen::~RouterKeygen()
{
    delete ui;
}
