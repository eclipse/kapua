FROM jboss/keycloak:latest

MAINTAINER Jens Reimann <jreimann@redhat.com>
LABEL maintainer "Jens Reimann <jreimann@redhat.com>"

COPY maven /

USER 0

RUN chown -R jboss:0 /opt/jboss && \
    chmod -R g=u /opt/jboss

USER jboss

VOLUME /opt/jboss/keycloak/standalone/data

ENTRYPOINT ["/opt/jboss/run-keycloak"]
#CMD ["-b", "0.0.0.0"] # TODO: maybe leave this, since it's useful for openshift?
