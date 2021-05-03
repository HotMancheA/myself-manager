@echo off
rem 设置监听的端口号
set port=3000
echo port : %port%
 
for /f "usebackq tokens=1-5" %%a in (`netstat -ano ^| findstr %port%`) do (
	if [%%d] EQU [LISTENING] (
		set pid=%%e
	)
)
 
for /f "usebackq tokens=1-5" %%a in (`tasklist ^| findstr %pid%`) do (
	set image_name=%%a
)
 
echo now will kill process : pid %pid%, image_name %image_name%

rem 根据进程ID，kill进程
taskkill /f /pid %pid%
exit