FROM 10.211.55.6:5000/jboss/wildfly:my-v2
ADD ./target/rsf-sample-backend-1.0.0.war /opt/jboss/wildfly/standalone/deployments/