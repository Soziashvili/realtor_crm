# CRM для риелторского агентства

**Описание функционала микросервисаов**

Сервис представляет собой упрощенную систему риелторского агентства управления заданиями и обработки объектов недвижимости:

1. ***Core CRM*** CRM для продаж: назначение заданий на менеджеров, создание автоматических заданий и т.д.<br>
**REST endpoints (Building):**<br>
* Получение всех сущностей <code>GET /api/buildings</code>
* Получение здания по id <code>GET /api/buildings/{id}</code>    
* Создание <code>POST /api/buildings</code>
* Изменение <code>PUT /api/buildings/{id}</code>
* Удаление <code>DELETE /api/buildings/{id}</code><br>
   **REST endpoints (Users):**<br>
* Получение всех <code>GET /api/users</code>
* Получение по id <code>GET /api/users/{id}</code>    
* Создание <code>POST /api/users</code>
* Изменение <code>PUT /api/users/{id}</code>
* Удаление менеджера <code>DELETE /api/users/{id}</code>)<br>
   **REST endpoints (Tasks):**<br>
* Получение всех Tasks <code>GET /api/tasks</code>
* Получение задачи по id <code>GET /api/tasks/{id}</code>    
* Создание <code>POST /api/tasks</code>
* Изменение <code>PUT /api/tasks/{id}</code>
* Назаначение задания на менеджера <code>POST /api/tasks/{id}/assign</code>

2. ***ContentLoaderAdapter*** Загрузка данных по зданиям из xlsx-файлов и отправка в топик Kafka ("buildings-data"). Excel-файлы с данными находятся в папке ./uploads<br>
    ***REST endpoints:*** <br>
* Загрузка xlsx-файла <code>POST /api/content-loader/upload</code>

3. ***ContentProcessor*** Сервис читает данные из Kafka-топика "buildings-data" и батчами обновляет содержимое Estate (недвижимость) объектов.

4. ***PriceHistory*** Читает топик "buildings-data" и хранит все данные по ценам и типу источника (Домклик, Росреестр, Циан) в виде истории.

5. ***NotificationSender*** Читает топик "buildings-data" и отправляет запросы в сервисы для отправки смс и почтовые сервисы.
________________________________________
**Технологии:**

* Java 17 (Spring Boot)
* Apache Kafka
* PostgreSQL
* REST API (OpenARI/Swagger)
* Apache POI
* Docker, Kubernetes
________________________________________
## Запуск микросервисов на локальной машине

**Запустить minikube**

> minikube start --driver=docker

**Применим все конфигурации**

> kubectl apply -f kafka-zookeeper.yaml
> kubectl apply -f kafka-deployment.yaml
> kubectl apply -f create-topic-job.yaml
> kubectl apply -f content-loader-service.yaml
> kubectl apply -f content-processor-service.yaml
> kubectl apply -f price-history-service.yaml
> kubectl apply -f notification-service.yaml
> kubectl apply -f core-crm-service.yaml

**Проверим статус**

> kubectl get all

**Проверим поды**

> kubectl get pods

**Проверим сервисы**

> kubectl get services

**Проверим логи**

> kubectl logs -f service/content-loader
> kubectl logs -f service/content-processor
> kubectl logs -f service/notificaton-processor
> kubectl logs -f service/price-history

**Для загрузки xlsx файла пробросим порт сервиса content-loader к себе на localhost**

> kubectl port-forward service/content-loader 8080:80

**Загрузим файл (например Циан) через curl**

> curl -X POST localhost:8080/api/content-loader/upload -F "file=@[путь к файлу]/cian.xlsx" -F "source=CIAN"

**Посмотрим на данные в БД сервисов Content Processor и Price History**

> kubectl exec -it deployment/postgres-service-content-processor -- psql -h postgres-service-content-processor -U postgres -d service_content_processor_db -c "SELECT * FROM estates;"

> kubectl exec -it deployment/postgres-service-price -- psql -h postgres-service-price -U postgres -d service_price_db -c "SELECT * FROM estates_history;"

**Пробросим порты сервиса core-crm**

> kubectl port-forward service/content-loader 8081:80

**Загрузим объект недвижимости (Builder) и сушность User для тестирования core-crm**

> curl -X POST -H "Content-Type: application/json" -d "{\"address\": \"Москва, Малый Новопесковский переулок, 8\", \"cadastre\": \"77:04:2522000:31\", \"square\": 134, \"yearBuilt\": \"2015\", \"description\": \"Эффектная 3-комнатная квартира площадью 134 кв.м. в жилом комплексе Дом на Смоленской набережной\", \"district\": \"center\", \"type\": \"APARTMENT\", \"material\": \"brick\", \"price\": 160000000, \"ownerName\": \"unknown\", \"ownerContact\": \"+79168080390\", \"status\": \"NEW\"}" http://localhost:8081/api/buildings

> curl -X POST -H "Content-Type: application/json" -d "{\"nickname\": \"nickname1\", \"name\": \"Alex\", \"phoneNumber\": \"+79089082211\", \"email\": \"test@mail.ru\"}" http://localhost:8081/api/users

**Посмотрим на полученные результаты**

> kubectl exec -it deployment/postgres-service-core-crm -- psql -h postgres-service-core-crm -U postgres -d service_core_crm_db -c "SELECT * FROM buildings;"

> kubectl exec -it deployment/postgres-service-core-crm -- psql -h postgres-service-core-crm -U postgres -d service_core_crm_db -c "SELECT * FROM users;"

