call "%WINDOWS_SDK_PATH%\bin\setenv.cmd" /Release /x86 /xp

set COMPILE_OPTIONS=/Ox /fp:fast /arch:SSE2 /Zi /W4 /TC /MD /D_CRT_SECURE_NO_WARNINGS /I "C:\Program Files (x86)\ATI Stream\include"
set SOURCE_FILES=main.c

set LINK_OPTIONS=/link /DEBUG /OPT:REF /DYNAMICBASE /LIBPATH:"C:\Program Files (x86)\ATI Stream\lib\x86"
set LIBRARY_FILES=OpenCL.lib

cl.exe /Fetest /Fdtest %COMPILE_OPTIONS% %SOURCE_FILES% %LINK_OPTIONS% %LIBRARY_FILES%
IF %ERRORLEVEL% NEQ 0 exit 1
mt.exe -manifest test.exe.manifest -outputresource:test.exe;1
IF %ERRORLEVEL% NEQ 0 exit 1


