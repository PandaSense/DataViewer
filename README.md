# DataViewer
DataViewer is an lightweight database utility written entirely in Java. Using the flexibility provided by Java Database Connectivity (JDBC), DataViewer provides a simple way to interact with some databases include DB2, Oracle, MySQL, and Microsoft SQL SERVER from simple queries to table and updates table of an entire schema's data. And DataViewer keeps some customized functions to streamline operations for Testing Team includes Super Copying, Table Name Auto-Completed, SQL Result Exported, Auto-Updated etc. DataViewer can be executed in most of OS include Windows, Linux, Mac

# Release History
  - DataViewer 1.0.0 release

# Requirements
  - JDK 1.7 for developing and compiling
  - JRE 1.7 or above for executing include Oracle or IBM
  - NSIS (Nullsoft Scriptable Install System) for creating windows executable file

# Installation
  1. Download source zip file into your local machine and unzip the file.
  2. Create normal java project with your java ide include Eclipse or Intelij IDEA.
  3. Copy all of source files under src folder into your local src folder in your java project.
  4. Copy lib folders into root path for your local project.
  5. Add all of jar files into your local classpath for your project to compile java code with your ide.
  6. You need create two jars include DataViewerLauncher.jar and DataViewer.jar.
  7. com.dv.ui.DataViewer belongs to DataViewerLauncher.jar, and you also need MANIFEST.MF to create executable jar for OS.
  8. All of other java files belong to DataViewer.jar, this is normal jar file.
  9. Copy DataViewerLauncher.jar and DataViewer.jar into lib folder that you have created.
  10. Double click DataViewerLauncher.jar to load DataViewer for all of OS, and DataViewer.exe and DataViewer.bat belong for Windwows.

# Configuration
Usually you don't need to set DataViewer, but you always can find config information in {user.home}/.dataviewer/ folder include database and normal setting etc.

# ScreenCapture

![DataViewer UI](https://github.com/PandaSense/DataViewer/raw/master/screencapture/DataViewer_UI.png)
![DataViewer Path](https://github.com/PandaSense/DataViewer/raw/master/screencapture/DataViewer_path.png)
![DataViewer Lib Path](https://github.com/PandaSense/DataViewer/raw/master/screencapture/DataViewer_lib.png)

