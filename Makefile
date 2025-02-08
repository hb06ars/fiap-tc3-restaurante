## Test

unit-test:
	mvn test

integration-test:
	mvn test -P integration-test

system-test:
	mvn test -Psystem-test
	@echo $(TIMESTAMP) [INFO] cucumber HTML report generate in: target/cucumber-reports/cucumber.html

performance-test:
	mvn gatling:test -Pperformance-test

test: unit-test integration-test


report-maven:
	mvn surefire-report:report
	@echo $(TIMESTAMP) [INFO] maven report generate in: $(MVN_REPORT)

report-allure:
	allure serve .\target\allure-results\
