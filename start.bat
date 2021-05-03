@echo off
%1 mshta vbscript:CreateObject("WScript.Shell").Run("%~s0 ::",0,FALSE)(window.close)&&exit
cd java-back
java -jar forget-curve-server.jar >nohup.log 2>&1 &
exit