#!/bin/sh
export ACTIVEMQ_OPTS="-Dcommons.db.schema.update=true -Dcertificate.jwt.private.key=file:///home/vagrant/key.pk8 -Dcertificate.jwt.certificate=file:///home/vagrant/cert.pem"
export CATALINA_OPTS="-Dcommons.db.schema.update=true -Dcertificate.jwt.private.key=file:///home/vagrant/key.pk8 -Dcertificate.jwt.certificate=file:///home/vagrant/cert.pem"
