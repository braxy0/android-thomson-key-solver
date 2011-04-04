#-------------------------------------------------
#
# Project created by QtCreator 2011-03-29T01:35:14
#
#-------------------------------------------------

QT       += core gui

TARGET = qt
TEMPLATE = app


SOURCES += main.cpp\
        routerkeygen.cpp \
    keygenthread.cpp \
    tecomkeygen.cpp \
    wifinetwork.cpp \
    thomsonkeygen.cpp \
    verizonkeygen.cpp \
    infostradakeygen.cpp \
    eircomkeygen.cpp \
    skyv1keygen.cpp \
    wlan4keygen.cpp \
    discuskeygen.cpp \
    wlan2keygen.cpp \
    wlan6keygen.cpp \
    dlinkkeygen.cpp \
    pirellikeygen.cpp

HEADERS  += routerkeygen.h \
    keygenthread.h \
    tecomkeygen.h \
    wifinetwork.h \
    thomsonkeygen.h \
    unknown.h \
    verizonkeygen.h \
    infostradakeygen.h \
    eircomkeygen.h \
    skyv1keygen.h \
    wlan4keygen.h \
    discuskeygen.h \
    wlan2keygen.h \
    wlan6keygen.h \
    dlinkkeygen.h \
    pirellikeygen.h

FORMS    += routerkeygen.ui

CONFIG += mobility
MOBILITY = 
 vendorinfo = \
     "%{\"Example Localized Vendor\"}" \
     ":\"Example Vendor\""

 my_deployment.pkg_prerules = vendorinfo
 DEPLOYMENT += my_deployment
symbian {
    TARGET.UID3 = 0xeb3dcfc3
    # TARGET.CAPABILITY += 
    TARGET.EPOCSTACKSIZE = 0x14000
    TARGET.EPOCHEAPSIZE = 0x020000 0x800000
}
