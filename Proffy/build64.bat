call "%WINDOWS_SDK_PATH%\bin\setenv.cmd" /Release /x64 /xp

set COMPILE_OPTIONS=/Foobj64\ /O1 /GL /Oy- /Zi /W4 /TP /MD /EHsc /I Proffy/External/include
set SOURCE_FILES=Proffy\Assert.cpp Proffy\ComInitialize.cpp Proffy\CommandLineArguments.cpp Proffy\ConsoleColor.cpp Proffy\DebugEventCallbacks.cpp Proffy\DebugOutputCallbacks.cpp Proffy\Exception.cpp Proffy\LookupSymbols.cpp Proffy\Main.cpp Proffy\Results.cpp Proffy\Utilities.cpp Proffy\WriteReport.cpp Proffy\XercesInitialize.cpp
set LINK_OPTIONS=/link /DEBUG /LIBPATH:Proffy/External/lib64 /OPT:REF /DYNAMICBASE
set LIBRARY_FILES=dbgeng.lib xerces-c_3.lib ole32.lib

mkdir bin64
mkdir obj64

cl.exe /Febin64\Proffy64 /Fdbin64\Proffy64 %COMPILE_OPTIONS% %SOURCE_FILES% %LINK_OPTIONS% %LIBRARY_FILES%
IF %ERRORLEVEL% NEQ 0 exit 1
mt.exe -manifest bin64\Proffy64.exe.manifest -outputresource:bin64\Proffy64.exe;1
IF %ERRORLEVEL% NEQ 0 exit 1

cl.exe /Febin64\SampleTarget64 /Fdbin64\SampleTarget64 %COMPILE_OPTIONS% /GL- SampleTarget\Main.cpp %LINK_OPTIONS%
IF %ERRORLEVEL% NEQ 0 exit 1
mt.exe -manifest bin64\SampleTarget64.exe.manifest -outputresource:bin64\SampleTarget64.exe;1
IF %ERRORLEVEL% NEQ 0 exit 1

copy /y Proffy\External\bin64\*.dll bin64\
IF %ERRORLEVEL% NEQ 0 exit 1

