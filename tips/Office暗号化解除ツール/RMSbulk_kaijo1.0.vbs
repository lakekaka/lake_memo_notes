Option Explicit
'########################################
'### Program: RMSBulk_kaijo.vbs
'### Create: 2015.08.19 M.Endo Ver1.0
'### 
'########################################

Dim RMSBulkVer
RMSBulkVer = "Kaijo1.0"

Dim objFShell
Dim objFolderSelect
Dim ans
Dim objWShell
Dim objFileSys
Dim RMSBulkPath
Dim objFolder
Dim ScriptPath





Set objFShell = CreateObject("Shell.Application")
Set objFileSys = CreateObject("Scripting.FileSystemObject")
Set objFolderSelect = objFShell.BrowseForFolder(0, "Office�t�@�C���Í��������Ώۂ̃t�H���_��I�����Ă��������B", 0, "")
If (objFolderSelect Is Nothing) Then
	WScript.Quit
End If
ans = MsgBox(objFolderSelect.Self.Path, vbYesNo, "�Ώۂ͂��̃t�H���_�Ő������ł����H")
If ans = vbYes Then
	Set objFolder = objFileSys.GetFolder(objFolderSelect.Self.Path)
ElseIf ans = vbNo Then
	WScript.Quit
End If

'### ���O���N���A
Set objWShell = CreateObject("WScript.Shell")
ScriptPath = Replace(WScript.ScriptFullName,WScript.ScriptName,"")
Call objWShell.Run("cmd.exe /c @echo RMSBulk_Kaijo.vbs ver." & RMSBulkVer & " > """ & ScriptPath & "RMSBulk_Kaijo.log""",0,True)

'### OS bit����
Set objFileSys = CreateObject("Scripting.FileSystemObject")
If objFileSys.FileExists("C:\Program Files\AD RMS Bulk Protection Tool\RMSBulk.exe") = True Then
	RMSBulkPath = "C:\Program Files\AD RMS Bulk Protection Tool\RMSBulk.exe"
ElseIf objFileSys.FileExists("C:\Program Files (x86)\AD RMS Bulk Protection Tool\RMSBulk.exe") = True Then
	RMSBulkPath = "C:\Program Files (x86)\AD RMS Bulk Protection Tool\RMSBulk.exe"
End If
'wscript.echo("cmd.exe /c """"" & RMSBulkPath & """ /decrypt """ & objFolder & """ >> """ & ScriptPath & "RMSBulk_Kaijo.log""""")
call objWShell.Run("cmd.exe /c """"" & RMSBulkPath & """ /decrypt """ & objFolder & """ >> """ & ScriptPath & "RMSBulk_Kaijo.log""""", 0, True)

Wscript.Echo "�������������܂����B���O�t�@�C�����J���܂��B"

Call objWShell.Run("cmd.exe /c @" & ScriptPath & "RMSBulk_Kaijo.log""",0,True)

