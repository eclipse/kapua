#!/bin/sh
export ACTIVEMQ_OPTS="-Dcommons.db.schema.update=true -Dcertificate.jwt.private.key=/home/vagrant/key.pk8 -Dcertificate.jwt.certificate=/home/vagrant/cert.pem"
export CATALINA_OPTS="-Dcommons.db.schema.update=true -Dcertificate.jwt.private.key=/home/vagrant/key.pk8 -Dcertificate.jwt.certificate=/home/vagrant/cert.pem"
