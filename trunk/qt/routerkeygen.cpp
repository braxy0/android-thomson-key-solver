#include "routerkeygen.h"
#include "ui_routerkeygen.h"
#include <QMessageBox>
#include "tecomkeygen.h"


RouterKeygen::RouterKeygen(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::RouterKeygen)
{
    ui->setupUi(this);
    connect( ui->pushButton , SIGNAL( clicked() ), this , SLOT( setText() ) );
}

RouterKeygen::~RouterKeygen()
{
    delete ui;
}

void RouterKeygen::setText()
{
    this->calculator = new TecomKeygen();
    this->calculator->router = "TECOM-AH4222-527A92";
    connect( this->calculator , SIGNAL( finished() ), this , SLOT( update() ) );
    this->calculator->start();
}


void RouterKeygen::update()
{
    ui->label->setText(this->calculator->result);
}
