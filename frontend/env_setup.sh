#! /bin/bash

ENV_VAR_PLACEHOLDERS=( "APP_API_URL" )
ENV_VAR_REPLACEMENTS=( "$APP_API_URL" )

for i in "${!ENV_VAR_PLACEHOLDERS[@]}"; do
    placeholder="${ENV_VAR_PLACEHOLDERS[i]}"
    replacement="${ENV_VAR_REPLACEMENTS[i]}"

    files=( $( grep -ir "$placeholder" ./dist | awk -v FS=':' '{ print $1 }' ) )

    for f in "${files[@]}"; do
        sed -i "s%$placeholder%$replacement%g" "$f"
    done
done
