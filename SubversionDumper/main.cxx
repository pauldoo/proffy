#include <boost/scoped_ptr.hpp>
#include <iostream>
#include <QCoreApplication>
#include <QProcess>
#include <QString>
#include <QStringList>

namespace {
    const int DetermineRevision(QObject& parent, const QString& repositoryPath)
    {
        QString program = "svnlook";
        QStringList arguments;

        arguments << "youngest" << repositoryPath;
        boost::scoped_ptr<QProcess> myProcess(new QProcess(&parent));
        myProcess->start(program, arguments);
        myProcess->waitForFinished(-1);
    
        if (myProcess->exitCode() != EXIT_SUCCESS) {
            throw (std::string("svnlook failed: ") + myProcess->errorString().toStdString());
        }
    
        QString revisionString = myProcess->readAllStandardOutput();
        bool ok;
        int revision = revisionString.toInt(&ok);
        if (!ok) {
            throw (std::string("svnlook returned something strange: ") + revisionString.toStdString());
        }
        
        return revision;
    }
}

int main(int argc, char *argv[])
{
    try {
        std::ios::sync_with_stdio(false);
        QCoreApplication app(argc, argv);
        
        const int currentRevision = DetermineRevision(app, app.arguments().at(1));
        std::cout << "Latest revision: " << currentRevision << std::endl;
    
        //return app.exec();
        return EXIT_SUCCESS;
    } catch (const std::string& error) {
        std::cerr << error << std::endl;
    } catch (...) {
        std::cerr << "Unknown exception" << std::endl;
    }
    return EXIT_FAILURE;
}

