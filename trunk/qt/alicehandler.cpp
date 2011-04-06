#include "alicehandler.h"
#include <iostream>

AliceHandler::AliceHandler(QString a) : alice(a)
{

}
AliceHandler::~AliceHandler(){
    cleanInfo();
}
bool AliceHandler::isSupported(){
    return !this->supportedAlice.isEmpty();
}

QVector<AliceMagicInfo *> & AliceHandler::getSupportedAlice(){
    return this->supportedAlice;
}


bool AliceHandler::startElement(const QString &  ,
                              const QString & localName ,
                              const QString &,
                              const QXmlAttributes &attributes)
{
    int magic[2];
    QString serial;
    QString mac;
    bool status;
    if ( alice.toLower() == localName.toLower() )
    {
            serial = attributes.value("sn");
            mac = attributes.value("mac");
            magic[0] = attributes.value("q").toInt(&status, 10);
            magic[1] = attributes.value("k").toInt(&status, 10);
            supportedAlice.append(new AliceMagicInfo(alice, magic, serial, mac));
    }

    return true;
}


bool AliceHandler::fatalError(const QXmlParseException &exception)
{
    std::cerr << "Parse error at line " << exception.lineNumber()
              << ", " << "column " << exception.columnNumber() << ": "
              << qPrintable(exception.message()) << std::endl;
    return false;
}
bool AliceHandler::readFile(const QString &fileName)
{
    if ( !supportedAlice.isEmpty() )
        cleanInfo();
    QFile file(fileName);
    QXmlInputSource inputSource(&file);
    QXmlSimpleReader reader;
    reader.setContentHandler(this);
    reader.setErrorHandler(this);
    return reader.parse(inputSource);
}

void AliceHandler::cleanInfo(){
    for ( int i = 0 ; i < supportedAlice.size();++i )
        delete supportedAlice.at(i);
    supportedAlice.clear();
}
