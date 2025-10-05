# CRM для риелторского агентства

**Описание функционала микросервисаов**

Сервис представляет собой упрощенную систему риелторского агентства управления заданиями и обработки объектов недвижимости:

1. Core CRM 
2. ContentLoaderAdapter 
3. ContentProcessor 
4. PriceHistory
5. NotificationSender 
________________________________________
**Технологии:**

Java 17 (Spring Boot)

Apache Kafka

PostgreSQL

REST API (OpenARI/Swagger)

Apache POI

Docker, Kubernetes
________________________________________
# Запуск микросервисов на локальной машине

**Запустить minikube**

minikube start --driver=docker

**Применяем все конфигурации**

kubectl apply -f kafka-zookeeper.yaml
kubectl apply -f kafka-deployment.yaml
kubectl apply -f create-topic-job.yaml
kubectl apply -f content-loader-service.yaml
kubectl apply -f content-processor-service.yaml
kubectl apply -f price-history-service.yaml
kubectl apply -f notification-service.yaml
kubectl apply -f core-crm-service.yaml

**Загрузить файл через curl**

curl -X POST localhost:8080/api/content-loader/upload -F "file=@C:/Users/oktda/Downloads/cian.xlsx" -F "source=CIAN"

**Посмотрим на данные в БД сервисов Content Processor и Price History**

kubectl exec -it deployment/postgres-service-content-processor -- psql -h postgres-service-content-processor -U postgres -d service_content_processor_db -c "SELECT * FROM estates;"

kubectl exec -it deployment/postgres-service-price -- psql -h postgres-service-price -U postgres -d service_price_db -c "SELECT * FROM estates_history;"

**Загрузить json builder для тестирования core-crm**

curl -X POST -H "Content-Type: application/json" -d "{\"address\": \"Москва, Малый Новопесковский переулок, 8\", \"cadastre\": \"77:04:2522000:31\", \"square\": 134, \"yearBuilt\": \"2015\", \"description\": \"Эффектная 3-комнатная квартира площадью 134 кв.м. в жилом комплексе Дом на Смоленской набережной\", \"district\": \"center\", \"type\": \"APARTMENT\", \"material\": \"brick\", \"price\": 160000000, \"ownerName\": \"unknown\", \"ownerContact\": \"+79168080390\", \"status\": \"NEW\"}" http://localhost:8081/api/buildings

curl -X POST -H "Content-Type: application/json" -d "{\"nickname\": \"nickname1\", \"name\": \"Alex\", \"phoneNumber\": \"+79089082211\", \"email\": \"test@mail.ru\"}" http://localhost:8081/api/users

**Посмотрим на полученные результаты**

kubectl exec -it deployment/postgres-service-core-crm -- psql -h postgres-service-core-crm -U postgres -d service_core_crm_db -c "SELECT * FROM buildings;"

kubectl exec -it deployment/postgres-service-core-crm -- psql -h postgres-service-core-crm -U postgres -d service_core_crm_db -c "SELECT * FROM users;"

