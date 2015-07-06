Name "DataViewer"
Caption "DataViewer"
Icon "DataViewer.ico"
OutFile "DataViewer.exe"
 
SilentInstall silent
AutoCloseWindow true
ShowInstDetails nevershow
 
Section ""
  Call GetJRE
  Pop $R0
 
  ; change for your purpose (-jar etc.)
  StrCpy $0 '"$R0"  -Xms256m -Xmx512m -cp ".\lib\autocomplete.jar;.\lib\db2jcc.jar;.\lib\db2jcc_license_cisuz.jar;.\lib\fife.common.jar;.\lib\htmllexer.jar;.\lib\htmlparser.jar;.\lib\iText-2.1.7.jar;.\lib\iText-rtf-2.1.7.jar;.\lib\log4j-1.2.15.jar;.\lib\poi-3.2-FINAL-20081019.jar;.\lib\rsyntaxtextarea.jar;.\lib\swingx-1.6.1.jar;.\lib\commons-net-3.2.jar;.\lib\jide-oss-master.jar;.\lib\DataViewerLauncher.jar;" com.dv.ui.DataViewer'
 
  SetOutPath $EXEDIR
  ExecWait $0
SectionEnd
 
Function GetJRE 
  Push $R0
  Push $R1
 
  ClearErrors
  StrCpy $R0 "$EXEDIR\jre\bin\javaw.exe"
  IfFileExists $R0 JreFound
  StrCpy $R0 ""
  
  ClearErrors
  ReadEnvStr $R0 "JAVA_HOME"
  StrCpy $R0 "$R0\bin\javaw.exe"
  IfErrors 0 JreFound
   
  IfErrors 0 JreFound
  StrCpy $R0 "javaw.exe"
        
 JreFound:
  Pop $R1
  Exch $R0
FunctionEnd