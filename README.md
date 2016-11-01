# JasperReportListener
JasperReportListener is a standalone server that runs jasper reports on demand via a simple http request where you specify:
* A repository name (repo=reponame)
* A Report name (jasper=reportname)
* A pdf file name to be created (out=myoutfile.pdf)
* Zero or more optional parameters to pass to the report (id=1&type=5 etc...)
                
To create a report, make a simple call like this:
http://127.0.0.1:8777/jasper/?out=test.pdf&jasper=exampleEmpty&repo=locale
that will generate a pdf named test.pdf.

No DB required (Despite you will tipically use one for report data)

## runnig Requirements
* jdk 1.8
* jasperstarter ( http://jasperstarter.cenote.de )
  Then specify path into config/repositories.yml

## compiling Requirements
* https://github.com/fbrisa/NanoHTTPDWebServer
* https://github.com/fbrisa/JasperInvoker


## Installation
just copy wherever you need and edit config/repositories.yml to use how many repositories you need.


## Run
multiplatform (All java based)

```bash
java -jar dist/jasperReportListener.jar
```

Then connect to thtp://127.0.0.1:8777 with any browser