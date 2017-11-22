rem Startup...

set CLASSPATH=%CLASSPATH%;.\;.\dist\lesk-wsd-dsm.jar
rem Add all jars....
for %%i in (".\lib\*.jar") do call ".\cpappend.bat" %%i

set a=%1
set b=%2
set c=%3
set d=%4
set e=%5
set f=%6
set g=%7
set h=%8
set i=%9
shift
set aa=%1
set bb=%2
set cc=%3
set dd=%4
set ee=%5
set ff=%6
set gg=%7
set hh=%8
set ii=%9
shift
set aaa=%1
set bbb=%2
set ccc=%3
set ddd=%4
set eee=%5
set fff=%6
set ggg=%7
set hhh=%8
set iii=%9

java di.uniba.it.exec.RunWSD %a% %b% %c% %d% %e% %f% %g% %h% %i% %aa% %bb% %cc% %dd% %ee% %ff% %gg% %hh% %ii% %aaa% %bbb% %ccc% %ddd% %eee% %fff% %ggg% %hhh% %iii%

pause
