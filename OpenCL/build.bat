set WINDOWS_SDK_PATH=C:\Program Files\Microsoft SDKs\Windows\v6.1
cmd.exe /E:ON /V:ON /C build32.bat
IF %ERRORLEVEL% NEQ 0 exit 1
test.exe > output.txt
IF %ERRORLEVEL% NEQ 0 exit 1
pause

