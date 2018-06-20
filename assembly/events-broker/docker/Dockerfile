FROM enmasseproject/activemq-artemis:2.2.0-1

COPY maven /

USER 0

RUN chown -R artemis:root /opt/artemis && \
    chmod -R g=u /opt/artemis

USER artemis

VOLUME [ "/opt/artemis/data" ]

ENTRYPOINT [ "/run-artemis" ]