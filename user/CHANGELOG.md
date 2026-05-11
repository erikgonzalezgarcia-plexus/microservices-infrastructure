# Changelog

Este documento recoge las 5 versiones más recientes del microservicio `user`.

> Nota: el repositorio no dispone de tags ni releases publicadas. Para mantener el alcance acordado, este changelog toma como referencia las 5 actualizaciones más recientes detectadas sobre la carpeta `user` en Git.

## 2025-07-22 - 594936e

### Cambios
- Updated spring boot version
- Deleted more tracer UUID
- First implementation of micrometer

## 2025-05-27 - 51b232e

### Cambios
- Creados `README.md` y `Dockerfile` necesarios
- Actualizados `pom.xml` con cambios necesarios para una mejor detección del IDE
- Cambios en archivos `.yml` en relación con la dockerización
- Dockerfile actualizado en `config-server`

## 2023-09-09 - a74e366

### Cambios
- Creación de nuevos microservicios:
  - Spring Config Server
  - Loyalty microservice para almacenar puntos de usuario tras una compra correcta
- Añadida configuración Kafka en `order` (producer solo y en fase de test)

## 2023-09-06 - ea0f988

### Cambios
- Añadidos casos de uso para recuperación de pedidos y compras de usuario
- Primera implementación de `RestAdapter` (no finalizada)
- Añadidos endpoints necesarios en `OrderController`

## 2023-09-03 - 5998da2

### Cambios
- Añadidos ficheros no versionados que no estaban siendo trackeados
