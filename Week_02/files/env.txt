

## Windows

1.管理员身份打开powershell

2.运行
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))

3.执行choco install superbenchmarker

4.输入 sb

执行 sb -u http://localhost:8088/api/hello -c 20 -N 60

## Mac

1.执行brew install wrk
如果显式brew update很慢，可以ctrl+C打断更新

2.输入 wrk

执行 wrk -t8 -c40 -d60s http://localhost:8088/api/hello
