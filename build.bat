set WINDOWS_SDK_PATH=C:\Program Files\Microsoft SDKs\Windows\v7.1
set PATH=%PATH%;c:\cygwin64\bin

c:\cygwin64\bin\sh getversion.sh > Proffy\Version.inc
IF %ERRORLEVEL% NEQ 0 exit 1

cmd.exe /E:ON /V:ON /C build32.bat
IF %ERRORLEVEL% NEQ 0 exit 1
cmd.exe /E:ON /V:ON /C build64.bat
IF %ERRORLEVEL% NEQ 0 exit 1

mkdir dist
mkdir dist\bin32
mkdir dist\bin64

del *.txt~
copy /y *.txt dist\
IF %ERRORLEVEL% NEQ 0 exit 1
copy /y Proffy\External\bin32\*.dll dist\bin32\
IF %ERRORLEVEL% NEQ 0 exit 1
copy /y Proffy\External\bin64\*.dll dist\bin64\
IF %ERRORLEVEL% NEQ 0 exit 1
copy /y Proffy\Launcher.h dist\
IF %ERRORLEVEL% NEQ 0 exit 1
copy /y Proffy\Xhtml.xsl dist\
IF %ERRORLEVEL% NEQ 0 exit 1
copy /y bin32\Proffy32.exe dist\bin32\
IF %ERRORLEVEL% NEQ 0 exit 1
copy /y bin32\Proffy32.pdb dist\bin32\
IF %ERRORLEVEL% NEQ 0 exit 1
copy /y bin64\Proffy64.exe dist\bin64\
IF %ERRORLEVEL% NEQ 0 exit 1
copy /y bin64\Proffy64.pdb dist\bin64\
IF %ERRORLEVEL% NEQ 0 exit 1

