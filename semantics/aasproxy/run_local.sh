#
# Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
#
# See the AUTHORS file(s) distributed with this work for additional
# information regarding authorship.
#
# See the LICENSE file(s) distributed with this work for
# additional information regarding license terms.
#

#
# Shell script to build and run the aas proxy service for testing purposes.
#
# Prerequisites:
#   Windows, (git)-bash shell, java 11 (java) and maven (mvn) in the $PATH.
#
# Synposis:
#   ./run_local.sh (-build)? (-clean)? (-suspend)? (-debug)?
#
# Comments:
#

DEBUG_PORT=8888
DEBUG_SUSPEND=n
DEBUG_OPTIONS=
PROFILE=dev042

for var in "$@"
do
  if [ "$var" == "-debug" ]; then
    DEBUG_OPTIONS="-agentlib:jdwp=transport=dt_socket,address=${DEBUG_PORT},server=y,suspend=${DEBUG_SUSPEND}"
  else
      if [ "$var" == "-build" ]; then
        mvn install -DskipTests -Dmaven.javadoc.skip=true
      else
        if [ "$var" == "-suspend" ]; then
          DEBUG_SUSPEND=y
        else
          if [ "$var" == "-clean" ]; then
            mvn clean
          fi
        fi
      fi
  fi
done

CALL_ARGS="-classpath ./src/main/resources;target/aasproxy-1.0.0-SNAPSHOT.jar \
           -Dspring.profiles.active=$PROFILE  $DEBUG_OPTIONS\
           org.springframework.boot.loader.JarLauncher"

java ${CALL_ARGS}


