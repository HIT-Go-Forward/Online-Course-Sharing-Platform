export ROOT=/hgf_root
export BACKEND_ROOT=$ROOT/backend/Online-Course-Sharing-Platform
export TOMCAT_WEBAPPS=$ROOT/environments/apache-tomcat-8.5.41/webapps

cd $BACKEND_ROOT

git reset HEAD --hard
git pull origin master

sed -i 's/$mysqlPasswd/xxxxxx#/g' $BACKEND_ROOT/action-server/src/main/webapp/WEB-INF/dispatcher-servlet.xml

mvn clean
mvn package
cp $BACKEND_ROOT/action-server/target/ROOT.war $TOMCAT_WEBAPPS/ROOT.war
