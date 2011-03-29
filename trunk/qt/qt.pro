#-------------------------------------------------
#
# Project created by QtCreator 2011-03-29T01:35:14
#
#-------------------------------------------------

QT       += core gui

TARGET = qt
TEMPLATE = app


SOURCES += main.cpp\
        routerkeygen.cpp

HEADERS  += routerkeygen.h

FORMS    += routerkeygen.ui

CONFIG += mobility
MOBILITY = 

symbian {
    TARGET.UID3 = 0xeb3dcfc3
    # TARGET.CAPABILITY += 
    TARGET.EPOCSTACKSIZE = 0x14000
    TARGET.EPOCHEAPSIZE = 0x020000 0x800000
}
