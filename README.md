<div align="center">
<h1>Chat-Service</h1>
<h3> 멘토와 멘티의 대화 서비스</h3>
</div>


## Architecture
<img width="9116" alt="인프라아키텍처" src="https://github.com/Dokcer-DevLink/chat-service/assets/80077569/871ed4c5-f886-47a1-9171-69f09cd96883">

## Description

### - Spring Websocket Message Broker 사용
구현의 용이성을 고려하여 SpringBoot에서 제공하는 WebsocketMessageBroker를 사용하였고 STOMP 엔드포인트를 열어 STOMP 통신을 하였다. 

### - Kafka 연동
SpringBoot 내부에서 제공하는 메시지브로커를 사용하다보니 여러 서버에서 메시지브로커를 공유하지 못하는 문제가 발생했다. 
WebsocketMessageBroker를 걷어내고 Redis의 pub/sub 사용을 고려했지만 개발시간이 부족하여 Kafka 연동으로 선회하였다. 채팅 서버의 Kafka 컨슈머의 컨슈머 그룹을 각기 달리하여 토픽을 구독하므로써, 토픽으로 들어온 메시지가 모든 서버에 전달되도록 하였다.

### - QueryDSL 사용
간단한 쿼리는 Spring Data JPA로 자동생성하였고 복잡한 쿼리는 타입 안정성과 동적 쿼리 생성을 고려하여 QueryDSL를 사용하였다.


## Issue

### - AWS EKS 환경에서 웹소켓 통신이 안되는 현상 발생

AWS 로드밸런서의 타입이 classic인 경우 웹소켓 통신을 지원하지 않는다. 로드밸런서 타입을 Network로 변경하여 웹소켓 통신 문제를 해결하였다.

### - Kafka를 연동하였는데 메시지가 하나의 서버로만 전달되는 현상 발생

채팅 서버의 컨슈머를 모두 동일한 컨슈머 그룹으로 묶어 놓아 발생한 문제였다. Kafka는 컨슈머 그룹 단위로 offset을 관리한다. 채팅 토픽의 파티션으로 들어온 메시지를 모든 서버가 받으려면, 각 서버의 컨슈머는 각기 다른 컨슈머 그룹에 속해야 한다.

### - 컨슈머 그룹 ID 동적생성 문제 발생

컨슈머 마다 각자의 컨슈머 그룹을 가지려면 각기 다른 컨슈머 그룹 ID를 가져야 하는데, 컨슈머 그룹 ID는 정적으로 생성되는 데이터였다. 정적으로 생성되는 데이터를 동적으로 서버마다 다르게 생성해야 하는 아이러니한 상황이었다. 정적인 데이터를 동적으로 변경하기 위해 SpEL를 사용하였다.
SpEL은 런타임에 객체를 조작하는데 사용하는 강력한 언어이다. static final로 생성된 전역 상수에 UUID를 SpEL을 사용하여 랜덤 생성하였다. 이로써 서버마다 다른 컨슈머 그룹 ID를 갖는 것이 가능해졌다.