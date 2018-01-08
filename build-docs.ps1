Remove-Item -Recurse -Force docs
cd raw_docs
mkdocs build --clean
Move-Item site ..\docs