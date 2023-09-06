@echo off

SET FolderTraversal="C:\pat-eosl\workspace_web\DST"
SET FileAllocate="D:/filesTraversal.txt"

for /R %FolderTraversal% %%s in (*) do (

echo %%s >> %FileAllocate%

)

pause
