mkdir local-maven-repo
mvn deploy:deploy-file \
	-Dfile=../common/target/common-0.0.1-SNAPSHOT.jar \
	-DgroupId=dev.zentari \
	-DartifactId=common \
	-Dversion=0.0.1-SNAPSHOT \
	-Durl=file:./local-maven-repo/ \
	-DrepositoryId=local-maven-repo
