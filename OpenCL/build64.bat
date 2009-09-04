call "%WINDOWS_SDK_PATH%\bin\setenv.cmd" /Release /x64 /xp

REM /I Proffy/External/include
set COMPILE_OPTIONS=/Ox /fp:fast /Zi /W4 /TC /MD
set SOURCE_FILES=main.c

REM /LIBPATH:Proffy/External/lib64
set LINK_OPTIONS=/link /DEBUG /OPT:REF /DYNAMICBASE
set LIBRARY_FILES=

cl.exe /Fetest /Fdtest %COMPILE_OPTIONS% %SOURCE_FILES% %LINK_OPTIONS% %LIBRARY_FILES%
IF %ERRORLEVEL% NEQ 0 exit 1
mt.exe -manifest test.exe.manifest -outputresource:test.exe;1
IF %ERRORLEVEL% NEQ 0 exit 1

REM copy /y Proffy\External\bin64\*.dll bin64\

