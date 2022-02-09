FROM python:3.9.1

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY requirements.txt /usr/src/app/

RUN pip3 install --no-cache-dir -r requirements.txt

COPY . /usr/src/app

#FLASK CONFIG
ENV FLASK_APP=__main__.py
ENV FLASK_ENV=development
ENV FLASK_DEBUG=TRUE

EXPOSE 8080

ENTRYPOINT ["python3"]

CMD ["-m", "oem_resolver"]