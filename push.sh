#!/usr/bin/env bash
services=("scada" "mes" "erp" "web")

docker-compose build
for service in "${services[@]}"
do
    image_name="sdubeerproduction/beerproduction_$service"
    docker push "$image_name"
done
