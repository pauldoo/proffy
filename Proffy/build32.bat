call "%WINDOWS_SDK_PATH%\bin\setenv.cmd" /Release /x86 /xp

set COMPILE_OPTIONS=/Foobj32\ /O1 /Oy- /Zi /W4 /TP /MD /EHsc /I Proffy/External/include
set SOURCE_FILES=Proffy\Assert.cpp Proffy\ComInitialize.cpp Proffy\CommandLineArguments.cpp Proffy\ConsoleColor.cpp Proffy\DebugEventCallbacks.cpp Proffy\DebugOutputCallbacks.cpp Proffy\Exception.cpp Proffy\Main.cpp Proffy\Results.cpp Proffy\Utilities.cpp Proffy\WriteReport.cpp Proffy\XercesInitialize.cpp
set LINK_OPTIONS=/link /DEBUG /LIBPATH:Proffy/External/lib32 /OPT:REF /DYNAMICBASE
set LIBRARY_FILES=dbgeng.lib xerces-c_3.lib ole32.lib

mkdir bin32
mkdir obj32

cl.exe /Febin32\Proffy32 /Fdbin32\Proffy32 %COMPILE_OPTIONS% %SOURCE_FILES% %LINK_OPTIONS% %LIBRARY_FILES%

cl.exe /Febin32\SampleTarget32 /Fdbin32\SampleTarget32 %COMPILE_OPTIONS% SampleTarget\Main.cpp %LINK_OPTIONS%

copy /y Proffy\External\bin32\*.dll bin32\

