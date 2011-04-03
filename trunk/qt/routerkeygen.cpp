#include "routerkeygen.h"
#include "ui_routerkeygen.h"
#include <QMessageBox>
#include "tecomkeygen.h"
#include "thomsonkeygen.h"
#include "verizonkeygen.h"
#include "infostradakeygen.h"
#include "eircomkeygen.h"
#include "skyv1keygen.h"
#include "wlan2keygen.h"
#include "wlan4keygen.h"
#include "wlan6keygen.h"
#include "discuskeygen.h"
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
    wordList << "TECOM-AH4222-" << "TECOM-AH4021-" << "Thomson" << "WLAN"
            << "eircom" << "InfostradaWiFi-" << "SKY" ;
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
   ///router= new WifiNetwork(ui->inputSSID->text(), "00:1F:90:E2:7E:61");
   // router= new WifiNetwork(ui->inputSSID->text());
    router= new WifiNetwork(ui->inputSSID->text(), "00:11:22:33:44:55");
    if ( !router->isSupported() )
        return;
    switch ( router->getType() )
    {
    case WifiNetwork::THOMSON:
                                this->calculator = new ThomsonKeygen(router , false );
                                break;
    case  WifiNetwork::EIRCOM:
                                this->calculator = new EircomKeygen(router);
                                break;
    case  WifiNetwork::VERIZON:
                                this->calculator = new VerizonKeygen(router);
                                break;
    case  WifiNetwork::TECOM:
                                this->calculator = new TecomKeygen(router);
                                break;
    case  WifiNetwork::INFOSTRADA:
                                this->calculator = new InfostradaKeygen(router);
                                break;
    case  WifiNetwork::SKY_V1:
                                this->calculator = new SkyV1Keygen(router);
                                break;
    case WifiNetwork::WLAN2:
                                this->calculator = new Wlan2Keygen(router);
                                break;
    case  WifiNetwork::WLAN4:
                                this->calculator = new Wlan4Keygen(router);
                                break;
    case  WifiNetwork::WLAN6:
                                this->calculator = new Wlan6Keygen(router);
                                break;
    case  WifiNetwork::DISCUS:
                                this->calculator = new DiscusKeygen(router);
                                break;
    case  WifiNetwork::HUAWEI:
                                this->calculator = new InfostradaKeygen(router);
                                break;
    case  WifiNetwork::ALICE:
                                this->calculator = new SkyV1Keygen(router);
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
