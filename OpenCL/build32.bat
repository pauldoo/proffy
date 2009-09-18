call "%WINDOWS_SDK_PATH%\bin\setenv.cmd" /Release /x86 /xp

REM set COMPILE_OPTIONS=/Ox /fp:fast /arch:SSE2 /Zi /W4 /TC /MD /D_CRT_SECURE_NO_WARNINGS /I "C:\Program Files (x86)\ATI Stream\include"
set COMPILE_OPTIONS=/Ox /fp:fast /arch:SSE2 /Zi /W4 /TC /MD /D_CRT_SECURE_NO_WARNINGS /I "C:\ProgramData\NVIDIA Corporation\NVIDIA GPU Computing SDK\OpenCL\common\inc"
set SOURCE_FILES=main.c common.c warpopencl.c warpvanilla.c warpvanilla2.c warpvanillaf.c

REM set LINK_OPTIONS=/link /DEBUG /OPT:REF /DYNAMICBASE /LIBPATH:"C:\Program Files (x86)\ATI Stream\lib\x86"
set LINK_OPTIONS=/link /DEBUG /OPT:REF /DYNAMICBASE /LIBPATH:"C:\ProgramData\NVIDIA Corporation\NVIDIA GPU Computing SDK\OpenCL\common\lib\Win32"
set LIBRARY_FILES=OpenCL.lib

cl.exe /Fetest /Fdtest %COMPILE_OPTIONS% %SOURCE_FILES% %LINK_OPTIONS% %LIBRARY_FILES%
IF %ERRORLEVEL% NEQ 0 exit 1
mt.exe -manifest test.exe.manifest -outputresource:test.exe;1
IF %ERRORLEVEL% NEQ 0 exit 1


