FROM jboss/keycloak:3.0.0.Final

MAINTAINER Jens Reimann <jreimann@redhat.com>
LABEL maintainer "Jens Reimann <jreimann@redhat.com>"

USER root

COPY kapua-entrypoint.sh /opt/jboss/kapua-entrypoint.sh

RUN chmod a+rw -R /opt/jboss && \
    find /opt/jboss -type d | xargs chmod a+rwx && \
    chmod a+x /opt/jboss/kapua-entrypoint.sh

USER jboss

VOLUME /opt/jboss/keycloak/standalone/data

ENTRYPOINT ["/opt/jboss/kapua-entrypoint.sh"]
CMD ["-b", "0.0.0.0"]
