#!/usr/bin/env bash

(
mkdir -p /tmp/kapua-containers

TAG=latest
SERVICES=("console" "api" "sql" "broker" "events-broker")

echo "Exporting Kapua images..."
for SERVICE in ${SERVICES[@]}; do
	echo "     kapua-${SERVICE}:${TAG}"
    docker save -o /tmp/kapua-containers/${SERVICE} kapua/kapua-${SERVICE}:${TAG}
done
echo "Exporting Kapua images... DONE!"


eval $(minishift docker-env)

echo "Importing Kapua images into Minishift..."
for SERVICE in ${SERVICES[@]}; do
	echo ""
	echo "kapua-${SERVICE}:${TAG}"
    docker load < /tmp/kapua-containers/${SERVICE}
done
echo "Importing Kapua images into Minishift... DONE!"

rm -rf /tmp/kapua-containers
)