#include "routerkeygen.h"
#include "ui_routerkeygen.h"
#include <QMessageBox>
#include "tecomkeygen.h"
#include "thomsonkeygen.h"
#include "verizonkeygen.h"
#include <QCompleter>
#include <QStringList>

RouterKeygen::RouterKeygen(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::RouterKeygen)
{
    ui->setupUi(this);
    connect( ui->calcButton , SIGNAL( clicked() ), this , SLOT( calculateKeys() ) );

    /*Auto-Complete!*/
    QStringList wordList;
    wordList << "TECOM-AH4222-" << "TECOM-AH4021-" << "Thomson" << "WLAN";
    QCompleter *completer = new QCompleter(wordList, this);
    completer->setCaseSensitivity(Qt::CaseInsensitive);
    completer->setModelSorting(QCompleter::CaseInsensitivelySortedModel);
    ui->inputSSID->setCompleter(completer);
    this->calculator = NULL;
    this->router = NULL;

}

RouterKeygen::~RouterKeygen()
{
    delete ui;
    if ( calculator != NULL )
    {
        calculator->stop();
        delete calculator;
    }
    delete router;
}

void RouterKeygen::calculateKeys()
{//TECOM-AH4222-527A92
    router= new WifiNetwork(ui->inputSSID->text(), "00:1F:90:E2:7E:61");
    if ( !router->isSupported() )
        return;
    switch ( router->getType() )
    {
    case WifiNetwork::THOMSON:
                                this->calculator = new ThomsonKeygen(router);
                                break;
    case  WifiNetwork::VERIZON:
                                this->calculator = new VerizonKeygen(router);
                                break;
    case  WifiNetwork::TECOM:
                                this->calculator = new TecomKeygen(router);
                                break;
    }
    connect( this->calculator , SIGNAL( finished() ), this , SLOT( getResults() ) );
    this->calculator->start();
}


void RouterKeygen::getResults()
{
    ui->listWidget->clear();
    listKeys = this->calculator->getResults();
    for ( int i = 0 ; i < listKeys.size() ;++i)
        ui->listWidget->insertItem(0,listKeys.at(i) );
    delete calculator;
    calculator = NULL;
}
